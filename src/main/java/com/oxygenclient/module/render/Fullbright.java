package com.oxygenclient.module.render;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;

public class Fullbright extends Module {
    private double old;
    public Fullbright() {
        super("Fullbright", "Full brightness", Category.RENDER);
    }
    @Override public void onEnable() { old = mc.options.getGamma().getValue(); mc.options.getGamma().setValue(16.0); }
    @Override public void onDisable() { mc.options.getGamma().setValue(old); }
}
