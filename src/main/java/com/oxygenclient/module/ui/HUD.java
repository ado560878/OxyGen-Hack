package com.oxygenclient.ui;

import com.oxygenclient.OxygenClient;
import com.oxygenclient.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import java.util.List;

public class HUD {
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private final NotificationManager notif = NotificationManager.getInstance();

    public void render(DrawContext ctx, float delta) {
        if (mc.player == null) return;

        // Bildirimleri render et
        notif.render(ctx, delta);

        // Aktif modülleri göster
        List<Module> active = OxygenClient.moduleManager.getModules().stream()
            .filter(Module::isEnabled)
            .toList();

        if (active.isEmpty()) return;

        int x = 5;
        int y = 5;
        int maxWidth = 0;
        
        // Maksimum genişliği hesapla
        for (Module m : active) {
            int w = mc.textRenderer.getWidth(m.getName()) + 20;
            if (w > maxWidth) maxWidth = w;
        }
        maxWidth = Math.max(maxWidth, 80);
        
        // Pembe-mor arkaplan
        int height = 12 + active.size() * 10 + 5;
        
        // Gradient arkaplan
        for (int i = 0; i < height; i++) {
            float ratio = (float) i / height;
            int r = (int)(255 * (1 - ratio) + 147 * ratio); // 255 -> 147
            int g = (int)(105 * (1 - ratio) + 50 * ratio);   // 105 -> 50
            int b = (int)(180 * (1 - ratio) + 139 * ratio);  // 180 -> 139
            int color = (128 << 24) | (r << 16) | (g << 8) | b;
            ctx.fill(x - 2, y - 2 + i, x + maxWidth, y - 1 + i, color);
        }
        
        // Kenarlık
        ctx.drawHorizontalLine(x - 2, x + maxWidth, y - 2, 0xFFFF69B4); // Üst pembe
        ctx.drawHorizontalLine(x - 2, x + maxWidth, y - 2 + height, 0xFF9932CC); // Alt mor
        ctx.drawVerticalLine(x - 2, y - 2, y - 2 + height, 0xFFFF69B4);
        ctx.drawVerticalLine(x + maxWidth, y - 2, y - 2 + height, 0xFF9932CC);
        
        // Başlık
        ctx.drawTextWithShadow(mc.textRenderer, 
            Text.literal("§d✦ §fOxyGen §7v" + OxygenClient.VERSION), 
            x, y, 0xFFFFFF);
        y += 12;
        
        // Modüller
        for (Module m : active) {
            ctx.drawTextWithShadow(mc.textRenderer, 
                Text.literal("§d● §f" + m.getName()), 
                x, y, 0xFFFFFF);
            y += 10;
        }
    }
}
