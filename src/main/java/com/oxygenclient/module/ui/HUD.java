package com.oxygenclient.ui;

import com.oxygenclient.OxygenClient;
import com.oxygenclient.module.Module;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import java.util.List;

public class HUD {
    public HUD() {
        HudRenderCallback.EVENT.register(this::render);
    }

    private void render(DrawContext ctx, float delta) {
        var mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        List<Module> mods = OxygenClient.moduleManager.getModules().stream()
            .filter(Module::isEnabled).toList();

        int y = 5;
        ctx.drawTextWithShadow(mc.textRenderer, Text.literal("§6OxyGen v" + OxygenClient.VERSION), 5, y, 0xFFD700);
        y += 12;
        for (Module m : mods) {
            ctx.drawTextWithShadow(mc.textRenderer, Text.literal("§a" + m.getName()), 5, y, 0x00FF00);
            y += 10;
        }
    }
}
