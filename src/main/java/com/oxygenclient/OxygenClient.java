package com.oxygenclient;

import com.oxygenclient.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;

public class OxygenClient implements ClientModInitializer {
    public static final String NAME = "OxyGen";
    public static final String VERSION = "5.0";
    public static ModuleManager moduleManager;

    @Override
    public void onInitializeClient() {
        moduleManager = new ModuleManager();
        System.out.println("[" + NAME + "] v" + VERSION + " loaded!");
    }
}
