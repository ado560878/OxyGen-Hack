package com.oxygenclient.module.combat;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;

public class Criticals extends Module {
    public Criticals() {
        super("Criticals", "Always critical hit", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player != null && mc.player.isOnGround() && mc.options.attackKey.isPressed()) {
            mc.player.jump();
        }
    }
}
