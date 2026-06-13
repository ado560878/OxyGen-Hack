package com.oxygenclient.module.render;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;

public class XRay extends Module {
    private static boolean xrayEnabled = false;
    
    public XRay() {
        super("XRay", "See ores through walls", Category.RENDER);
    }
    
    public static boolean isXRayEnabled() {
        return xrayEnabled;
    }
    
    @Override
    public void onEnable() {
        xrayEnabled = true;
        if (mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }
    
    @Override
    public void onDisable() {
        xrayEnabled = false;
        if (mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }
}
