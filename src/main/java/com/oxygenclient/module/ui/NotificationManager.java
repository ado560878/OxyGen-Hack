package com.oxygenclient.ui;

import net.minecraft.client.gui.DrawContext;
import java.util.ArrayList;
import java.util.List;

public class NotificationManager {
    private static NotificationManager instance;
    private final List<Notification> notifications = new ArrayList<>();
    private static final int MAX_NOTIFICATIONS = 5;

    public static NotificationManager getInstance() {
        if (instance == null) instance = new NotificationManager();
        return instance;
    }

    public void addNotification(String message) {
        notifications.add(new Notification(message, 10, 2000));
        if (notifications.size() > MAX_NOTIFICATIONS) {
            Notification oldest = notifications.get(0);
            oldest.startRemoving();
        }
    }

    public void render(DrawContext ctx, float delta) {
        notifications.removeIf(n -> {
            if (n.isExpired()) return true;
            n.render(ctx, delta);
            return false;
        });
        
        // Her bildirimi aşağı kaydır
        int yOffset = 0;
        for (Notification n : notifications) {
            yOffset += 25;
        }
    }

    public void clear() {
        notifications.clear();
    }
}
