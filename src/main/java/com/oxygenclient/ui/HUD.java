package com.oxygenclient.ui;

import com.oxygenclient.OxygenClient;
import com.oxygenclient.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import java.util.List;

public class HUD {
    private final MinecraftClient mc = MinecraftClient.getInstance();

    public void render(DrawContext ctx, float delta) {
        if (mc.player == null) return;
        if (mc.currentScreen != null) return;

        NotificationManager.getInstance().render(ctx);

        List<Module> active = OxygenClient.moduleManager.getModules().stream()
            .filter(m -> m.enabled).toList();

        if (active.isEmpty()) return;

        int x = 3, y = 3;
        for (Module m : active) {
            ctx.drawTextWithShadow(mc.textRenderer, Text.literal("§d● §f" + m.name), x, y, 0xFFFFFF);
            y += 10;
        }
    }
}
