package com.oxygenclient.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class Notification {
    public String text;
    public long start, duration;
    public float x, alpha;
    public boolean removing;

    public Notification(String text, long duration) {
        this.text = text;
        this.duration = duration;
        this.start = System.currentTimeMillis();
        this.x = 160;
        this.alpha = 1;
    }

    public boolean isExpired() { 
        return System.currentTimeMillis() - start > duration + 600; 
    }

    public void render(DrawContext ctx, int yOffset) {
        long elapsed = System.currentTimeMillis() - start;
        float targetX = 10;
        
        if (!removing && elapsed < 400) {
            x = targetX + 160 * (1 - elapsed / 400f);
            alpha = elapsed / 400f;
        } else if (!removing && elapsed > duration - 400) {
            removing = true;
            start = System.currentTimeMillis();
        } else if (removing) {
            float p = elapsed / 500f;
            x = targetX + 160 * p;
            alpha = 1 - p;
        } else {
            x = targetX;
            alpha = 1;
        }

        int ix = (int)x, iy = yOffset;
        int w = 140, h = 18;
        
        ctx.fill(ix, iy, ix + w, iy + h, 0xCC9932CC);
        ctx.fill(ix, iy, ix + w, iy + 2, 0xFFFF69B4);
        ctx.drawTextWithShadow(MinecraftClient.getInstance().textRenderer,
            Text.literal(text), ix + 5, iy + 5, 0xFFFFFF);
    }
}
