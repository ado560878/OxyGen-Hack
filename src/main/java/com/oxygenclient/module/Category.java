package com.oxygenclient.module;

public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    WORLD("World");

    private final String name;
    Category(String n) { name = n; }
    public String getName() { return name; }
}
