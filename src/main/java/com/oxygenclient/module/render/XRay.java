// XRay.java - Mixinsiz
package com.oxygenclient.module.render;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;

public class XRay extends Module {
    public XRay() {
        super("XRay", "See ores through walls (Use Gamma)", Category.RENDER);
    }
    
    @Override
    public void onEnable() {
        mc.options.getGamma().setValue(100.0);
    }
    
    @Override
    public void onDisable() {
        mc.options.getGamma().setValue(0.0);
    }
}
