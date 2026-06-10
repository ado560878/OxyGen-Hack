package com.oxygenclient.bypass;

import java.util.Random;

public class AntiCheatBypass {
    private static final Random random = new Random();

    public static double getHumanizedReach() {
        return 3.0 + random.nextDouble() * 0.2;
    }

    public static long getHumanizedDelay() {
        return 100 + random.nextInt(150);
    }

    public static float getRandomYawOffset() {
        return (random.nextFloat() - 0.5f) * 4.0f;
    }

    public static float getRandomPitchOffset() {
        return (random.nextFloat() - 0.5f) * 2.0f;
    }

    public static boolean shouldMiss() {
        return random.nextInt(100) < 3;
    }

    public static boolean shouldSkipTransaction() {
        return random.nextInt(8) == 0;
    }

    public static int getHumanizedCPS() {
        return 8 + random.nextInt(7);
    }
}
