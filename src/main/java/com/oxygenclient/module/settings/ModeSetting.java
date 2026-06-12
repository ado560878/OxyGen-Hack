package com.oxygenclient.module.settings;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting {
    private String value;
    private final List<String> modes;
    private final String defaultValue;

    public ModeSetting(String name, String description, String defaultValue, String... modes) {
        super(name, description);
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.modes = Arrays.asList(modes);
    }

    public String getValue() { return value; }
    public void setValue(String value) { if (modes.contains(value)) this.value = value; }
    public void cycle() {
        int i = modes.indexOf(value);
        value = modes.get((i + 1) % modes.size());
    }
    public List<String> getModes() { return modes; }
}
