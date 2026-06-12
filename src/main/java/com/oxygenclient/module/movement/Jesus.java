package com.oxygenclient.module.movement;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;

public class Jesus extends Module {
    public Jesus() {
        super("Jesus", "Walk on water", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player != null && mc.player.isTouchingWater()) {
            mc.player.setVelocity(mc.player.getVelocity().x, 0.1, mc.player.getVelocity().z);
        }
    }
}
