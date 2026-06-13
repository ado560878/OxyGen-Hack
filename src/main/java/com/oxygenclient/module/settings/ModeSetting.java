package com.oxygenclient.module.settings;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting {
    private String value;
    private List<String> modes;
    public ModeSetting(String name, String desc, String def, String... modes) {
        super(name, desc);
        this.value = def;
        this.modes = Arrays.asList(modes);
    }
    public String getValue() { return value; }
    public void setValue(String v) { if (modes.contains(v)) value = v; }
    public void cycle() {
        int i = modes.indexOf(value);
        value = modes.get((i + 1) % modes.size());
    }
    public List<String> getModes() { return modes; }
}
