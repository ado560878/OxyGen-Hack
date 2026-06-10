package com.oxygenclient.bypass;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;

public class HitboxCorrector {
    public static Box expand(Entity entity) {
        if (entity == null) return null;
        return entity.getBoundingBox().expand(0.12);
    }
}
