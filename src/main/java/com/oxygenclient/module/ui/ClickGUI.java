package com.oxygenclient.ui;

import com.oxygenclient.OxygenClient;
import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import com.oxygenclient.ui.components.ModuleButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import java.util.*;

public class ClickGUI extends Screen {
    private Category selectedCategory = Category.COMBAT;
    private final List<ModuleButton> moduleButtons = new ArrayList<>();

    public ClickGUI() {
        super(Text.literal("OxyGen Client v" + OxygenClient.VERSION));
    }

    @Override
    protected void init() {
        super.init();
        moduleButtons.clear();

        Category[] cats = Category.values();
        int catWidth = Math.max(80, (this.width - 20) / cats.length);
        
        for (int i = 0; i < cats.length; i++) {
            final Category cat = cats[i];
            boolean sel = cat == selectedCategory;
            this.addDrawableChild(ButtonWidget.builder(
                Text.literal((sel ? "§6§l" : "§7") + cat.getName()),
                btn -> {
                    selectedCategory = cat;
                    this.clearChildren();
                    this.init();
                }
            ).dimensions(10 + i * (catWidth - 5), 25, catWidth - 10, 20).build());
        }

        List<Module> mods = OxygenClient.moduleManager.getByCategory(selectedCategory);
        int y = 50;
        for (Module mod : mods) {
            ModuleButton mb = new ModuleButton(mod, 10, y, 180, 20, this);
            moduleButtons.add(mb);
            this.addDrawableChild(mb);
            y += 22;
        }

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("§c✕"),
            btn -> close()
        ).dimensions(this.width - 25, 2, 20, 20).build());
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        ctx.fill(0, 0, width, height, 0xCC101020);
        ctx.fill(5, 22, width - 5, 24, 0xFF3333AA);
        ctx.drawTextWithShadow(textRenderer, "§6OxyGen §7v" + OxygenClient.VERSION, 10, 6, 0xFFAA00);
        ctx.fill(5, 44, width - 5, 45, 0xFF222244);
        int count = OxygenClient.moduleManager.getByCategory(selectedCategory).size();
        ctx.drawTextWithShadow(textRenderer, "§7" + selectedCategory.getName() + " §8(" + count + ")", 10, 35, 0xAAAAAA);
        super.render(ctx, mx, my, delta);
    }

    @Override
    public boolean shouldPause() { return false; }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (ModuleButton mb : moduleButtons) {
            if (mb.handleKey(keyCode)) return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
