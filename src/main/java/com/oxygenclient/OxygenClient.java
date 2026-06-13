package com.oxygenclient;

import com.oxygenclient.module.ModuleManager;
import com.oxygenclient.ui.ClickGUI;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class OxygenClient implements ModInitializer {
    public static final String NAME = "OxyGen";
    public static final String VERSION = "5.0";
    
    public static KeyBinding openClickGui;
    
    @Override
    public void onInitialize() {
        ModuleManager.init();
        
        openClickGui = new KeyBinding(
            "key.oxygen.openclickgui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "OxyGen Client"
        );
        
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openClickGui.wasPressed()) {
                client.setScreen(ClickGUI.INSTANCE);
            }
        });
    }
}
