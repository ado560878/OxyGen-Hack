package com.oxygenclient.module.misc;

import com.oxygenclient.bypass.AntiCheatBypass;
import com.oxygenclient.bypass.DelayNormalizer;
import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import java.util.Random;

public class Disabler extends Module {
    private final Random random = new Random();
    private int tickCounter = 0;

    public Disabler() {
        super("Disabler", "Anticheat check'lerini devre dışı bırakır", Category.WORLD);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.getNetworkHandler() == null) return;
        
        tickCounter++;
        AntiCheatBypass.AntiCheatType ac = AntiCheatBypass.detectAntiCheat();

        switch (ac) {
            case GRIMAC -> grimacBypass();
            case NCP -> ncpBypass();
            case AAC -> aacBypass();
            case VERUS -> verusBypass();
            case WATCHDOG -> watchdogBypass();
        }
    }

    private void grimacBypass() {
        // GrimAC transaction bypass
        if (DelayNormalizer.shouldSkipTransaction()) {
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(
                !mc.player.isOnGround()
            ));
        }
    }

    private void ncpBypass() {
        // NCP ground spoof
        if (tickCounter % 4 == 0) {
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(
                tickCounter % 8 != 0
            ));
        }
    }

    private void aacBypass() {
        // AAC rotation bypass
        if (tickCounter % 6 == 0) {
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(
                mc.player.getYaw() + (random.nextFloat() - 0.5f) * 10,
                mc.player.getPitch(),
                mc.player.isOnGround()
            ));
        }
    }

    private void verusBypass() {
        // Verus - düzensiz packet gönderimi
        if (random.nextInt(4) == 0) {
            mc.getNetworkHandler().sendPacket(new TeleportConfirmC2SPacket(
                tickCounter + random.nextInt(100)
            ));
        }
    }

    private void watchdogBypass() {
        // Watchdog - combat check bypass
        if (tickCounter % 10 == 0) {
            mc.player.setSprinting(false);
        } else if (tickCounter % 10 == 5) {
            mc.player.setSprinting(true);
        }
    }

    @Override
    public void onDisable() {
        tickCounter = 0;
    }
}
