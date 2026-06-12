package com.oxygenclient.ui;

import com.oxygenclient.OxygenClient;
import com.oxygenclient.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import java.util.*;
import java.util.stream.Collectors;

public class HUD {
    private final MinecraftClient mc = MinecraftClient.getInstance();

    public void render(DrawContext ctx, float delta) {
        if (mc.player == null) return;

        List<Module> active = OxygenClient.moduleManager.getModules().stream()
            .filter(Module::isEnabled)
            .collect(Collectors.toList());

        if (active.isEmpty()) return;

        int x = 5;
        int y = 5;
        
        // Arkaplan
        ctx.fill(x - 2, y - 2, x + 120, y + 10 + active.size() * 10, 0x88000000);
        
        // Başlık
        ctx.drawTextWithShadow(mc.textRenderer, Text.literal("§6⚡ OxyGen §7v" + OxygenClient.VERSION), x, y, 0xFFD700);
        y += 12;
        
        // Aktif modüller
        for (Module m : active) {
            ctx.drawTextWithShadow(mc.textRenderer, Text.literal("§a● §f" + m.getName()), x, y, 0xFFFFFF);
            y += 10;
        }
    }
}
