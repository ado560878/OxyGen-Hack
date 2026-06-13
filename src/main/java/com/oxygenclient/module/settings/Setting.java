package com.oxygenclient.module.settings;

public class Setting {
    public String name;
    public String description;
    public boolean visible = true;

    public Setting(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
}
