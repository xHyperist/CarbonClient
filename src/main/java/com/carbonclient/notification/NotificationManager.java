package com.carbonclient.notification;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class NotificationManager {

    public static final long DEFAULT_DURATION_MS = 3000L;
    private static final int MAX_QUEUED_NOTIFICATIONS = 12;

    private final List<Notification> notifications =
        new ArrayList<Notification>();

    public synchronized void show(
        String title,
        String message,
        NotificationType type
    ) {
        show(title, message, type, DEFAULT_DURATION_MS);
    }

    public synchronized void show(
        String title,
        String message,
        NotificationType type,
        long durationMs
    ) {
        notifications.add(
            new Notification(title, message, type, durationMs)
        );

        while (notifications.size() > MAX_QUEUED_NOTIFICATIONS) {
            notifications.remove(0);
        }
    }

    public void info(String title, String message) {
        show(title, message, NotificationType.INFO);
    }

    public void success(String title, String message) {
        show(title, message, NotificationType.SUCCESS);
    }

    public void warning(String title, String message) {
        show(title, message, NotificationType.WARNING, 4000L);
    }

    public void error(String title, String message) {
        show(title, message, NotificationType.ERROR, 5000L);
    }

    public synchronized List<Notification> getActiveNotifications(long now) {
        Iterator<Notification> iterator = notifications.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isExpired(now)) {
                iterator.remove();
            }
        }

        return new ArrayList<Notification>(notifications);
    }

    public synchronized void clear() {
        notifications.clear();
    }
}
