package com.oxygenclient.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;

import java.util.concurrent.ConcurrentLinkedQueue;

public class NotificationManager {
    private static final ConcurrentLinkedQueue<Notification> notifications = new ConcurrentLinkedQueue<>();
    
    public static void add(String message, long duration) {
        notifications.add(new Notification(message, duration));
        while (notifications.size() > 6) {
            notifications.poll();
        }
    }
    
    public static void render(DrawContext context) {
        int y = 30;
        for (Notification notification : notifications.stream().toList()) {
            notification.render(context, y);
            y += 30;
            if (notification.isExpired()) {
                notifications.remove(notification);
            }
        }
    }
}
