package me.TahaCheji.tasks;

import me.TahaCheji.Main;
import me.TahaCheji.gameData.Game;
import me.TahaCheji.gameData.GamePlayer;
import me.TahaCheji.gameData.PlayerLocation;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;


public class GameRunTask extends BukkitRunnable {

    private Game game;
    private int startIn = 25;
    ActiveGameTask gameTask;

    public GameRunTask(Game game) {
        this.game = game;
        this.game.setState(Game.GameState.PREPARATION);
        for (GamePlayer gamePlayer : game.getPlayers()) {
            gamePlayer.setPlayerLocation(PlayerLocation.GAMELOBBY);
            gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 2, 1);
        }
        this.game.assignSpawnPositions();
        this.game.sendMessage(ChatColor.GOLD + "[Game Manager] "  + "You've been teleported.");
        this.game.sendMessage(ChatColor.GOLD + "[Game Manager] " + "The game will begin in " + this.startIn + " seconds...");
        this.game.setMovementFrozen(true);
    }

    @Override
    public void run() {
        if (startIn <= 1) {
            this.cancel();
            game.start();
            gameTask = new ActiveGameTask(game, game.getGameTime());
            gameTask.runTaskTimer(Main.getInstance(), 0, 20);
        } else {
            startIn -= 1;
            this.game.sendMessage(ChatColor.GOLD + "[Game Manager] " + "The game will begin in " + startIn + " second" + (startIn == 1 ? "" : "s"));
        }
    }

    public ActiveGameTask getGameTask() {
        return gameTask;
    }
}