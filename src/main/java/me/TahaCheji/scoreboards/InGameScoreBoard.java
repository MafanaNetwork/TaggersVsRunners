package me.TahaCheji.scoreboards;

import me.TahaCheji.Main;
import me.TahaCheji.gameData.Game;
import me.TahaCheji.gameData.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;

public class InGameScoreBoard {

    public int TaskId;

    public void setGameScoreboard(GamePlayer player) {
        Game game = Main.getInstance().getGame(player.getPlayer());
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("TaggersVsRunners", "dummy", ChatColor.GRAY + "♧" + ChatColor.GOLD + "TaggersVsRunners" + ChatColor.GRAY + "♧");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score emptyText1 = obj.getScore(" ");
        emptyText1.setScore(15);

        Team gameInfo = board.registerNewTeam("GameInfo");
        gameInfo.addEntry(ChatColor.BLACK + "" + ChatColor.BLACK);
        gameInfo.setPrefix(ChatColor.GRAY + ">> " + ChatColor.GOLD + "Name: " + game.getName() + " | Mode: " + game.getGameMode().toString() + " | Map: " + game.getMap().getName());
        obj.getScore(ChatColor.BLACK + "" + ChatColor.BLACK).setScore(14);

        Score emptyText2 = obj.getScore("   ");
        emptyText2.setScore(13);

        Team time = board.registerNewTeam("GameTime");
        time.addEntry(ChatColor.BLACK + "" + ChatColor.GOLD);
        time.setPrefix(ChatColor.GRAY + ">> " + ChatColor.GOLD + "Time: " + game.getGameTime());
        obj.getScore(ChatColor.BLACK + "" + ChatColor.GOLD).setScore(12);

        Score emptyText3 = obj.getScore("    ");
        emptyText3.setScore(11);

        Score emptyText4 = obj.getScore(ChatColor.GRAY + ">> " + ChatColor.GOLD + "Team: ");
        emptyText4.setScore(10);

        Score emptyText5 = obj.getScore("      ");
        emptyText5.setScore(9);

        Score score7 = obj.getScore(ChatColor.GRAY + "Mafana.us.to");
        score7.setScore(8);

        player.getPlayer().setScoreboard(board);
    }

    public void updateScoreBoard(Game game) {
       TaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (GamePlayer player : game.getPlayers()) {
                    if(!player.getPlayer().isOnline()) {
                        stopUpdating();
                        return;
                    }
                    Scoreboard board = player.getPlayer().getScoreboard();
                    if(game.getGameTime() <= 0) {
                        board.getTeam("GameTime").setPrefix(ChatColor.GRAY + ">> " + ChatColor.GOLD + "Time: " + "Ending");
                    } else {
                        board.getTeam("GameTime").setPrefix(ChatColor.GRAY + ">> " + ChatColor.GOLD + "Time: " + game.getGameTime());
                    }
                }
            }
        }, 0, 5);
    }

    public void stopUpdating() {
        Bukkit.getScheduler().cancelTask(TaskId);
    }

    public int getTaskId() {
        return TaskId;
    }
}
