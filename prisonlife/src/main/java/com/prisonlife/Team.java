package com.prisonlife;

public enum Team {
    MAHKUM("mahkum", "&aMahkum"),
    GARDIYAN("gardiyan", "&9Gardiyan");

    private final String configKey;
    private final String displayName;

    Team(String configKey, String displayName) {
        this.configKey = configKey;
        this.displayName = displayName;
    }

    public String configKey() {
        return configKey;
    }

    public String displayName() {
        return displayName;
    }

    public static Team fromString(String s) {
        for (Team t : values()) {
            if (t.configKey.equalsIgnoreCase(s)) return t;
        }
        return null;
    }
}
