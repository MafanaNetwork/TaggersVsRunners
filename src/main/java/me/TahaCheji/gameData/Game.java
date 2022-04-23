package me.TahaCheji.gameData;

import com.ryanhcode.tropicalscanner.TropicalScanner;
import me.TahaCheji.Main;
import me.TahaCheji.gameData.teamManager.RunnersKit;
import me.TahaCheji.gameData.teamManager.Team;
import me.TahaCheji.gameData.teamManager.TeamType;
import me.TahaCheji.mapUtil.GameMap;
import me.TahaCheji.scoreboards.InGameScoreBoard;
import me.TahaCheji.scoreboards.LobbyScoreBoard;
import me.TahaCheji.tasks.GameCountdownTask;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game implements GameManager {

    private GamePlayer Runner;
    private GamePlayer Tagger;

    private final String name;
    private final ItemStack gameIcon;
    private final GameMode gameMode;
    private final GameMap map;
    private Location p1Location;
    private Location p2Location;
    private Location lobbySpawn;


    private GameState gameState = GameState.LOBBY;
    private boolean movementFrozen = false;
    private int gameTime = 250;

    private List<GamePlayer> activePlayers = new ArrayList<>();

    public Game(String name, ItemStack gameIcon, GameMode gameMode, GameMap map, Location p1Location, Location p2Location, Location lobbySpawn) {
        this.name = name;
        this.gameIcon = gameIcon;
        this.gameMode = gameMode;
        this.map = map;
        this.p1Location = p1Location;
        this.p2Location = p2Location;
        this.lobbySpawn = lobbySpawn;
    }

    public Game(String name, ItemStack gameIcon, GameMode gameMode, GameMap map) {
        this.name = name;
        this.gameIcon = gameIcon;
        this.gameMode = gameMode;
        this.map = map;
    }

    public InGameScoreBoard inGameScoreBoard = new InGameScoreBoard();
    public GameCountdownTask gameCountdownTask = null;


    @Override
    public void playerJoin(GamePlayer gamePlayer) {
        if (isState(GameState.LOBBY) || isState(GameState.STARTING) || isState(GameState.ACTIVE)) {
            if (activePlayers.size() == 2) {
                gamePlayer.sendMessage(ChatColor.GOLD + "[Game Manager] " + "Error: This game is active.");
                return;
            }
        }
        if (!map.isLoaded()) {
            map.load();
            World world = map.getWorld();
            p1Location.setWorld(world);
            p2Location.setWorld(world);
            lobbySpawn.setWorld(world);
        }
        if(Main.getInstance().isInGame(gamePlayer.getPlayer())) {
            gamePlayer.sendMessage(ChatColor.RED +"[Game Manager] " + "You are already in a game");
            return;
        }
        activePlayers.add(gamePlayer);
        gamePlayer.sendMessage(ChatColor.GOLD + "[Game Manager] " + "[" + activePlayers.size() + "/" + "2" + "]");
        gamePlayer.setPlayerLocation(PlayerLocation.GAMELOBBY);
        gamePlayer.getPlayer().getInventory().clear();
        gamePlayer.getPlayer().getInventory().setArmorContents(null);
        gamePlayer.getPlayer().setGameMode(org.bukkit.GameMode.ADVENTURE);
        gamePlayer.getPlayer().setHealth(gamePlayer.getPlayer().getMaxHealth());
        gamePlayer.teleport(lobbySpawn);
        Main.getInstance().setGame(gamePlayer.getPlayer(), this);
        inGameScoreBoard.setGameScoreboard(gamePlayer);
        setState(GameState.LOBBY);
        if (activePlayers.size() == 2 && !isState(GameState.STARTING)) {
            setState(GameState.STARTING);
            inGameScoreBoard.updateScoreBoard(this);
            sendMessage(ChatColor.GOLD + "[Game Manager] " + "The game will begin in 20 seconds...");
            Main.getInstance().addActiveGame(this);
            start();
            startCountDown();
        }

    }

    @Override
    public void start() {
        setState(GameState.ACTIVE);
        sendMessage(ChatColor.GOLD + "[Game Manager] " + "The game has started.");
        setMovementFrozen(false);
    }

    @Override
    public void end() {
        if (inGameScoreBoard != null) {
            inGameScoreBoard.stopUpdating();
        }

        if(gameCountdownTask != null) {
            gameCountdownTask.getGameRunTask().getGameTask().setGameTimer(300);
            gameCountdownTask.getGameRunTask().getGameTask().cancel();
        }


        if (Tagger != null && Tagger.getPlayer() != null) {
            Tagger.getPlayer().sendMessage(ChatColor.GOLD + "[Game Manager] " + "Game has ended");
            Tagger.getPlayer().teleport(Main.getInstance().getLobbyPoint());
            Tagger.getPlayer().getPlayer().getInventory().clear();
            Tagger.getPlayer().getPlayer().getInventory().setArmorContents(null);
            getTagger().getPlayer().setHealth(20);
            Tagger.setPlayerLocation(PlayerLocation.LOBBY);
            Main.getInstance().playerGameMap.remove(Tagger.getPlayer(), this);
            LobbyScoreBoard lobbyScoreBoard = new LobbyScoreBoard();
            lobbyScoreBoard.setLobbyScoreBoard(Tagger);
            lobbyScoreBoard.updateScoreBoard(Tagger);
            Tagger.getPlayer().removePotionEffect(PotionEffectType.SPEED);
        }
        if (Runner != null && Runner.getPlayer() != null) {
            Runner.getPlayer().sendMessage(ChatColor.GOLD + "[Game Manager] " + "Game has ended");
            Runner.getPlayer().teleport(Main.getInstance().getLobbyPoint());
            Runner.getPlayer().getPlayer().getInventory().clear();
            Runner.getPlayer().getPlayer().getInventory().setArmorContents(null);
            getRunner().getPlayer().setHealth(20);
            Runner.setPlayerLocation(PlayerLocation.LOBBY);
            Main.getInstance().playerGameMap.remove(Runner.getPlayer(), this);
            LobbyScoreBoard lobbyScoreBoard = new LobbyScoreBoard();
            lobbyScoreBoard.setLobbyScoreBoard(Runner);
            lobbyScoreBoard.updateScoreBoard(Runner);
        }
        Main.getInstance().getGames().remove(this);
        resetGameInfo();
        Main.getInstance().getGames().add(this);
        Main.getInstance().removeActiveGame(this);
    }

    @Override
    public void save() throws IOException {
        GameData.saveGame(this);
    }

    @Override
    public void playerLeave(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        if (!Main.getInstance().isInGame(player)) {
            return;
        }
        if (isState(GameState.ACTIVE)) {
            getPlayers().remove(gamePlayer);
            setWinner(getPlayers().get(0));
            return;
        }
        end();
    }

    @Override
    public void resetGameInfo() {
        Tagger = null;
        Runner = null;
        getPlayers().clear();
        gameTime = 300;
        inGameScoreBoard = new InGameScoreBoard();
        map.unload();
    }

    @Override
    public void setWinner(GamePlayer gamePlayer) {
        gamePlayer.getPlayer().sendMessage(ChatColor.GOLD + gamePlayer.getTeam().getTeamType(gamePlayer.getPlayer()).toString()  + " has won the game");
        end();
    }

    @Override
    public void assignSpawnPositions() {
        Team team = new Team();
        int i = 0;
        for (GamePlayer player : getPlayers()) {
            if (i < getPlayers().toArray().length / 2) {
                team.addToTeam(TeamType.RUNNERS, player.getPlayer());
                player.teleport(p2Location);
                player.getPlayer().setGameMode(org.bukkit.GameMode.SURVIVAL);
                Runner = player;
            } else {
                team.addToTeam(TeamType.TAGGERS, player.getPlayer());
                player.teleport(p1Location);
                player.getPlayer().setHealth(player.getPlayer().getMaxHealth());
                player.getPlayer().setGameMode(org.bukkit.GameMode.SURVIVAL);
                Tagger = player;
                player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 1));
            }
            player.getPlayer().setHealth(20);
            player.setTeam(team);
            i++;
        }

        ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta3 = pickaxe.getItemMeta();

        ItemStack blocks = new ItemStack(Material.WHITE_TERRACOTTA, 64);

        meta3.setDisplayName(ChatColor.GREEN + "DIGO-3000");
        meta3.addEnchant(Enchantment.DURABILITY, 5, true);
        meta3.addEnchant(Enchantment.DIG_SPEED, 3, true);
        meta3.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta3.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta3.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        pickaxe.setItemMeta(meta3);

        getTagger().getPlayer().getInventory().clear();
        getTagger().getPlayer().getInventory().setBoots(RunnersKit.RunnersBoots());
        getTagger().getPlayer().getInventory().setLeggings(RunnersKit.RunnersLeggings());
        getTagger().getPlayer().getInventory().setChestplate(RunnersKit.RunnersChestplate());
        getTagger().getPlayer().getInventory().setHelmet(RunnersKit.RunnersHelmet());

        getTagger().getPlayer().getInventory().setItem(0, RunnersKit.RunnersSword());
        getTagger().getPlayer().getInventory().setItem(3, blocks);
        getTagger().getPlayer().getInventory().setItem(4, blocks);
        getTagger().getPlayer().getInventory().setItem(5, pickaxe);
    }

    @Override
    public void startCountDown() {
        gameCountdownTask = new GameCountdownTask(this);
        gameCountdownTask.runTaskTimer(Main.getInstance(), 0, 20);
    }


    public List<GamePlayer> getPlayers() {
        return activePlayers;
    }

    public boolean isState(GameState state) {
        return getGameState() == state;
    }

    public void setState(GameState gameState) {
        this.gameState = gameState;
    }

    public void sendMessage(String message) {
        for (GamePlayer gamePlayer : getPlayers()) {
            gamePlayer.sendMessage(message);
        }
    }

    public GamePlayer getGamePlayer(Player player) {
        for (GamePlayer gamePlayer : getPlayers()) {
            if (gamePlayer.getPlayer() == player) {
                return gamePlayer;
            }
        }
        return null;
    }

    public void setMovementFrozen(boolean movementFrozen) {
        this.movementFrozen = movementFrozen;
    }


    public GamePlayer getRunner() {
        return Runner;
    }

    public GamePlayer getTagger() {
        return Tagger;
    }

    public String getName() {
        return name;
    }

    public ItemStack getGameIcon() {
        return gameIcon;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public GameMap getMap() {
        return map;
    }

    public Location getP1Location() {
        return p1Location;
    }

    public Location getP2Location() {
        return p2Location;
    }

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean isMovementFrozen() {
        return movementFrozen;
    }

    public int getGameTime() {
        return gameTime;
    }

    public List<GamePlayer> getActivePlayers() {
        return activePlayers;
    }

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    public void setP1Location(Location p1Location) {
        this.p1Location = p1Location;
    }

    public void setP2Location(Location p2Location) {
        this.p2Location = p2Location;
    }

    public void setLobbySpawn(Location lobbySpawn) {
        this.lobbySpawn = lobbySpawn;
    }

    public enum GameState {
        LOBBY, STARTING, PREPARATION, ACTIVE, DEATHMATCH, ENDING
    }
}
