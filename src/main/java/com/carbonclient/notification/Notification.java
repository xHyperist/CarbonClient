package com.carbonclient.notification;

public final class Notification {

    private final String title;
    private final String message;
    private final NotificationType type;
    private final long durationMs;
    private final long createdAt;

    public Notification(
        String title,
        String message,
        NotificationType type,
        long durationMs
    ) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Notification title cannot be empty.");
        }
        if (message == null || type == null) {
            throw new IllegalArgumentException(
                "Notification message and type cannot be null."
            );
        }

        this.title = title;
        this.message = message;
        this.type = type;
        this.durationMs = Math.max(750L, durationMs);
        this.createdAt = System.currentTimeMillis();
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public NotificationType getType() {
        return type;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getAge(long now) {
        return Math.max(0L, now - createdAt);
    }

    public boolean isExpired(long now) {
        return getAge(now) >= durationMs;
    }
}
