package com.oxygenclient.module.settings;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting {
    private int index;
    private final List<String> modes;
    
    public ModeSetting(String name, String defaultValue, String... modes) {
        super(name);
        this.modes = Arrays.asList(modes);
        this.index = this.modes.indexOf(defaultValue);
    }
    
    public String getMode() { return modes.get(index); }
    public void setMode(String mode) { this.index = modes.indexOf(mode); }
    public void nextMode() { index = (index + 1) % modes.size(); }
    public void prevMode() { index = (index - 1 + modes.size()) % modes.size(); }
    public List<String> getModes() { return modes; }
}
