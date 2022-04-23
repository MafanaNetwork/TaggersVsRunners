package me.TahaCheji.tasks;

import me.TahaCheji.gameData.Game;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadLocalRandom;

public class ActiveGameTask extends BukkitRunnable {


    private Game game;
    private int gameTimer;

    public ActiveGameTask(Game game, int gameTimer) {
        this.game = game;
        this.gameTimer = gameTimer;
    }
    @Override
    public void run() {
        if(this.gameTimer <= 0) {
            this.cancel();
            game.getPlayers().remove(game.getTagger());
            game.setWinner(game.getPlayers().get(0));
            //gameScoreBoard.stopUpdating();
        } else {
            gameTimer --;
            game.setGameTime(gameTimer);
            ThreadLocalRandom random = ThreadLocalRandom.current();
            Material[] materials = Material.values();
            ItemStack itemStack = new ItemStack(materials[random.nextInt(materials.length)]);
            if(hasAvaliableSlot(game.getRunner().getPlayer())) {
                game.getRunner().getPlayer().getInventory().addItem(itemStack);
                game.getRunner().sendMessage(ChatColor.GOLD + "[GameManager]: " + "+1 Item");
            }
        }
    }

    public int getGameTimer() {
        return game.getGameTime();
    }

    public void setGameTimer(int gameTimer) {
        this.gameTimer = gameTimer;
    }

    public boolean hasAvaliableSlot(Player player){
        Inventory inv = player.getInventory();
        for (ItemStack item: inv.getContents()) {
            if(item == null) {
                return true;
            }
        }
        return false;
    }


}
