package com.oxygenclient.module.combat;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import net.minecraft.util.Hand;

public class AutoClicker extends Module {
    private int tick = 0;

    public AutoClicker() {
        super("AutoClicker", "Auto left click", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.currentScreen != null) return;
        tick++;
        if (tick >= 2 && mc.options.attackKey.isPressed()) {
            mc.interactionManager.attackEntity(mc.player, mc.crosshairTarget != null ? mc.crosshairTarget.getEntity() : null);
            mc.player.swingHand(Hand.MAIN_HAND);
            tick = 0;
        }
    }
}
