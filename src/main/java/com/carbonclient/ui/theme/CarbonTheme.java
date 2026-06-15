package com.carbonclient.ui.theme;

public final class CarbonTheme {

    // Core brand colors
    public static final int OVERLAY = 0xC0080B14;
    public static final int BACKGROUND = 0xFF0D1220;
    public static final int PANEL = 0xFF121A2E;
    public static final int PRIMARY = 0xFFFF4FA3;
    public static final int SECONDARY = 0xFF4DA6FF;
    public static final int ACCENT = 0xFF6EE7FF;
    public static final int TEXT = 0xFFFFFFFF;
    public static final int MUTED_TEXT = 0xFFA8B0C2;
    public static final int DANGER = 0xFFFF4F6D;
    public static final int WARNING = 0xFFFFB84D;

    // UI surfaces and interaction states
    public static final int CARD = 0xE0182238;
    public static final int CARD_HOVER = 0xF01C2942;
    public static final int BORDER = 0xFF273653;
    public static final int BORDER_HOVER = 0xFF3B5278;
    public static final int BUTTON = 0xFF202D49;
    public static final int BUTTON_HOVER = 0xFF2A3B5E;
    public static final int ROW = 0xD9162034;
    public static final int ROW_HOVER = 0xEF1B2942;
    public static final int TRACK = 0xFF182238;
    public static final int DIVIDER = 0xFF202D49;

    // Layout tokens
    public static final int MENU_WIDTH = 500;
    public static final int MENU_HEIGHT = 410;
    public static final int HEADER_HEIGHT = 48;
    public static final int FOOTER_HEIGHT = 28;
    public static final int CONTENT_PADDING = 16;
    public static final int GRID_COLUMNS = 3;
    public static final int CARD_GAP = 10;
    public static final int CARD_HEIGHT = 126;
    public static final int BUTTON_HEIGHT = 20;
    public static final int SETTING_ROW_HEIGHT = 32;
    public static final int CONTROL_WIDTH = 150;

    // Spacing tokens
    public static final int SPACE_2 = 2;
    public static final int SPACE_4 = 4;
    public static final int SPACE_6 = 6;
    public static final int SPACE_8 = 8;
    public static final int SPACE_10 = 10;
    public static final int SPACE_12 = 12;
    public static final int SPACE_16 = 16;
    public static final int SPACE_20 = 20;
    public static final int SPACE_24 = 24;

    // Shape tokens. Rect rendering is used in 1.8.9; these reserve the API.
    public static final int RADIUS_SMALL = 2;
    public static final int RADIUS_MEDIUM = 4;
    public static final int RADIUS_LARGE = 6;
    public static final int BORDER_WIDTH = 1;
    public static final int ACCENT_BAR_WIDTH = 3;

    // Timing tokens for future lightweight state interpolation.
    public static final int ANIMATION_FAST_MS = 90;
    public static final int ANIMATION_NORMAL_MS = 160;

    private CarbonTheme() {
    }
}
