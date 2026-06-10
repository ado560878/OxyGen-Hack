package com.oxygenclient.bypass;

import java.util.Random;

public class DelayNormalizer {
    private static final Random random = new Random();
    private static long lastAttack = 0;
    private static long lastPacket = 0;
    private static int hits = 0;

    public static long getAttackDelay(int cps) {
        long now = System.currentTimeMillis();
        long base = 1000 / cps;
        long offset = (long)(base * (random.nextDouble() - 0.5) * 0.4);
        if (hits > 5) base += 50;
        long delay = base + offset;
        long since = now - lastAttack;
        if (since < delay) return delay - since;
        lastAttack = now;
        hits++;
        return 0;
    }

    public static boolean canSendPacket() {
        long now = System.currentTimeMillis();
        if (now - lastPacket < 50 + random.nextInt(50)) return false;
        lastPacket = now;
        return true;
    }

    public static void resetPattern() {
        hits = random.nextInt(3);
    }
}
