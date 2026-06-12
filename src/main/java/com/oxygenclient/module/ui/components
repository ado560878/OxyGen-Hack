package com.oxygenclient.ui.components;

import com.oxygenclient.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ModuleButton extends ButtonWidget {
    private final Module module;
    private boolean expanded = false;
    private boolean keyBinding = false;

    public ModuleButton(Module module, int x, int y, int w, int h) {
        super(x, y, w, h, Text.literal(""), btn -> {}, text -> Text.literal(""));
        this.module = module;
    }

    @Override
    public void onPress() {
        if (keyBinding) { keyBinding = false; return; }
        if (net.minecraft.client.gui.screen.Screen.hasShiftDown()) { keyBinding = true; return; }
        module.toggle();
    }

    @Override
    public void renderWidget(DrawContext ctx, int mx, int my, float d) {
        boolean on = module.isEnabled();
        int bg = on ? 0x8800AA00 : 0x88AA0000;
        if (isHovered()) bg = on ? 0xAA00CC00 : 0xAACC0000;
        ctx.fill(getX(), getY(), getX()+width, getY()+height, bg);
        String t = (on ? "§a✔ " : "§c✘ ") + module.getName();
        if (module.getKey() != 0) t += " §7[" + GLFW.glfwGetKeyName(module.getKey(), 0) + "]";
        if (keyBinding) t = "§e> " + module.getName() + " §6[...]";
        ctx.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, t, getX()+4, getY()+6, 0xFFFFFF);
    }

    public boolean handleKey(int keyCode) {
        if (keyBinding) { module.setKey(keyCode); keyBinding = false; return true; }
        return false;
    }
}
