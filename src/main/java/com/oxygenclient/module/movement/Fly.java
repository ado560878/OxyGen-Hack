package com.oxygenclient.module.movement;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import com.oxygenclient.module.settings.NumberSetting;

public class Fly extends Module {
    private NumberSetting flySpeed = new NumberSetting("Speed", 1.0, 0.1, 5.0, 0.1);
    
    public Fly() {
        super("Fly", "Allows you to fly", Category.MOVEMENT);
        addSetting(flySpeed);
    }
    
    @Override
    public void onEnable() {
        if (mc.player != null) {
            mc.player.getAbilities().allowFlying = true;
        }
    }
    
    @Override
    public void onDisable() {
        if (mc.player != null && !mc.player.isCreative()) {
            mc.player.getAbilities().allowFlying = false;
            mc.player.getAbilities().flying = false;
        }
    }
    
    @Override
    public void onTick() {
        if (mc.player == null) return;
        
        mc.player.getAbilities().setFlySpeed(flySpeed.getValue() / 10.0f);
        mc.player.getAbilities().flying = true;
    }
}
