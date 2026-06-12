package com.oxygenclient.mixin;

import com.oxygenclient.OxygenClient;
import com.oxygenclient.ui.ClickGUI;
import com.oxygenclient.ui.HUD;
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
    private final HUD hud = new HUD();

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        MinecraftClient mc = (MinecraftClient) (Object) this;
        
        if (OxygenClient.moduleManager != null) {
            OxygenClient.moduleManager.onTick();
        }
        
        // Modül tuşları
        OxygenClient.moduleManager.getModules().forEach(mod -> {
            if (mod.getKey() != 0 && mc.player != null && mc.currentScreen == null) {
                long w = mc.getWindow().getHandle();
                if (GLFW.glfwGetKey(w, mod.getKey()) == GLFW.GLFW_PRESS) {
                    mod.toggle();
                }
            }
        });
        
        // GUI tuşu
        long win = mc.getWindow().getHandle();
        boolean pressed = GLFW.glfwGetKey(win, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
        if (pressed && !keyDown && mc.currentScreen == null && mc.player != null) {
            mc.setScreen(new ClickGUI());
        }
        keyDown = pressed;
    }
    
    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(DrawContext ctx, float delta, CallbackInfo ci) {
        hud.render(ctx, delta);
    }
}
