package me.TahaCheji.commands;

import me.TahaCheji.Main;
import me.TahaCheji.gameData.Game;
import me.TahaCheji.gameData.GameGui;
import me.TahaCheji.gameData.GamePlayer;
import me.TahaCheji.gameData.PlayerLocation;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class MainCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("tr")) {
            Player player = (Player) sender;
            if (args[0].equalsIgnoreCase("join")) {
                if (args.length == 1) {
                    new GameGui().getGameGui().open(player);
                    return true;
                }
                Game game = Main.getInstance().getGame(args[1]);
                if (game == null) {
                    player.sendMessage(ChatColor.RED +"[Game Manager] " + "That game does not exist");
                    return true;
                }
                GamePlayer gamePlayer = Main.getInstance().getPlayer(player);
                if (Main.getInstance().isInGame(player)) {
                    player.sendMessage(ChatColor.GREEN +"[Game Manager] " +"You are already in a game");
                    return true;
                }
                gamePlayer.setGame(game);
                game.playerJoin(gamePlayer);
            }
            if (args[0].equalsIgnoreCase("leave")) {
                if (Main.getInstance().isInGame(player)) {
                    Game game = Main.getInstance().getGame(player);
                    game.playerLeave(Main.getInstance().getPlayer(player));
                    player.sendMessage(ChatColor.RED +"[Game Manager] " + "You have left the game.");
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("hub")) {
                if (Main.getInstance().isInGame(player)) {
                    Game game = Main.getInstance().getGame(player);
                    game.playerLeave(Main.getInstance().getPlayer(player));
                    return true;
                } else {
                    Main.getInstance().getPlayer(player).setPlayerLocation(PlayerLocation.LOBBY);
                    player.teleport(Main.getInstance().getLobbyPoint());
                }
            }
        }
        return false;
    }
}
