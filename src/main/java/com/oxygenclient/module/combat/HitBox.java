package com.oxygenclient.module.combat;

import com.oxygenclient.bypass.HitboxCorrector;
import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;

public class HitBox extends Module {
    private double expansion = 0.15;

    public HitBox() {
        super("HitBox", "Vuruş alanını genişlet", Category.COMBAT);
    }

    public Box getModifiedHitbox(Entity entity, Box original) {
        if (!isEnabled()) return original;
        return HitboxCorrector.getExpandedHitbox(entity, expansion);
    }
}
