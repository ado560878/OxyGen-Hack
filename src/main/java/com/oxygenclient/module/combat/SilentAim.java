package com.oxygenclient.module.combat;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import com.oxygenclient.module.settings.NumberSetting;

public class SilentAim extends Module {
    private NumberSetting range = new NumberSetting("Range", "Aim range", 6.0, 1.0, 10.0, 0.1);
    
    public SilentAim() {
        super("SilentAim", "Silently aims at entities", Category.COMBAT);
        addSetting(range);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;
        // Silent aim logic here
    }
}
