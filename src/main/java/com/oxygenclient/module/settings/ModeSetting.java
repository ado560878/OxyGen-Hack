package com.oxygenclient.module.settings;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting {
    private int index;
    private List<String> modes;
    
    public ModeSetting(String name, String description, String defaultValue, String... modes) {
        super(name, description);
        this.modes = Arrays.asList(modes);
        this.index = this.modes.indexOf(defaultValue);
        if (this.index == -1) this.index = 0;
    }
    
    public String getMode() { return modes.get(index); }
    public void setMode(String mode) { 
        int newIndex = modes.indexOf(mode);
        if (newIndex != -1) this.index = newIndex;
    }
    public void nextMode() { index = (index + 1) % modes.size(); }
    public void prevMode() { index = (index - 1 + modes.size()) % modes.size(); }
    public List<String> getModes() { return modes; }
}
