package com.oxygenclient.module.combat;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import com.oxygenclient.module.settings.NumberSetting;
import net.minecraft.util.Hand;

public class AutoClicker extends Module {
    private final NumberSetting cps = new NumberSetting("CPS", "Clicks per second", 10, 1, 20, 1);
    private int tick = 0;

    public AutoClicker() {
        super("AutoClicker", "Auto left click", Category.COMBAT);
        addSetting(cps);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.currentScreen != null) return;
        tick++;
        int delay = Math.max(1, 20 / (int)cps.getValue());
        if (tick >= delay && mc.options.attackKey.isPressed()) {
            mc.interactionManager.attackEntity(mc.player, mc.player);
            mc.player.swingHand(Hand.MAIN_HAND);
            tick = 0;
        }
    }
}
