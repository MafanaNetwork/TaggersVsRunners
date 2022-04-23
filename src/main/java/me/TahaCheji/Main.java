package me.TahaCheji;

import me.TahaCheji.commands.AdminCommand;
import me.TahaCheji.commands.MainCommand;
import me.TahaCheji.gameData.Game;
import me.TahaCheji.gameData.GameData;
import me.TahaCheji.gameData.GamePlayer;
import me.TahaCheji.gameData.PlayerLocation;
import me.TahaCheji.mapUtil.GameMap;
import me.TahaCheji.util.Files;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public final class Main extends JavaPlugin {

    private static Main instance;
    private Set<Game> games = new HashSet<>();
    private Set<Game> activeGames = new HashSet<>();
    public Map<Player, Game> playerGameMap = new HashMap<>();
    public static Set<GamePlayer> players = new HashSet<>();
    private static HashMap<Player, GameMap> playerGameHashMap = new HashMap<>();
    private static HashMap<Player, Game> playerCreateGameHashMap = new HashMap<>();
    public static List<GameMap> activeMaps = new ArrayList<>();
    private static Economy econ = null;


    @Override
    public void onEnable() {
        System.out.println("Starting TaggersVsRunners");
        instance = this;
        String packageName = getClass().getPackage().getName();
        for (Class<?> clazz : new Reflections(packageName, ".listeners").getSubTypesOf(Listener.class)) {
            try {
                Listener listener = (Listener) clazz.getDeclaredConstructor().newInstance();
                getServer().getPluginManager().registerEvents(listener, this);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        try {
            Files.initFiles();
        } catch (IOException | InvalidConfigurationException e2) {
            e2.printStackTrace();
        }

        for (Game game : GameData.getAllSavedGames()) {
            addGame(game);
        }

        if (!setupEconomy()) {
            System.out.print("No econ plugin found.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        for(Game game : activeGames) {
            game.end();
        }


        for(Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(getLobbyPoint());
            GamePlayer gamePlayer = new GamePlayer(player, PlayerLocation.LOBBY);
            addGamePlayer(gamePlayer);
            player.sendMessage(ChatColor.RED + "It is very recommended for you to re join the server this is a reboot");
        }

        getCommand("trAdmin").setExecutor(new AdminCommand());
        getCommand("tr").setExecutor(new MainCommand());
    }

    @Override
    public void onDisable() {
        System.out.println("Stopping TaggersVsRunners");
    }

    public boolean isInGame(Player player) {
        return this.playerGameMap.containsKey(player);
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public void addActiveGame(Game game) {
        activeGames.add(game);
    }

    public void removeActiveGame(Game game) {
        activeGames.remove(game);
    }

    public Game getActiveGame(String name) {
        Game game = GameData.getGame(name);
        if(activeGames.contains(game)) {
            return game;
        } else {
            return null;
        }
    }

    public Game getGame(String gameName) {
        for (Game game : games) {
            if (game.getName().equalsIgnoreCase(gameName)) {
                return game;
            }
        }

        return null;
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        players.add(gamePlayer);
    }

    public Game getGame(Player player) {
        return this.playerGameMap.get(player);
    }

    public void setGame(Player player, Game game) {
        if (game == null) {
            this.playerGameMap.remove(player);
        } else {
            this.playerGameMap.put(player, game);
        }
    }

    public GamePlayer getPlayer(Player player) {
        GamePlayer gPlayer = null;
        for(GamePlayer gamePlayer : players) {
            if(gamePlayer.getPlayer().getUniqueId().toString().contains(player.getUniqueId().toString())) {
                gPlayer = gamePlayer;
            }
        }
        return gPlayer;
    }

    public void addMap (Player player, GameMap gameMap) {
        playerGameHashMap.put(player, gameMap);
    }

    public void removeMap (Player player, GameMap gameMap) {
        playerGameHashMap.remove(player, gameMap);
    }

    public GameMap getMap (Player player) {
       return getPlayerGameHashMap().get(player);
    }

    public static Main getInstance() {
        return instance;
    }

    public Set<Game> getGames() {
        return games;
    }

    public Set<Game> getActiveGames() {
        return activeGames;
    }

    public Map<Player, Game> getPlayerGameMap() {
        return playerGameMap;
    }

    public static Set<GamePlayer> getPlayers() {
        return players;
    }

    public static HashMap<Player, GameMap> getPlayerGameHashMap() {
        return playerGameHashMap;
    }

    public static List<GameMap> getActiveMaps() {
        return activeMaps;
    }

    public static HashMap<Player, Game> getPlayerCreateGameHashMap() {
        return playerCreateGameHashMap;
    }

    private Location lobbyPoint = null;
    public Location getLobbyPoint() {
        if (lobbyPoint == null) {
            int x = 0;
            int y = 0;
            int z = 0;
            String world = "world";

            try {
                x = Main.getInstance().getConfig().getInt("lobby-point.x");
                y = Main.getInstance().getConfig().getInt("lobby-point.y");
                z = Main.getInstance().getConfig().getInt("lobby-point.z");
                world = Main.getInstance().getConfig().getString("lobby-point.world");
            } catch (Exception ex) {
                Main.getInstance().getLogger().severe("Lobby point failed with exception: " + ex);
                ex.printStackTrace();
            }

            lobbyPoint = new Location(Bukkit.getWorld(world), x, y, z);
        }

        return lobbyPoint;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEcon() {
        return econ;
    }
}
