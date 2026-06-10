package com.prisonlife;

import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class JoinGui {

    public static final Component TITLE = PrisonLifePlugin.color("&7[&6PrisonLife&7] &8Takim Sec");
    public static final int MAHKUM_SLOT = 11;
    public static final int GARDIYAN_SLOT = 15;

    private JoinGui() {
    }

    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, TITLE);

        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.displayName(Component.text(" "));
        filler.setItemMeta(fillerMeta);
        for (int i = 0; i < 27; i++) {
            inv.setItem(i, filler);
        }

        ItemStack mahkum = new ItemStack(Material.IRON_BARS);
        ItemMeta mahkumMeta = mahkum.getItemMeta();
        mahkumMeta.displayName(PrisonLifePlugin.color("&aMahkum Katil"));
        mahkumMeta.lore(List.of(PrisonLifePlugin.color("&7Mahkum takimina katilmak icin tikla!")));
        mahkum.setItemMeta(mahkumMeta);
        inv.setItem(MAHKUM_SLOT, mahkum);

        ItemStack gardiyan = new ItemStack(Material.IRON_HELMET);
        ItemMeta gardiyanMeta = gardiyan.getItemMeta();
        gardiyanMeta.displayName(PrisonLifePlugin.color("&9Gardiyan Katil"));
        gardiyanMeta.lore(List.of(PrisonLifePlugin.color("&7Gardiyan takimina katilmak icin tikla!")));
        gardiyan.setItemMeta(gardiyanMeta);
        inv.setItem(GARDIYAN_SLOT, gardiyan);

        player.openInventory(inv);
    }
}
