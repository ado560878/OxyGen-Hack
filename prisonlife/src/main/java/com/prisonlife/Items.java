package com.prisonlife;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class Items {

    private Items() {
    }

    public static ItemStack guardSword() {
        ItemStack item = new ItemStack(Material.WOODEN_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(PrisonLifePlugin.color("&6Gardiyan Kilici"));
        meta.lore(List.of(PrisonLifePlugin.color("&7Mahkumlara karsi kullan!")));
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack handcuffs() {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(PrisonLifePlugin.color("&7Kelepce"));
        meta.lore(List.of(PrisonLifePlugin.color("&7Bir mahkuma sag tikla, kelepcele!")));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack shockDevice() {
        ItemStack item = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(PrisonLifePlugin.color("&eSok Cihazi"));
        meta.lore(List.of(PrisonLifePlugin.color("&7Bir mahkuma sag tikla, sok uygula!")));
        item.setItemMeta(meta);
        return item;
    }

    public static void giveGuardKit(Player player) {
        player.getInventory().addItem(guardSword(), handcuffs(), shockDevice());
    }

    public static boolean isHandcuffs(ItemStack item) {
        return matches(item, Material.STICK, "&7Kelepce");
    }

    public static boolean isShockDevice(ItemStack item) {
        return matches(item, Material.BLAZE_ROD, "&eSok Cihazi");
    }

    private static boolean matches(ItemStack item, Material material, String name) {
        if (item == null || item.getType() != material || !item.hasItemMeta()) return false;
        return PrisonLifePlugin.color(name).equals(item.getItemMeta().displayName());
    }
}
