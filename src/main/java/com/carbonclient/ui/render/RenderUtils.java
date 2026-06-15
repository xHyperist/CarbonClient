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
        Gui.drawRect(
            x,
            y,
            x + width,
            y + height,
            hovered ? CarbonTheme.CARD_HOVER : CarbonTheme.CARD
        );
        drawOutline(
            x,
            y,
            width,
            height,
            hovered ? accent : CarbonTheme.BORDER
        );
        Gui.drawRect(
            x,
            y,
            x + CarbonTheme.ACCENT_BAR_WIDTH,
            y + height,
            accent
        );
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
        drawOutline(
            x,
            y,
            width,
            height,
            hovered ? accent : CarbonTheme.BORDER
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
        int accent = enabled ? CarbonTheme.ACCENT : CarbonTheme.PRIMARY;
        Gui.drawRect(
            x,
            y,
            x + width,
            y + height,
            hovered ? CarbonTheme.BUTTON_HOVER : CarbonTheme.TRACK
        );
        drawOutline(
            x,
            y,
            width,
            height,
            hovered ? accent : CarbonTheme.BORDER
        );
        Gui.drawRect(x, y, x + 3, y + height, accent);
    }

    public static void drawRow(
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
            hovered ? CarbonTheme.ROW_HOVER : CarbonTheme.ROW
        );
        drawOutline(
            x,
            y,
            width,
            height,
            hovered ? accent : CarbonTheme.BORDER
        );
        Gui.drawRect(
            x,
            y,
            x + CarbonTheme.SPACE_2,
            y + height,
            accent
        );
    }

    public static void drawDivider(int x, int y, int width) {
        Gui.drawRect(x, y, x + width, y + 1, CarbonTheme.DIVIDER);
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
