package com.oxygenclient.module.render;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;

public class XRay extends Module {
    private static XRay instance;
    
    public XRay() {
        super("XRay", "See ores through walls", Category.RENDER);
        instance = this;
    }
    
    public static XRay getInstance() {
        return instance;
    }
    
    public boolean isXRayActive() {
        return this.isEnabled();
    }
    
    @Override
    public void onEnable() {
        if (mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }
    
    @Override
    public void onDisable() {
        if (mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }
}
