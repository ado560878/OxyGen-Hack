package com.oxygenclient.bypass;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

public class RotationSpoofer {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private static float serverYaw = 0;
    private static float serverPitch = 0;
    private static boolean init = false;

    public static void update() {
        if (mc.player == null) return;
        if (!init) {
            serverYaw = mc.player.getYaw();
            serverPitch = mc.player.getPitch();
            init = true;
        }
        float diff = MathHelper.wrapDegrees(mc.player.getYaw() - serverYaw);
        serverYaw += MathHelper.clamp(diff, -3f, 3f);
        serverPitch += MathHelper.clamp(mc.player.getPitch() - serverPitch, -2f, 2f);
    }

    public static void setRotation(float yaw, float pitch) {
        serverYaw = yaw;
        serverPitch = pitch;
    }

    public static float getServerYaw() { return serverYaw; }
    public static float getServerPitch() { return serverPitch; }
}
