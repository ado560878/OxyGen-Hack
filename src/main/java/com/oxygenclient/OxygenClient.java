package com.oxygenclient;

import com.oxygenclient.module.ModuleManager;
import com.oxygenclient.ui.ClickGUI;
import com.oxygenclient.ui.HUD;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class OxygenClient implements ClientModInitializer {
    public static final String VERSION = "3.0.0";
    public static ModuleManager moduleManager;
    private static KeyBinding guiKey;
    private static HUD hud;

    @Override
    public void onInitializeClient() {
        moduleManager = new ModuleManager();
        hud = new HUD();
        
        guiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.oxygen-client.gui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.oxygen-client"
        ));
        
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            moduleManager.onTick();
            while (guiKey.wasPressed()) {
                MinecraftClient.getInstance().setScreen(new ClickGUI());
            }
        });
        
        HudRenderCallback.EVENT.register((ctx, counter) -> {
            hud.render(ctx, counter.getTickDelta(false));
        });
        
        System.out.println("[OxyGen] v" + VERSION + " | " + moduleManager.getModules().size() + " modules");
    }
}
