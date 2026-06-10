package com.prisonlife;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GameListener implements Listener {

    private final PrisonLifePlugin plugin;

    public GameListener(PrisonLifePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGuiClick(InventoryClickEvent event) {
        if (!JoinGui.TITLE.equals(event.getView().title())) return;
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Team selected;
        if (event.getRawSlot() == JoinGui.MAHKUM_SLOT) {
            selected = Team.MAHKUM;
        } else if (event.getRawSlot() == JoinGui.GARDIYAN_SLOT) {
            selected = Team.GARDIYAN;
        } else {
            return;
        }

        TeamManager tm = plugin.getTeamManager();
        if (tm.getTeam(player) == selected) {
            player.sendMessage(plugin.msg("already-in-team"));
            player.closeInventory();
            return;
        }

        tm.setTeam(player, selected);
        player.closeInventory();
        player.getInventory().clear();
        plugin.applySkin(player, selected);

        if (selected == Team.GARDIYAN) {
            Items.giveGuardKit(player);
            player.sendMessage(plugin.msg("joined-gardiyan"));
        } else {
            player.sendMessage(plugin.msg("joined-mahkum"));
        }

        Location spawn = plugin.getSpawn(selected);
        if (spawn != null) {
            player.teleport(spawn);
        } else {
            player.sendMessage(plugin.msg("spawn-not-set"));
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!(event.getRightClicked() instanceof Player target)) return;
        Player guard = event.getPlayer();
        TeamManager tm = plugin.getTeamManager();
        if (tm.getTeam(guard) != Team.GARDIYAN) return;
        if (tm.getTeam(target) != Team.MAHKUM) return;

        ItemStack item = guard.getInventory().getItemInMainHand();
        if (Items.isHandcuffs(item)) {
            event.setCancelled(true);
            handcuff(guard, target);
        } else if (Items.isShockDevice(item)) {
            event.setCancelled(true);
            shock(guard, target);
        }
    }

    private void handcuff(Player guard, Player target) {
        int seconds = plugin.getConfig().getInt("handcuff.freeze-seconds", 3);
        plugin.getTeamManager().handcuff(target, seconds * 1000L);
        target.sendMessage(plugin.msg("handcuffed"));
        guard.sendMessage(plugin.msg("handcuff-used", "%player%", target.getName()));

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (!target.isOnline()) return;
            plugin.getTeamManager().releaseHandcuff(target);
            Location spawn = plugin.getSpawn(Team.MAHKUM);
            if (spawn != null) target.teleport(spawn);
        }, seconds * 20L);
    }

    private void shock(Player guard, Player target) {
        int seconds = plugin.getConfig().getInt("shock.duration-seconds", 5);
        GameMode previous = target.getGameMode();
        target.setGameMode(GameMode.ADVENTURE);
        target.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, seconds * 20, 1));
        target.sendMessage(plugin.msg("shocked"));
        guard.sendMessage(plugin.msg("shock-used", "%player%", target.getName()));

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (!target.isOnline()) return;
            target.setGameMode(previous);
        }, seconds * 20L);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;
        TeamManager tm = plugin.getTeamManager();

        if (tm.isHandcuffed(attacker)) {
            event.setCancelled(true);
            return;
        }

        Team attackerTeam = tm.getTeam(attacker);
        Team victimTeam = tm.getTeam(victim);
        if (attackerTeam != null && attackerTeam == victimTeam) {
            event.setCancelled(true);
            attacker.sendMessage(plugin.msg("friendly-fire"));
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!plugin.getTeamManager().isHandcuffed(event.getPlayer())) return;
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
            event.setTo(from.clone().setDirection(to.getDirection()));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getTeamManager().removePlayer(event.getPlayer().getUniqueId());
    }
}
