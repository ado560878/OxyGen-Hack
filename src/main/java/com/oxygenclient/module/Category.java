package com.oxygenclient.module;

public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    WORLD("World");
    public String name;
    Category(String n) { this.name = n; }
}
