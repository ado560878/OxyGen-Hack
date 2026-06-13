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

    public void addNotification(String name) {
        list.add(new Notification(name, 2000));
        if (list.size() > 6) list.remove(0);
    }

    public void render(DrawContext ctx) {
        list.removeIf(Notification::isExpired);
        int yOff = 5;
        for (Notification n : list) {
            n.render(ctx, yOff);
            yOff += 22;
        }
    }
}
