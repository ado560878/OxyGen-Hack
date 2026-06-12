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
    private Category selected = Category.COMBAT;
    private final List<ModuleButton> buttons = new ArrayList<>();

    public ClickGUI() {
        super(Text.literal("OxyGen v" + OxygenClient.VERSION));
    }

    @Override
    protected void init() {
        super.init();
        buttons.clear();
        Category[] cats = Category.values();
        int w = Math.max(80, (this.width - 20) / cats.length);
        for (int i = 0; i < cats.length; i++) {
            final Category c = cats[i];
            boolean s = c == selected;
            addDrawableChild(ButtonWidget.builder(Text.literal((s?"§6§l":"§7")+c.getName()), b -> { selected=c; clearChildren(); init(); }).dimensions(10+i*(w-5), 25, w-10, 20).build());
        }
        int y = 50;
        for (Module m : OxygenClient.moduleManager.getByCategory(selected)) {
            ModuleButton mb = new ModuleButton(m, 10, y, 180, 20);
            buttons.add(mb);
            addDrawableChild(mb);
            y += 22;
        }
        addDrawableChild(ButtonWidget.builder(Text.literal("§c✕"), b -> close()).dimensions(this.width-25, 2, 20, 20).build());
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float d) {
        ctx.fill(0, 0, width, height, 0xCC101020);
        ctx.fill(5, 22, width-5, 24, 0xFF3333AA);
        ctx.drawTextWithShadow(textRenderer, "§6OxyGen §7v"+OxygenClient.VERSION, 10, 6, 0xFFAA00);
        ctx.fill(5, 44, width-5, 45, 0xFF222244);
        super.render(ctx, mx, my, d);
    }

    @Override public boolean shouldPause() { return false; }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (ModuleButton mb : buttons) if (mb.handleKey(keyCode)) return true;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
