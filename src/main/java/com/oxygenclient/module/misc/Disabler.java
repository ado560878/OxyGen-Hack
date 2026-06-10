package com.oxygenclient.module.misc;

import com.oxygenclient.bypass.AntiCheatBypass;
import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;

public class Disabler extends Module {
    private int tick = 0;

    public Disabler() { super("Disabler", Category.WORLD); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        tick++;
        
        // GrimAC / NCP transaction bypass
        if (tick % 8 == 0 && AntiCheatBypass.shouldSkipTransaction()) {
            mc.player.setOnGround(tick % 16 != 0);
        }
    }
}
