package com.oxygenclient.ui;

import net.minecraft.client.gui.DrawContext;
import java.util.*;

public class NotificationManager {
    private static NotificationManager instance;
    private final List<Notification> list = new ArrayList<>();

    public static NotificationManager getInstance() {
        if (instance == null) instance = new NotificationManager();
        return instance;
    }

    public void addNotification(String name, boolean on) {
        list.add(new Notification((on ? "§a✔ " : "§c✘ ") + name + (on ? " ON" : " OFF"), 2000));
        if (list.size() > 6) list.remove(0);
    }

    public void render(DrawContext ctx) {
        list.removeIf(Notification::isExpired);
        int yOff = 0;
        for (Notification n : list) {
            n.render(ctx);
            yOff += 22;
        }
    }
}
