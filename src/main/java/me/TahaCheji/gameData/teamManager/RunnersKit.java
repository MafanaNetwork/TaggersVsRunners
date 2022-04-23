package me.TahaCheji.gameData.teamManager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class RunnersKit {

    public static ItemStack RunnersHelmet() {
        ItemStack ReservoirCap = new ItemStack(Material.IRON_HELMET);
        ItemMeta ReservoirCapMeta = (ItemMeta) ReservoirCap.getItemMeta();
        ReservoirCapMeta.setDisplayName(ChatColor.GOLD + "RunnersCap");
        ReservoirCapMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ReservoirCapMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ReservoirCapMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        ReservoirCapMeta.setUnbreakable(true);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add(ChatColor.GOLD + "[Helmet]");
        ReservoirCapMeta.setLore(lore);
        ReservoirCap.setItemMeta(ReservoirCapMeta);
        return ReservoirCap.clone();
    }

    public static ItemStack RunnersChestplate() {
        ItemStack ReservoirCap = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        ItemMeta ReservoirCapMeta = (ItemMeta) ReservoirCap.getItemMeta();
        ReservoirCapMeta.setDisplayName(ChatColor.GOLD + "RunnersChestplate");
        ReservoirCapMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ReservoirCapMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ReservoirCapMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        ReservoirCapMeta.setUnbreakable(true);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add(ChatColor.GOLD + "[Chestplate]");
        ReservoirCapMeta.setLore(lore);
        ReservoirCap.setItemMeta(ReservoirCapMeta);
        return ReservoirCap.clone();
    }

    public static ItemStack RunnersLeggings() {
        ItemStack ReservoirCap = new ItemStack(Material.IRON_LEGGINGS);
        ItemMeta ReservoirCapMeta = (ItemMeta) ReservoirCap.getItemMeta();
        ReservoirCapMeta.setDisplayName(ChatColor.GOLD + "RunnersLeggings");
        ReservoirCapMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ReservoirCapMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ReservoirCapMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        ReservoirCapMeta.setUnbreakable(true);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add(ChatColor.GOLD + "[Leggings]");
        ReservoirCapMeta.setLore(lore);
        ReservoirCap.setItemMeta(ReservoirCapMeta);
        return ReservoirCap.clone();
    }

    public static ItemStack RunnersBoots() {
        ItemStack ReservoirCap = new ItemStack(Material.CHAINMAIL_BOOTS);
        ItemMeta ReservoirCapMeta = (ItemMeta) ReservoirCap.getItemMeta();
        ReservoirCapMeta.setDisplayName(ChatColor.GOLD + "RunnersBoots");
        ReservoirCapMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ReservoirCapMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ReservoirCapMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        ReservoirCapMeta.setUnbreakable(true);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add(ChatColor.GOLD + "[Boots]");
        ReservoirCapMeta.setLore(lore);
        ReservoirCap.setItemMeta(ReservoirCapMeta);
        return ReservoirCap.clone();
    }

    public static ItemStack RunnersSword() {
        ItemStack ReservoirCap = new ItemStack(Material.IRON_SWORD);
        ItemMeta ReservoirCapMeta = (ItemMeta) ReservoirCap.getItemMeta();
        ReservoirCapMeta.setDisplayName(ChatColor.GOLD + "RunnersSpade");
        ReservoirCapMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ReservoirCapMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ReservoirCapMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        ReservoirCapMeta.setUnbreakable(true);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add(ChatColor.GOLD + "[Sword]");
        ReservoirCapMeta.setLore(lore);
        ReservoirCap.setItemMeta(ReservoirCapMeta);
        return ReservoirCap.clone();
    }
}
