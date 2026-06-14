package com.oxygenclient.module.render;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;

public class XRay extends Module {
    private double oldGamma;
    
    public XRay() {
        super("XRay", "See ores through walls (Gamma)", Category.RENDER);
    }
    
    @Override
    public void onEnable() {
        oldGamma = mc.options.getGamma().getValue();
        mc.options.getGamma().setValue(100.0);
    }
    
    @Override
    public void onDisable() {
        mc.options.getGamma().setValue(oldGamma);
    }
}
