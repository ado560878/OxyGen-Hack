package com.oxygenclient.mixin;

import com.oxygenclient.OxygenClient;
import com.oxygenclient.ui.ClickGUI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    private boolean keyDown = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        MinecraftClient mc = (MinecraftClient) (Object) this;
        
        if (OxygenClient.moduleManager != null && mc.player != null) {
            OxygenClient.moduleManager.onTick();
            
            // Modül tuşları
            long w = mc.getWindow().getHandle();
            OxygenClient.moduleManager.getModules().forEach(mod -> {
                if (mod.getKey() != 0 && mc.currentScreen == null) {
                    if (GLFW.glfwGetKey(w, mod.getKey()) == GLFW.GLFW_PRESS) {
                        mod.toggle();
                    }
                }
            });
            
            // GUI tuşu
            boolean pressed = GLFW.glfwGetKey(w, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
            if (pressed && !keyDown && mc.currentScreen == null) {
                mc.setScreen(new ClickGUI());
            }
            keyDown = pressed;
        }
    }
}
