package com.oxygenclient.module;

public enum Category {
    COMBAT("§cCombat"),
    MOVEMENT("§bMovement"),
    RENDER("§dRender"),
    PLAYER("§ePlayer"),
    WORLD("§aWorld");

    private final String name;
    Category(String name) { this.name = name; }
    public String getName() { return name; }
}
