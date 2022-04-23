package me.TahaCheji.gameData;

import me.TahaCheji.Main;
import me.TahaCheji.gameData.teamManager.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class GamePlayer {

    private final Player player;
    private Game game;
    private BukkitTask regen;
    private PlayerLocation playerLocation;
    private Team team;


    public GamePlayer(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    public GamePlayer(Player player, PlayerLocation location) {
        this.player = player;
        this.playerLocation = location;
    }


    public Player getPlayer() {
        return player;
    }

    public void sendMessage(String message) {
            player.sendMessage(message);
    }


    public Game getGame() {
        return game;
    }

    public PlayerLocation getPlayerLocation() {
        return playerLocation;
    }


    public void setPlayerLocation(PlayerLocation playerLocation) {
        this.playerLocation = playerLocation;
    }


    public void setGame(Game game) {
        this.game = game;
    }

    public void teleport(Location location) {
        if (location == null) {
            return;
        }
            getPlayer().teleport(location);
        }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setRegen(BukkitTask regen) {
        this.regen = regen;
    }
    public BukkitTask getRegen() {
        return regen;
    }

    public String getName() {
            return player.getDisplayName();
    }
}
