package com.oxygenclient.module.movement;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import com.oxygenclient.module.settings.NumberSetting;

public class Fly extends Module {
    private final NumberSetting flySpeed = new NumberSetting("Speed", "Fly speed", 0.4, 0.1, 2.0, 0.1);

    public Fly() {
        super("Fly", "Enable flying", Category.MOVEMENT);
        addSetting(flySpeed);
    }

    @Override
    public void onTick() {
        if (mc.player != null) {
            mc.player.getAbilities().flying = true;
            mc.player.getAbilities().allowFlying = true;
            mc.player.getAbilities().setFlySpeed((float)flySpeed.getValue() / 10);
        }
    }

    @Override
    public void onDisable() {
        if (mc.player != null && !mc.player.isCreative()) {
            mc.player.getAbilities().flying = false;
            mc.player.getAbilities().allowFlying = false;
            mc.player.getAbilities().setFlySpeed(0.05f);
        }
    }
}
