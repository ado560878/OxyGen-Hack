package com.oxygenclient.module.movement;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;

public class Fly extends Module {
    public Fly() {
        super("Fly", "Enable flying", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player != null) {
            mc.player.getAbilities().flying = true;
            mc.player.getAbilities().allowFlying = true;
        }
    }

    @Override
    public void onDisable() {
        if (mc.player != null && !mc.player.isCreative()) {
            mc.player.getAbilities().flying = false;
            mc.player.getAbilities().allowFlying = false;
        }
    }
}
