package com.oxygenclient.module.movement;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import com.oxygenclient.module.settings.NumberSetting;

public class Speed extends Module {
    private final NumberSetting speed = new NumberSetting("Speed", "Speed multiplier", 0.3, 0.1, 1.0, 0.05);

    public Speed() {
        super("Speed", "Move faster", Category.MOVEMENT);
        addSetting(speed);
    }

    @Override
    public void onTick() {
        if (mc.player == null || !mc.player.isOnGround() || mc.player.forwardSpeed <= 0) return;
        double s = speed.getValue();
        double y = Math.toRadians(mc.player.getYaw());
        mc.player.setVelocity(-Math.sin(y)*s, mc.player.getVelocity().y, Math.cos(y)*s);
    }
}
