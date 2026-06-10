package com.oxygenclient.ui;

import com.oxygenclient.OxygenClient;
import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import java.util.*;

public class ClickGUI extends Screen {
    private Category selectedCategory = Category.COMBAT;

    public ClickGUI() {
        super(Text.literal("§6§l⚡ OxyGen Client GUI"));
    }

    @Override
    protected void init() {
        super.init();
        int startX = 20;
        int startY = 30;

        // Kategori butonları
        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) {
            Category cat = categories[i];
            int x = startX + (i * 120);
            
            this.addDrawableChild(ButtonWidget.builder(
                Text.literal(cat.getDisplay()),
                button -> selectedCategory = cat
            ).dimensions(x, startY, 110, 20).build());
        }

        // Modül butonları
        List<Module> modules = OxygenClient.moduleManager.getModulesByCategory(selectedCategory);
        int y = startY + 30;
        
        for (Module module : modules) {
            boolean enabled = module.isEnabled();
            String text = (enabled ? "§a✔ " : "§c✘ ") + module.getName();
            
            this.addDrawableChild(ButtonWidget.builder(
                Text.literal(text),
                button -> module.toggle()
            ).dimensions(startX, y, 200, 20).build());
            
            y += 22;
        }

        // Kapat butonu
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("§cKapat"),
            button -> this.close()
        ).dimensions(this.width / 2 - 40, this.height - 30, 80, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        
        context.drawCenteredTextWithShadow(this.textRenderer,
            Text.literal("§6§l⚡ OXYGEN CLIENT v" + OxygenClient.VERSION + " ⚡"),
            this.width / 2, 10, 0xFFD700);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
