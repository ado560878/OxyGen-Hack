package com.prisonlife;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class PbCommand implements TabExecutor {

    private final PrisonLifePlugin plugin;

    public PbCommand(PrisonLifePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.msg("only-players"));
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("join")) {
            JoinGui.open(player);
            return true;
        }
        player.sendMessage(PrisonLifePlugin.color(
                plugin.getConfig().getString("prefix", "") + "&eKullanim: /pb join"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) return List.of("join");
        return List.of();
    }
}
