package com.carbonclient.ui.render;

import com.carbonclient.ui.theme.CarbonTheme;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public final class RenderUtils {

    private RenderUtils() {
    }

    public static void drawPanel(int x, int y, int width, int height, int color) {
        Gui.drawRect(x, y, x + width, y + height, color);
    }

    public static void drawCard(
        int x,
        int y,
        int width,
        int height,
        boolean hovered,
        int accent
    ) {
        drawOutline(x, y, width, height, CarbonTheme.BORDER);
        Gui.drawRect(
            x,
            y,
            x + width,
            y + height,
            hovered ? CarbonTheme.CARD_HOVER : CarbonTheme.CARD
        );
        Gui.drawRect(x, y, x + 3, y + height, accent);
    }

    public static void drawButton(
        int x,
        int y,
        int width,
        int height,
        boolean hovered,
        int accent
    ) {
        Gui.drawRect(
            x,
            y,
            x + width,
            y + height,
            hovered ? CarbonTheme.BUTTON_HOVER : CarbonTheme.BUTTON
        );
        Gui.drawRect(x, y + height - 2, x + width, y + height, accent);
    }

    public static void drawToggle(
        int x,
        int y,
        int width,
        int height,
        boolean enabled,
        boolean hovered
    ) {
        drawButton(
            x,
            y,
            width,
            height,
            hovered,
            enabled ? CarbonTheme.ACCENT : CarbonTheme.PRIMARY
        );
    }

    public static void drawOutline(
        int x,
        int y,
        int width,
        int height,
        int color
    ) {
        Gui.drawRect(x - 1, y - 1, x + width + 1, y, color);
        Gui.drawRect(x - 1, y + height, x + width + 1, y + height + 1, color);
        Gui.drawRect(x - 1, y, x, y + height, color);
        Gui.drawRect(x + width, y, x + width + 1, y + height, color);
    }

    public static void drawText(
        FontRenderer fontRenderer,
        String text,
        int x,
        int y,
        int color
    ) {
        fontRenderer.drawString(text, x, y, color);
    }

    public static void drawCenteredText(
        FontRenderer fontRenderer,
        String text,
        int x,
        int y,
        int width,
        int height,
        int color
    ) {
        int textX = x + (width - fontRenderer.getStringWidth(text)) / 2;
        int textY = y + (height - fontRenderer.FONT_HEIGHT) / 2;
        drawText(fontRenderer, text, textX, textY, color);
    }
}
