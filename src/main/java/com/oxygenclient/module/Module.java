package com.oxygenclient.module;

import com.oxygenclient.module.settings.Setting;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class Module {
    protected String name;
    protected String description;
    protected Category category;
    protected boolean enabled;
    protected int key;
    protected List<Setting> settings = new ArrayList<>();
    
    protected static MinecraftClient mc = MinecraftClient.getInstance();
    
    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }
    
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public int getKey() { return key; }
    public void setKey(int key) { this.key = key; }
    public List<Setting> getSettings() { return settings; }
    
    public void toggle() {
        enabled = !enabled;
        if (enabled) onEnable();
        else onDisable();
    }
    
    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}
    
    public String getDisplayValue() {
        // KillAura için güç değeri göster
        if (this.name.equals("KillAura")) {
            for (Setting s : settings) {
                if (s.getName().equals("Range")) {
                    return ((com.oxygenclient.module.settings.NumberSetting)s).getValue() + "";
                }
            }
        }
        return null;
    }
}
