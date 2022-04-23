package me.TahaCheji.gameData.teamManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Team {

    public static List<UUID> runners = new ArrayList<UUID>();
    public static List<UUID> taggers = new ArrayList<UUID>();


    public void addToTeam(TeamType type, Player player) {
        if(isInTeam(player)){
            player.sendMessage(ChatColor.GOLD + "[GameManager]: " + ChatColor.GRAY + "You are already in a team!");
        }
        switch (type) {
            case RUNNERS:
                runners.add(player.getUniqueId());
                break;
            case TAGGERS:
                taggers.add(player.getUniqueId());
                break;
        }
        Bukkit.broadcastMessage(ChatColor.GOLD + "[GameManager]: " + player.getDisplayName() + ChatColor.GOLD + " Added to " + type.toString() + " team!");
    }

    public boolean isInTeam(Player player) {
        return runners.contains(player.getUniqueId())
                || taggers.contains(player.getUniqueId());
    }

    public void clearTeam() {
        runners.clear();
        taggers.clear();
    }

    public List<UUID> getRunners() {
        return runners;
    }
    public List<UUID> getTaggers() {
        return taggers;
    }
    public List<UUID> getAllPlayersInTeams() {
        List<UUID> combinedTeams = new ArrayList<UUID>();
        combinedTeams.addAll(runners);
        combinedTeams.addAll(taggers);
        return combinedTeams;

    }

    public TeamType getTeamType(Player player) {
        if(!(isInTeam(player)))
            return null;
        return (runners.contains(player.getUniqueId())?TeamType.RUNNERS:TeamType.TAGGERS);

    }
}
