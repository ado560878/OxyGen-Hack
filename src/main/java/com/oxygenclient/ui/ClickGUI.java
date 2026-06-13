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
                if (Screen.hasShiftDown()) { bindMod = mod; clearChildren(); init(); return; }
                if (Screen.hasControlDown()) { selMod = (selMod == mod) ? null : mod; clearChildren(); init(); return; }
                mod.toggle();
                clearChildren(); init();
            }).dimensions(10, y, 170, 18).build());
            y += 20;
        }

        if (selMod != null) {
            int sx = 200, sy = 55;
            addDrawableChild(ButtonWidget.builder(
                Text.literal("§d✧ " + selMod.name), b -> {}
            ).dimensions(sx, sy, 150, 16).build());
            sy += 18;

            for (Setting s : selMod.settings) {
                if (s instanceof BooleanSetting bs) {
                    addDrawableChild(ButtonWidget.builder(
                        Text.literal("§7" + bs.getName() + ": " + (bs.getValue() ? "§a✓" : "§c✗")),
                        b -> { bs.toggle(); clearChildren(); init(); }
                    ).dimensions(sx, sy, 150, 16).build());
                    sy += 18;
                } else if (s instanceof NumberSetting ns) {
                    addDrawableChild(ButtonWidget.builder(
                        Text.literal("§7" + ns.getName() + ": §e" + String.format("%.1f", ns.getValue())),
                        b -> { 
                            ns.setValue(ns.getValue() + ns.getIncrement());
                            if (ns.getValue() > ns.getMax()) ns.setValue(ns.getMin());
                            clearChildren(); init(); 
                        }
                    ).dimensions(sx, sy, 150, 16).build());
                    sy += 18;
                } else if (s instanceof ModeSetting ms) {
                    addDrawableChild(ButtonWidget.builder(
                        Text.literal("§7" + ms.getName() + ": §b" + ms.getValue()),
                        b -> { ms.cycle(); clearChildren(); init(); }
                    ).dimensions(sx, sy, 150, 16).build());
                    sy += 18;
                }
            }

            addDrawableChild(ButtonWidget.builder(
                Text.literal("§e⌨ Bind: " + (selMod.key != 0 ? "§a" + GLFW.glfwGetKeyName(selMod.key, 0) : "§7None")),
                b -> {}
            ).dimensions(sx, sy, 150, 16).build());
        }

        if (bindMod != null) {
            addDrawableChild(ButtonWidget.builder(
                Text.literal("§cCancel"), 
                b -> { bindMod = null; clearChildren(); init(); }
            ).dimensions(200, 55, 60, 16).build());
        }
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
        scrollY += (int)(v * 15);
        if (scrollY > 0) scrollY = 0;
        clearChildren(); init();
        return true;
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float d) {
        ctx.fill(0, 0, width, height, 0xDD0D0015);
        for (int i = 0; i < 22; i++) {
            float r = i / 22f;
            ctx.fill(5, 22 + i, width - 5, 23 + i,
                (200<<24) | ((int)(255*r+147*(1-r))<<16) | ((int)(105*r+50*(1-r))<<8) | (int)(180*r+139*(1-r)));
        }
        ctx.drawTextWithShadow(textRenderer, "§d✦ §fOxyGen §7v" + OxygenClient.VERSION, 12, 6, 0xFFFFFF);
        ctx.drawTextWithShadow(textRenderer, "§7" + selCat.name + " §8| SHIFT:Bind CTRL:Settings ESC:Close", 10, 34, 0xAAAAAA);
        super.render(ctx, mx, my, d);
    }

    @Override public boolean shouldPause() { return false; }
}
