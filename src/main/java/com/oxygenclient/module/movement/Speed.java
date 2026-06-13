package com.oxygenclient.module.movement;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import com.oxygenclient.module.settings.NumberSetting;

public class Speed extends Module {
    private NumberSetting speed = new NumberSetting("Speed", "Movement speed multiplier", 0.3, 0.1, 1.0, 0.05);
    
    public Speed() {
        super("Speed", "Increases movement speed", Category.MOVEMENT);
        addSetting(speed);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null) return;
        
        if (mc.player.isOnGround()) {
            mc.player.setVelocity(mc.player.getVelocity().x * speed.getValue(), 
                                  mc.player.getVelocity().y, 
                                  mc.player.getVelocity().z * speed.getValue());
        }
    }
}
