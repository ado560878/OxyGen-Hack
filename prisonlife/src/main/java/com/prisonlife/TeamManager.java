package com.prisonlife;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;

public class TeamManager {

    private final PrisonLifePlugin plugin;
    private final Map<UUID, Team> teams = new HashMap<>();
    private final Map<UUID, Long> handcuffedUntil = new HashMap<>();

    public TeamManager(PrisonLifePlugin plugin) {
        this.plugin = plugin;
    }

    public Team getTeam(Player player) {
        return teams.get(player.getUniqueId());
    }

    public void setTeam(Player player, Team team) {
        teams.put(player.getUniqueId(), team);
    }

    public void removePlayer(UUID uuid) {
        teams.remove(uuid);
        handcuffedUntil.remove(uuid);
    }

    public boolean isHandcuffed(Player player) {
        Long until = handcuffedUntil.get(player.getUniqueId());
        if (until == null) return false;
        if (System.currentTimeMillis() >= until) {
            handcuffedUntil.remove(player.getUniqueId());
            return false;
        }
        return true;
    }

    public void handcuff(Player player, long durationMillis) {
        handcuffedUntil.put(player.getUniqueId(), System.currentTimeMillis() + durationMillis);
    }

    public void releaseHandcuff(Player player) {
        handcuffedUntil.remove(player.getUniqueId());
    }
}
