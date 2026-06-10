package com.prisonlife;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements TabExecutor {

    private final PrisonLifePlugin plugin;

    public SetSpawnCommand(PrisonLifePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.msg("only-players"));
            return true;
        }
        if (!player.hasPermission("prisonlife.setspawn")) {
            player.sendMessage(plugin.msg("no-permission"));
            return true;
        }
        if (args.length != 1) {
            player.sendMessage(PrisonLifePlugin.color(
                    plugin.getConfig().getString("prefix", "") + "&eKullanim: /setspawn <mahkum|gardiyan>"));
            return true;
        }
        Team team = Team.fromString(args[0]);
        if (team == null) {
            player.sendMessage(PrisonLifePlugin.color(
                    plugin.getConfig().getString("prefix", "") + "&cGecersiz takim! (mahkum|gardiyan)"));
            return true;
        }
        plugin.setSpawn(team, player.getLocation());
        player.sendMessage(plugin.msg("spawn-set", "%team%", team.displayName().replaceAll("&.", "")));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) return List.of("mahkum", "gardiyan");
        return List.of();
    }
}
