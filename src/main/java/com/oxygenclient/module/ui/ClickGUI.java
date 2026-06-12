package com.oxygenclient.ui;

import com.oxygenclient.OxygenClient;
import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import com.oxygenclient.module.settings.*;
import com.oxygenclient.ui.components.ModuleButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import java.util.*;

public class ClickGUI extends Screen {
    private Category selectedCategory = Category.COMBAT;
    private Module selectedModule = null;
    private final List<ModuleButton> moduleButtons = new ArrayList<>();

    public ClickGUI() {
        super(Text.literal("§6§l⚡ " + OxygenClient.NAME + " v" + OxygenClient.VERSION));
    }

    @Override
    protected void init() {
        super.init();
        moduleButtons.clear();
        
        int centerX = this.width / 2;
        
        // Kategori butonları
        Category[] cats = Category.values();
        int catWidth = this.width / cats.length;
        for (int i = 0; i < cats.length; i++) {
            final Category cat = cats[i];
            boolean sel = cat == selectedCategory;
            this.addDrawableChild(ButtonWidget.builder(
                Text.literal((sel ? "§6§l" : "§7") + cat.getName()),
                btn -> {
                    selectedCategory = cat;
                    selectedModule = null;
                    clearAndInit();
                }
            ).dimensions(10 + i * (catWidth - 5), 25, catWidth - 10, 20).build());
        }

        // Modül listesi
        List<Module> mods = OxygenClient.moduleManager.getByCategory(selectedCategory);
        int y = 50;
        for (Module mod : mods) {
            ModuleButton mb = new ModuleButton(mod, 10, y, 180, 20, this);
            moduleButtons.add(mb);
            this.addDrawableChild(mb);
            y += 22;
        }

        // Close
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("§c✕"),
            btn -> close()
        ).dimensions(this.width - 25, 2, 20, 20).build());
    }

    private void clearAndInit() {
        clearChildren();
        init();
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        // Arkaplan
        ctx.fill(0, 0, width, height, 0xCC101020);
        
        // Panel
        ctx.fill(5, 22, width - 5, 24, 0xFF3333AA);
        
        // Başlık
        ctx.drawTextWithShadow(textRenderer, "§6§l⚡ " + OxygenClient.NAME + " §7v" + OxygenClient.VERSION, 10, 6, 0xFFAA00);
        
        // Kategori çizgisi
        ctx.fill(5, 44, width - 5, 45, 0xFF222244);
        
        // Modül sayısı
        int count = OxygenClient.moduleManager.getByCategory(selectedCategory).size();
        ctx.drawTextWithShadow(textRenderer, "§7" + selectedCategory.getName() + " §8(" + count + ")", 10, 35, 0xAAAAAA);
        
        super.render(ctx, mx, my, delta);
    }

    @Override
    public boolean shouldPause() { return false; }
}
