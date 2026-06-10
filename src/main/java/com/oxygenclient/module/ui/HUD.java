package com.oxygenclient.ui;

import com.oxygenclient.OxygenClient;
import com.oxygenclient.module.Module;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import java.util.*;

public class HUD {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public HUD() {
        HudRenderCallback.EVENT.register(this::render);
    }

    private void render(DrawContext context, float tickDelta) {
        if (mc.player == null) return;

        List<Module> enabledModules = OxygenClient.moduleManager.getModules().stream()
            .filter(Module::isEnabled)
            .filter(Module::isVisible)
            .toList();

        int y = 5;
        int x = 5;

        // Başlık
        context.drawTextWithShadow(mc.textRenderer,
            Text.literal("§6⚡ OxyGen Client §7v" + OxygenClient.VERSION),
            x, y, 0xFFD700);
        y += 14;

        // Aktif modüller
        for (Module module : enabledModules) {
            String text = module.getName();
            context.drawTextWithShadow(mc.textRenderer,
                Text.literal(text),
                x, y, getModuleColor());
            y += 11;
        }

        // Alt bilgi
        y += 5;
        context.drawTextWithShadow(mc.textRenderer,
            Text.literal("§7RSHIFT: GUI | §7" + enabledModules.size() + " modül aktif"),
            x, y, 0xAAAAAA);
    }

    private int getModuleColor() {
        long time = System.currentTimeMillis() / 200;
        if (time % 2 == 0) return 0x00FF00;
        return 0x00CC00;
    }
}
