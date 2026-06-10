package com.oxygenclient.bypass;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import java.util.Random;

public class RotationSpoofer {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private static final Random random = new Random();
    
    private static float serverYaw, serverPitch;
    private static float realYaw, realPitch;
    private static boolean initialized = false;

    // Server'a gönderilen rotasyonu gizle
    public static void update() {
        if (!initialized && mc.player != null) {
            serverYaw = mc.player.getYaw();
            serverPitch = mc.player.getPitch();
            initialized = true;
        }
        
        if (mc.player == null) return;
        
        realYaw = mc.player.getYaw();
        realPitch = mc.player.getPitch();
        
        // Server rotasyonunu yumuşak geçişle güncelle
        float yawDiff = MathHelper.wrapDegrees(realYaw - serverYaw);
        float pitchDiff = realPitch - serverPitch;
        
        // Max 3 derece/tick değişim
        serverYaw += MathHelper.clamp(yawDiff, -3.0f, 3.0f);
        serverPitch += MathHelper.clamp(pitchDiff, -2.0f, 2.0f);
    }

    public static float getServerYaw() {
        return serverYaw;
    }

    public static float getServerPitch() {
        return serverPitch;
    }

    public static float getRealYaw() {
        return realYaw;
    }

    public static float getRealPitch() {
        return realPitch;
    }

    // KillAura için server rotasyonunu ayarla
    public static void setRotation(float targetYaw, float targetPitch, boolean instant) {
        if (instant) {
            serverYaw = targetYaw;
            serverPitch = targetPitch;
        } else {
            // Yumuşak geçiş
            float yawDiff = MathHelper.wrapDegrees(targetYaw - serverYaw);
            serverYaw += yawDiff * 0.3f;
            serverPitch += (targetPitch - serverPitch) * 0.3f;
        }
    }
}
