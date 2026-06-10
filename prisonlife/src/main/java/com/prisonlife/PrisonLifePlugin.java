package com.prisonlife;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class PrisonLifePlugin extends JavaPlugin {

    private TeamManager teamManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        teamManager = new TeamManager(this);

        PbCommand pbCommand = new PbCommand(this);
        getCommand("pb").setExecutor(pbCommand);
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this));

        Bukkit.getPluginManager().registerEvents(new GameListener(this), this);
        getLogger().info("PrisonLife aktif!");
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public Component msg(String key) {
        String prefix = getConfig().getString("prefix", "&7[&6PrisonLife&7] ");
        String text = getConfig().getString("messages." + key, key);
        return LegacyComponentSerializer.legacyAmpersand().deserialize(prefix + text);
    }

    public Component msg(String key, String placeholder, String value) {
        String prefix = getConfig().getString("prefix", "&7[&6PrisonLife&7] ");
        String text = getConfig().getString("messages." + key, key).replace(placeholder, value);
        return LegacyComponentSerializer.legacyAmpersand().deserialize(prefix + text);
    }

    public static Component color(String text) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
    }

    public void applySkin(Player player, Team team) {
        String skin = getConfig().getString("skins." + team.configKey());
        if (skin == null || skin.isBlank()) return;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "skin set " + skin + " " + player.getName());
    }

    public Location getSpawn(Team team) {
        String base = "spawns." + team.configKey();
        if (!getConfig().contains(base + ".world")) return null;
        return new Location(
                Bukkit.getWorld(getConfig().getString(base + ".world")),
                getConfig().getDouble(base + ".x"),
                getConfig().getDouble(base + ".y"),
                getConfig().getDouble(base + ".z"),
                (float) getConfig().getDouble(base + ".yaw"),
                (float) getConfig().getDouble(base + ".pitch"));
    }

    public void setSpawn(Team team, Location loc) {
        String base = "spawns." + team.configKey();
        getConfig().set(base + ".world", loc.getWorld().getName());
        getConfig().set(base + ".x", loc.getX());
        getConfig().set(base + ".y", loc.getY());
        getConfig().set(base + ".z", loc.getZ());
        getConfig().set(base + ".yaw", loc.getYaw());
        getConfig().set(base + ".pitch", loc.getPitch());
        saveConfig();
    }
}
