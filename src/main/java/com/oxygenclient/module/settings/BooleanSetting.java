package com.oxygenclient.module.settings;

public class BooleanSetting extends Setting {
    private boolean value;
    public BooleanSetting(String name, String desc, boolean def) {
        super(name, desc);
        this.value = def;
    }
    public boolean getValue() { return value; }
    public void setValue(boolean v) { this.value = v; }
    public void toggle() { value = !value; }
}
