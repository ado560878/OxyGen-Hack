package com.oxygenclient.module.render;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;

public class XRay extends Module {
    private static boolean xrayActive = false;
    
    public XRay() {
        super("XRay", "See ores through walls", Category.RENDER);
    }
    
    public static boolean isXRayActive() {
        return xrayActive;
    }
    
    @Override
    public void onEnable() {
        xrayActive = true;
        if (mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }
    
    @Override
    public void onDisable() {
        xrayActive = false;
        if (mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }
}
