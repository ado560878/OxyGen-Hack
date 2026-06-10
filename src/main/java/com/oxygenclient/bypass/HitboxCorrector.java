package com.oxygenclient.bypass;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import java.util.Random;

public class HitboxCorrector {
    private static final Random random = new Random();
    
    // Hitbox'ı genişlet ama aşırıya kaçma
    public static Box getExpandedHitbox(Entity entity, double expansion) {
        Box original = entity.getBoundingBox();
        
        // Max %20 genişletme (anticheatler %30'dan sonra tetiklenir)
        double safeExpansion = Math.min(expansion, 0.2);
        
        // Rastgele hafif kaydırma (insan hatası simülasyonu)
        double randomX = (random.nextDouble() - 0.5) * 0.05;
        double randomY = (random.nextDouble() - 0.5) * 0.05;
        double randomZ = (random.nextDouble() - 0.5) * 0.05;
        
        return original.expand(
            safeExpansion + randomX,
            safeExpansion + randomY,
            safeExpansion + randomZ
        );
    }

    // Vuruş noktasını rastgele kaydır
    public static double[] getRandomHitOffset() {
        return new double[]{
            (random.nextDouble() - 0.5) * 0.3,
            (random.nextDouble() - 0.5) * 0.3,
            (random.nextDouble() - 0.5) * 0.3
        };
    }
}
