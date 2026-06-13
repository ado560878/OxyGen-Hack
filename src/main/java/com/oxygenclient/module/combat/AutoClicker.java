package com.oxygenclient.module.combat;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import com.oxygenclient.module.settings.NumberSetting;

public class AutoClicker extends Module {
    private NumberSetting cps = new NumberSetting("CPS", "Clicks per second", 10, 1, 20, 1);
    private long lastClick = 0;
    
    public AutoClicker() {
        super("AutoClicker", "Automatically clicks", Category.COMBAT);
        addSetting(cps);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null) return;
        
        long now = System.currentTimeMillis();
        int delay = (int) (1000.0 / cps.getValue());
        
        if (now - lastClick >= delay) {
            mc.options.attackKey.setPressed(true);
            lastClick = now;
        }
    }
    
    @Override
    public void onDisable() {
        mc.options.attackKey.setPressed(false);
    }
}
