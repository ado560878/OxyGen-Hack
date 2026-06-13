package com.oxygenclient;

import com.oxygenclient.module.ModuleManager;
import net.fabricmc.api.ModInitializer;

public class OxygenClient implements ModInitializer {
    public static final String NAME = "OxyGen";
    public static final String VERSION = "5.0";
    
    @Override
    public void onInitialize() {
        ModuleManager.init();
        System.out.println("OxyGen Client v5.0 Initialized!");
    }
}
