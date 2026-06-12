package com.oxygenclient.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class Notification {
    private final String message;
    private long startTime;
    private final long duration;
    private float x;
    private final float targetX;
    private float alpha;
    private boolean removing = false;

    public Notification(String message, float targetX, long duration) {
        this.message = message;
        this.startTime = System.currentTimeMillis();
        this.duration = duration;
        this.targetX = targetX;
        this.x = targetX + 150;
        this.alpha = 1.0f;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - startTime > duration + 500;
    }

    public void render(DrawContext ctx, float delta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        long elapsed = System.currentTimeMillis() - startTime;
        
        // Animasyon
        if (!removing && elapsed < 300) {
            // Giriş animasyonu
            float progress = elapsed / 300f;
            x = targetX + 150 * (1 - progress);
            alpha = progress;
        } else if (!removing && elapsed > duration - 300) {
            // Çıkış animasyonu
            removing = true;
            startTime = System.currentTimeMillis();
            x = targetX;
            alpha = 1.0f;
        } else if (removing) {
            // Çıkış animasyonu devam
            float progress = elapsed / 400f;
            x = targetX + 150 * progress;
            alpha = 1.0f - progress;
            if (alpha < 0) alpha = 0;
        } else {
            // Sabit
            x = targetX;
            alpha = 1.0f;
        }

        int y = 25;
        int width = 140;
        int height = 22;
        
        // Pembe-mor gradient arkaplan
        int color1 = 0xCCFF69B4; // Pembe
        int color2 = 0xCC9932CC; // Mor
        
        ctx.fill((int)x, y, (int)x + width, y + height, color1);
        ctx.fill((int)x, y + height/2, (int)x + width, y + height, color2);
        
        // Kenarlık
        ctx.drawHorizontalLine((int)x, (int)x + width, y, 0xFFFFB6C1);
        ctx.drawHorizontalLine((int)x, (int)x + width, y + height, 0xFF8B008B);
        ctx.drawVerticalLine((int)x, y, y + height, 0xFFFFB6C1);
        ctx.drawVerticalLine((int)x + width, y, y + height, 0xFF8B008B);
        
        // Yazı
        String text = "§d§l✦ §f" + message;
        ctx.drawTextWithShadow(mc.textRenderer, Text.literal(text), 
            (int)x + 5, y + 7, 0xFFFFFF);
    }

    public void startRemoving() {
        if (!removing) {
            removing = true;
            startTime = System.currentTimeMillis();
        }
    }
}
