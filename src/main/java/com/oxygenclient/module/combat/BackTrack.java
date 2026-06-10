package com.oxygenclient.module.combat;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BackTrack extends Module {
    private final Map<UUID, List<PositionRecord>> playerHistory = new ConcurrentHashMap<>();
    private int maxDelay = 1000; // ms cinsinden

    public BackTrack() {
        super("BackTrack", "Geçmiş konumlara vuruş yap - lag exploit", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.world == null || mc.player == null) return;

        // Tüm oyuncuların konumlarını kaydet
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player) continue;

            UUID uuid = player.getUuid();
            List<PositionRecord> history = playerHistory.computeIfAbsent(uuid, k -> new ArrayList<>());

            PositionRecord record = new PositionRecord(
                player.getPos(),
                System.currentTimeMillis()
            );
            history.add(record);

            // Eski kayıtları temizle
            history.removeIf(r -> System.currentTimeMillis() - r.timestamp > maxDelay);
        }
    }

    public Vec3d getBacktrackPosition(PlayerEntity player, int delayMs) {
        List<PositionRecord> history = playerHistory.get(player.getUuid());
        if (history == null || history.isEmpty()) return player.getPos();

        long targetTime = System.currentTimeMillis() - delayMs;
        
        // En yakın zamanlı konumu bul
        PositionRecord best = history.stream()
            .min(Comparator.comparingLong(r -> Math.abs(r.timestamp - targetTime)))
            .orElse(null);

        return best != null ? best.position : player.getPos();
    }

    private record PositionRecord(Vec3d position, long timestamp) {}
}
