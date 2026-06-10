package com.oxygenclient.bypass;

import net.minecraft.client.MinecraftClient;
import java.util.Random;

public class AntiCheatBypass {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private static final Random random = new Random();
    
    // Tespit edilen anticheat
    public enum AntiCheatType {
        GRIMAC, NCP, AAC, VERUS, VULCAN, MATRIX, SPARTAN, WATCHDOG, 
        POLAR, KARHU, INTVAVE, UNKNOWN
    }
    
    private static AntiCheatType detectedAC = AntiCheatType.UNKNOWN;

    // Rastgele insan benzeri değerler
    public static double getHumanizedAimSpeed() {
        // İnsan gibi düzensiz aim hızı
        return 0.2 + random.nextDouble() * 0.3;
    }

    public static double getHumanizedReach() {
        // Normal reach 3.0, biz 3.2'ye kadar güvenli çıkalım
        return 3.0 + random.nextDouble() * 0.2;
    }

    public static int getHumanizedCPS() {
        // İnsan gibi 8-14 CPS arası
        return 8 + random.nextInt(7);
    }

    public static long getHumanizedDelay() {
        // İnsan reaksiyon süresi 100-250ms
        return 100 + random.nextInt(150);
    }

    public static float getRandomYawOffset() {
        // ±2 derece rastgele sapma (insan hatası)
        return (random.nextFloat() - 0.5f) * 4.0f;
    }

    public static float getRandomPitchOffset() {
        return (random.nextFloat() - 0.5f) * 2.0f;
    }

    public static boolean shouldMiss() {
        // %3 ihtimalle ıskala (insan gibi)
        return random.nextInt(100) < 3;
    }

    public static double getLegitKnockback() {
        // Normal knockback değeri
        return 0.4 + random.nextDouble() * 0.2;
    }

    // Packet sıklığını normalize et
    public static int getPacketDelay() {
        return 50 + random.nextInt(50); // 50-100ms arası
    }

    public static AntiCheatType detectAntiCheat() {
        // Sunucudan gelen packetlere göre AC tespiti
        if (mc.getCurrentServerEntry() != null) {
            String serverIP = mc.getCurrentServerEntry().address.toLowerCase();
            
            if (serverIP.contains("hypixel")) return AntiCheatType.WATCHDOG;
            if (serverIP.contains("minemen")) return AntiCheatType.GRIMAC;
            if (serverIP.contains("pika")) return AntiCheatType.MATRIX;
            if (serverIP.contains("blocksmc")) return AntiCheatType.VERUS;
            if (serverIP.contains("universal")) return AntiCheatType.GRIMAC;
        }
        return AntiCheatType.UNKNOWN;
    }
}
