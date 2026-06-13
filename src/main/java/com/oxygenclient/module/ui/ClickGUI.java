package com.oxygenclient.ui;

import com.oxygenclient.OxygenClient;
import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import com.oxygenclient.module.settings.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import java.util.*;

public class ClickGUI extends Screen {
    private Category selCat = Category.COMBAT;
    private Module selMod = null;
    private Module bindMod = null;
    private int scrollY = 0;
    private double sliderDrag = -1;

    public ClickGUI() { super(Text.literal("OxyGen")); }

    @Override
    protected void init() {
        super.init();
        int w = Math.max(90, (width - 30) / 4);
        for (int i = 0; i < Category.values().length; i++) {
            Category c = Category.values()[i];
            boolean s = c == selCat;
            addDrawableChild(ButtonWidget.builder(
                Text.literal((s ? "§d§l✦ " : "§7") + c.name),
                b -> { selCat = c; selMod = null; clearChildren(); init(); }
            ).dimensions(10 + i * w, 28, w - 5, 20).build());
        }

        List<Module> mods = OxygenClient.moduleManager.getByCategory(selCat);
        int y = 55 + scrollY;
        for (Module m : mods) {
            String txt;
            if (m == bindMod) txt = "§e... Press key (ESC=Remove)";
            else txt = (m.enabled ? "§a✔ " : "§c✘ ") + "§f" + m.name + 
                       (m.key != 0 ? " §8[" + GLFW.glfwGetKeyName(m.key, 0) + "]" : "");
            
            final Module mod = m;
            addDrawableChild(ButtonWidget.builder(Text.literal(txt), b -> {
                if (b == net.minecraft.client.gui.widget.ButtonWidget.class.cast(b) && 
                    ((ButtonWidget)b).getMessage().getString().contains("...")) return;
                if (Screen.hasShiftDown()) { bindMod = mod; clearChildren(); init(); return; }
                if (Screen.hasControlDown()) { selMod = (selMod == mod) ? null : mod; clearChildren(); init(); return; }
                if (isMouseOverSettings(mx(), my()) && selMod == mod) return;
                mod.toggle();
                clearChildren(); init();
            }).dimensions(10, y, 170, 18).build());
            y += 20;
        }

        if (selMod != null) renderSettings(200, 55);
        if (bindMod != null) {
            addDrawableChild(ButtonWidget.builder(Text.literal("§cCancel"), 
                b -> { bindMod = null; clearChildren(); init(); }
            ).dimensions(200, 55, 60, 18).build());
        }
    }

    private int mx() { return (int)(MinecraftClient.getInstance().mouse.getX() * width / MinecraftClient.getInstance().getWindow().getWidth()); }
    private int my() { return (int)(MinecraftClient.getInstance().mouse.getY() * height / MinecraftClient.getInstance().getWindow().getHeight()); }

    private boolean isMouseOverSettings(int mx, int my) {
        return mx >= 200 && my >= 55;
    }

