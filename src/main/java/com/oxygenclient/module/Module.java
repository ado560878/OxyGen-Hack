package com.oxygenclient.module;

import com.oxygenclient.module.settings.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    protected final MinecraftClient mc = MinecraftClient.getInstance();
    public String name, description;
    public Category category;
    public boolean enabled;
    public int key;
    public List<Setting> settings = new ArrayList<>();

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = false;
        this.key = 0;
    }

    public void toggle() {
        enabled = !enabled;
        if (enabled) onEnable(); else onDisable();
        if (mc.player != null) {
            mc.player.sendMessage(Text.literal((enabled ? "§a✔ " : "§c✘ ") + name + (enabled ? " ON" : " OFF")), true);
        }
    }

    public void addSetting(Setting s) { settings.add(s); }
    public void addSettings(Setting... s) { for (Setting ss : s) addSetting(ss); }
    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}

    public String getName() { return name; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public int getKey() { return key; }
    public void setKey(int k) { this.key = k; }
    public List<Setting> getSettings() { return settings; }
}
