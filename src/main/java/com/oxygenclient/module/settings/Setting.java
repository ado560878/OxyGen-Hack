package com.oxygenclient.module.settings;

public class Setting {
    private String name;
    private String description;
    
    public Setting(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public String getName() { return name; }
    public String getDescription() { return description; }
}
