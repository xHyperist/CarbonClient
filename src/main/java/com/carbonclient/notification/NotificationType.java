package com.carbonclient.notification;

import com.carbonclient.ui.theme.CarbonTheme;

public enum NotificationType {
    INFO(CarbonTheme.SECONDARY),
    SUCCESS(CarbonTheme.ACCENT),
    WARNING(CarbonTheme.WARNING),
    ERROR(CarbonTheme.DANGER);

    private final int accentColor;

    NotificationType(int accentColor) {
        this.accentColor = accentColor;
    }

    public int getAccentColor() {
        return accentColor;
    }
}
