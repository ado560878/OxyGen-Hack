package com.oxygenclient.mixin;

import com.oxygenclient.OxygenClient;
import com.oxygenclient.ui.ClickGUI;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    private boolean guiKeyDown = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        MinecraftClient mc = (MinecraftClient) (Object) this;
        if (mc.player == null) return;
        if (OxygenClient.moduleManager == null) return;

        OxygenClient.moduleManager.onTick();

        long w = mc.getWindow().getHandle();

        // Modül tuşları
        OxygenClient.moduleManager.getModules().forEach(m -> {
            if (m.getKey() != 0 && mc.currentScreen == null) {
                if (GLFW.glfwGetKey(w, m.getKey()) == GLFW.GLFW_PRESS) m.toggle();
            }
        });

        // GUI
        boolean p = GLFW.glfwGetKey(w, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
        if (p && !guiKeyDown && mc.currentScreen == null) mc.setScreen(new ClickGUI());
        guiKeyDown = p;
    }
}
