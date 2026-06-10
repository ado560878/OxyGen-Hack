package com.oxygenclient.module.movement;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;

public class NoFall extends Module {
    public NoFall() {
        super("NoFall", "No fall damage", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player != null && mc.player.fallDistance > 2.5f) {
            mc.player.fallDistance = 0;
        }
    }
}
