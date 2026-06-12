package com.oxygenclient.ui;

import com.oxygenclient.OxygenClient;
import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import com.oxygenclient.module.settings.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import java.util.List;

public class ClickGUI extends Screen {
    private Category selected = Category.COMBAT;
    private Module selectedModule = null;
    private Module bindingModule = null;

    public ClickGUI() {
        super(Text.literal("§d✧ OxyGen v" + OxygenClient.VERSION));
    }

    @Override
    protected void init() {
        super.init();
        Category[] cats = Category.values();
        int w = Math.max(85, (this.width - 20) / cats.length);
        
        for (int i = 0; i < cats.length; i++) {
            final Category c = cats[i];
            boolean s = c == selected;
            this.addDrawableChild(ButtonWidget.builder(
                Text.literal((s ? "§d§l✦ " : "§7") + c.getName()),
                b -> { selected = c; selectedModule = null; bindingModule = null; clearChildren(); init(); }
            ).dimensions(10 + i * (w - 3), 28, w - 6, 20).build());
        }

        List<Module> mods = OxygenClient.moduleManager.getByCategory(selected);
        int y = 52;
        for (Module mod : mods) {
            boolean on = mod.isEnabled();
            boolean sel = mod == selectedModule;
            boolean binding = mod == bindingModule;
            
            String display;
            if (binding) {
                display = "§e⌨ Press key... (ESC to remove)";
            } else {
                display = (on ? "§a✔ " : "§c✘ ") + (sel ? "§d" : "§f") + mod.getName() +
                    (mod.getKey() != 0 ? " §8[" + getKeyName(mod.getKey()) + "]" : "");
            }
            
            this.addDrawableChild(ButtonWidget.builder(
                Text.literal(display),
                b -> {
                    if (Screen.hasShiftDown() && !binding) {
                        bindingModule = mod;
                        clearChildren();
                        init();
                    } else if (Screen.hasControlDown() && !binding) {
                        selectedModule = (selectedModule == mod) ? null : mod;
                        clearChildren();
                        init();
                    } else if (!binding) {
                        mod.toggle();
                        clearChildren();
                        init();
                    }
                }
            ).dimensions(10, y, 185, 20).build());
            y += 22;
        }

        // Ayarlar paneli
        if (selectedModule != null && bindingModule == null) {
            renderSettings(y + 5);
        }

        // Tuş atama bilgi
        if (bindingModule != null) {
            this.addDrawableChild(ButtonWidget.builder(
                Text.literal("§e§l⌨ Tuş Atama: §f" + bindingModule.getName()),
                b -> {}
            ).dimensions(200, y + 5, 200, 20).build());
            
            this.addDrawableChild(ButtonWidget.builder(
                Text.literal("§7Herhangi bir tuşa basın §8| §cESC §7= Tuşu Sil"),
                b -> {}
            ).dimensions(200, y + 27, 200, 20).build());
            
            this.addDrawableChild(ButtonWidget.builder(
                Text.literal("§cİptal"),
                b -> { bindingModule = null; clearChildren(); init(); }
            ).dimensions(200, y + 49, 80, 20).build());
        }

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("§c✕"),
            b -> close()
        ).dimensions(this.width - 25, 2, 20, 20).build());
    }

    private void renderSettings(int y) {
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("§d§l✧ " + selectedModule.getName() + " Settings"),
            b -> {}
        ).dimensions(200, y, 160, 20).build());
        y += 22;

        for (Setting s : selectedModule.getSettings()) {
            if (s instanceof BooleanSetting bs) {
                this.addDrawableChild(ButtonWidget.builder(
                    Text.literal("§7" + bs.getName() + ": " + (bs.getValue() ? "§a✓" : "§c✗")),
                    b -> { bs.toggle(); clearChildren(); init(); }
                ).dimensions(200, y, 160, 20).build());
            } else if (s instanceof NumberSetting ns) {
                this.addDrawableChild(ButtonWidget.builder(
                    Text.literal("§7" + ns.getName() + ": §e" + String.format("%.1f", ns.getValue())),
                    b -> {
                        ns.setValue(ns.getValue() + ns.getIncrement());
                        if (ns.getValue() > ns.getMax()) ns.setValue(ns.getMin());
                        clearChildren(); init();
                    }
                ).dimensions(200, y, 160, 20).build());
            } else if (s instanceof ModeSetting ms) {
                this.addDrawableChild(ButtonWidget.builder(
                    Text.literal("§7" + ms.getName() + ": §b" + ms.getValue()),
                    b -> { ms.cycle(); clearChildren(); init(); }
                ).dimensions(200, y, 160, 20).build());
            }
            y += 22;
        }

        // Keybind butonu
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("§e⌨ Bind: " + (selectedModule.getKey() != 0 ? 
                "§a" + getKeyName(selectedModule.getKey()) : "§7None") + 
                " §7(SHIFT to rebind, ESC to remove)"),
            b -> {}
        ).dimensions(200, y, 160, 20).build());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (bindingModule != null) {
            if (keyCode == 256) { // ESC tuşu
                bindingModule.setKey(0);
                bindingModule = null;
                clearChildren();
                init();
                return true;
            }
            // Tuşu ata
            bindingModule.setKey(keyCode);
            bindingModule = null;
            clearChildren();
            init();
            return true;
        }
        
        // ESC ile GUI kapat
        if (keyCode == 256) {
            close();
            return true;
        }
        
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private String getKeyName(int key) {
        String name = org.lwjgl.glfw.GLW.glfwGetKeyName(key, 0);
        if (name == null) name = "KEY" + key;
        return name;
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float d) {
        ctx.fill(0, 0, width, height, 0xDD0D0015);
        
        for (int i = 0; i < 24; i++) {
            float r = i / 24f;
            int red = (int)(255 * r + 147 * (1 - r));
            int green = (int)(105 * r + 50 * (1 - r));
            int blue = (int)(180 * r + 139 * (1 - r));
            ctx.fill(5, 22 + i, width - 5, 23 + i, 
                (200 << 24) | (red << 16) | (green << 8) | blue);
        }
        
        ctx.drawTextWithShadow(textRenderer, 
            "§d✧ §fOxyGen §7v" + OxygenClient.VERSION, 12, 6, 0xFFFFFF);
        
        for (int i = 0; i < 2; i++) {
            float r = i / 2f;
            int col = (200 << 24) | ((int)(147*r + 255*(1-r)) << 16) | 
                      ((int)(50*r + 105*(1-r)) << 8) | (int)(139*r + 180*(1-r));
            ctx.fill(5, 47 + i, width - 5, 48 + i, col);
        }
        
        ctx.drawTextWithShadow(textRenderer, 
            "§7" + selected.getName() + " §8• §7SHIFT: Bind  CTRL: Settings  ESC: Close/Unbind", 
            10, 36, 0xAAAAAA);
        
        super.render(ctx, mx, my, d);
    }

    @Override public boolean shouldPause() { return false; }
}
