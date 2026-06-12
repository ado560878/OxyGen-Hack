package com.oxygenclient.module;

import com.oxygenclient.module.settings.Setting;
import com.oxygenclient.ui.NotificationManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    protected final MinecraftClient mc = MinecraftClient.getInstance();
    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled;
    private int key;
    private final List<Setting> settings = new ArrayList<>();

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
        
        // Chat mesajı
        if (mc.player != null) {
            String msg = enabled ? "§a✔ " + name + " §aenabled" : "§c✘ " + name + " §cdisabled";
            mc.player.sendMessage(Text.literal(msg), true);
        }
        
        // Popup bildirim
        NotificationManager.getInstance().addNotification(
            (enabled ? "§a" : "§c") + name + (enabled ? " enabled" : " disabled")
        );
    }

    protected void addSetting(Setting setting) { settings.add(setting); }
    protected void addSettings(Setting... s) { for (Setting setting : s) addSetting(setting); }

    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean e) { enabled = e; if (e) onEnable(); else onDisable(); }
    public int getKey() { return key; }
    public void setKey(int k) { this.key = k; }
    public List<Setting> getSettings() { return settings; }
}
