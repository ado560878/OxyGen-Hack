package com.oxygenclient.ui;

import com.oxygenclient.module.Module;
import com.oxygenclient.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class HUD {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    
    public static void render(DrawContext context) {
        int y = 5;
        int x = 5;
        
        for (Module module : ModuleManager.getModules()) {
            if (module.isEnabled()) {
                String text = "§d● §f" + module.getName();
                context.drawTextWithShadow(mc.textRenderer, Text.literal(text), x, y, 0xFFFFFF);
                y += mc.textRenderer.fontHeight + 2;
            }
        }
    }
}
