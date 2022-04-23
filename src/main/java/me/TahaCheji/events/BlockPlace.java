package me.TahaCheji.events;

import me.TahaCheji.Main;
import me.TahaCheji.gameData.Game;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {

    @EventHandler
    public void PlayerBlockPlace (BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Game game = Main.getInstance().getGame(player);
        if (game != null && game.getGamePlayer(player) != null) {
            Block block = e.getBlock();
        }
    }

}