    private void renderSettings(int x, int y) {
        // Başlık
        drawSettingBg(x, y, 180, 16, 0xFF9932CC);
        MinecraftClient.getInstance().textRenderer.draw(
            Text.literal("§d✧ " + selMod.name), x + 5, y + 4, 0xFFFFFF, false, 
            null, null, 0, 0xFFFFFF);
        y += 18;

        for (Setting s : selMod.settings) {
            if (s instanceof BooleanSetting bs) {
                drawSettingBg(x, y, 180, 16, 0x44000000);
                String t = "§7" + bs.getName() + ": " + (bs.getValue() ? "§a✓" : "§c✗");
                if (isMouseOver(mx(), my(), x, y, 180, 16)) {
                    drawSettingBg(x, y, 180, 16, 0x669932CC);
                    if (hasClicked()) { bs.toggle(); clearChildren(); init(); }
                }
                MinecraftClient.getInstance().textRenderer.draw(
                    Text.literal(t), x + 5, y + 4, 0xFFFFFF, false, null, null, 0, 0xFFFFFF);
                y += 18;
            } else if (s instanceof NumberSetting ns) {
                drawSettingBg(x, y, 180, 30, 0x44000000);
                MinecraftClient.getInstance().textRenderer.draw(
                    Text.literal("§7" + ns.getName() + ": §e" + String.format("%.1f", ns.getValue())), 
                    x + 5, y + 3, 0xFFFFFF, false, null, null, 0, 0xFFFFFF);
                
                // Slider
                int sliderX = x + 5, sliderY = y + 14, sliderW = 170, sliderH = 8;
                drawSettingBg(sliderX, sliderY, sliderW, sliderH, 0xFF333333);
                double ratio = (ns.getValue() - ns.getMin()) / (ns.getMax() - ns.getMin());
                int fillW = (int)(sliderW * ratio);
                drawSettingBg(sliderX, sliderY, fillW, sliderH, 0xFF9932CC);
                
                if (isMouseOver(mx(), my(), sliderX, sliderY, sliderW, sliderH)) {
                    if (org.lwjgl.glfw.GLFW.glfwGetMouseButton(
                        MinecraftClient.getInstance().getWindow().getHandle(), 0) == 1) {
                        double newRatio = (double)(mx() - sliderX) / sliderW;
                        ns.setValue(ns.getMin() + newRatio * (ns.getMax() - ns.getMin()));
                        clearChildren(); init();
                    }
                }
                y += 32;
            } else if (s instanceof ModeSetting ms) {
                drawSettingBg(x, y, 180, 16, 0x44000000);
                String t = "§7" + ms.getName() + ": §b" + ms.getValue();
                if (isMouseOver(mx(), my(), x, y, 180, 16)) {
                    drawSettingBg(x, y, 180, 16, 0x669932CC);
                    if (hasClicked()) { ms.cycle(); clearChildren(); init(); }
                }
                MinecraftClient.getInstance().textRenderer.draw(
                    Text.literal(t), x + 5, y + 4, 0xFFFFFF, false, null, null, 0, 0xFFFFFF);
                y += 18;
            }
        }

        // Keybind
        drawSettingBg(x, y, 180, 16, 0x44000000);
        String kb = "§e⌨ Bind: " + (selMod.key != 0 ? "§a" + GLFW.glfwGetKeyName(selMod.key, 0) : "§7None");
        MinecraftClient.getInstance().textRenderer.draw(
            Text.literal(kb), x + 5, y + 4, 0xFFFFFF, false, null, null, 0, 0xFFFFFF);
    }

    private void drawSettingBg(int x, int y, int w, int h, int color) {
        net.minecraft.client.gui.DrawContext ctx = new net.minecraft.client.gui.DrawContext(
            MinecraftClient.getInstance(), MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers());
        ctx.fill(x, y, x + w, y + h, color);
    }

    private boolean isMouseOver(int mx, int my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }

    private boolean hasClicked() {
        return org.lwjgl.glfw.GLFW.glfwGetMouseButton(
            MinecraftClient.getInstance().getWindow().getHandle(), 0) == 1;
    }

    @Override
    public boolean keyPressed(int key, int scan, int mods) {
        if (bindMod != null) {
            if (key == 256) bindMod.key = 0;
            else bindMod.key = key;
            bindMod = null;
            clearChildren(); init();
            return true;
        }
        if (key == 256) { close(); return true; }
        return super.keyPressed(key, scan, mods);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double h, double v) {
        scrollY += v * 15;
        if (scrollY > 0) scrollY = 0;
        clearChildren(); init();
        return true;
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float d) {
        ctx.fill(0, 0, width, height, 0xDD0D0015);
        // Üst bar
        for (int i = 0; i < 22; i++) {
            float r = i / 22f;
            ctx.fill(5, 22 + i, width - 5, 23 + i,
                (200<<24) | ((int)(255*r+147*(1-r))<<16) | ((int)(105*r+50*(1-r))<<8) | (int)(180*r+139*(1-r)));
        }
        ctx.drawTextWithShadow(textRenderer, "§d✦ §fOxyGen §7v" + OxygenClient.VERSION, 12, 6, 0xFFFFFF);
        ctx.drawTextWithShadow(textRenderer, "§7" + selCat.name + " §8| SHIFT:Bind CTRL:Settings", 10, 34, 0xAAAAAA);
        NotificationManager.getInstance().render(ctx);
        super.render(ctx, mx, my, d);
    }

    @Override public boolean shouldPause() { return false; }
}
