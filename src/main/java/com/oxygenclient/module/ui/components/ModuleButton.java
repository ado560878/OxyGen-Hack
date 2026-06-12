package com.oxygenclient.ui.components;

import com.oxygenclient.module.Module;
import com.oxygenclient.module.settings.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ModuleButton extends ButtonWidget {
    private final Module module;
    private final Screen parent;
    private boolean expanded = false;
    private int keyBinding = false;

    public ModuleButton(Module module, int x, int y, int w, int h, Screen parent) {
        super(x, y, w, h, Text.literal(""), btn -> {}, text -> Text.literal(""));
        this.module = module;
        this.parent = parent;
    }

    @Override
    public void onPress() {
        if (keyBinding) {
            // Tuş atama modu
            keyBinding = false;
            return;
        }
        if (hasShiftDown()) {
            keyBinding = true;
            return;
        }
        module.toggle();
    }

    @Override
    public void renderWidget(DrawContext ctx, int mouseX, int mouseY, float delta) {
        boolean on = module.isEnabled();
        int color = on ? 0xFF00AA00 : 0xFFAA0000;
        int bgColor = on ? 0x8800AA00 : 0x88AA0000;
        
        if (isHovered()) {
            bgColor = on ? 0xAA00CC00 : 0xAACC0000;
        }

        ctx.fill(getX(), getY(), getX() + width, getY() + height, bgColor);
        
        String text = (on ? "§a✔ " : "§c✘ ") + module.getName();
        if (module.getKey() != 0) {
            text += " §7[" + getKeyName(module.getKey()) + "]";
        }
        if (keyBinding) {
            text = "§e> " + module.getName() + " §6[...]";
        }
        
        ctx.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, 
            text, getX() + 4, getY() + 6, 0xFFFFFF);
        
        // Ayarlar varsa + işareti
        if (!module.getSettings().isEmpty()) {
            ctx.drawTextWithShadow(MinecraftClient.getInstance().textRenderer,
                expanded ? "§7▼" : "§7▶", getX() + width - 12, getY() + 6, 0xAAAAAA);
        }
    }

    private String getKeyName(int key) {
        if (key == 0) return "";
        return org.lwjgl.glfw.GLW.glfwGetKeyName(key, 0);
    }

    public boolean handleKey(int keyCode) {
        if (keyBinding) {
            module.setKey(keyCode);
            keyBinding = false;
            return true;
        }
        return false;
    }
}
