package com.oxygenclient.mixin;

import com.oxygenclient.module.ModuleManager;
import com.oxygenclient.ui.ClickGUI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    
    private static final int OPEN_GUI_KEY = GLFW.GLFW_KEY_RIGHT_SHIFT;
    private boolean lastKeyState = false;
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ModuleManager.onTick();
        
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen == null) {
            boolean currentKeyState = client.options.keyAttack.isPressed();
            // Right Shift kontrolü için
            long window = client.getWindow().getHandle();
            boolean rightShiftPressed = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
            
            if (rightShiftPressed && !lastKeyState) {
                client.setScreen(ClickGUI.INSTANCE);
            }
            lastKeyState = rightShiftPressed;
        }
    }
}
