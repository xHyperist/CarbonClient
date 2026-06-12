package com.carbonclient.gui;

import com.carbonclient.common.Reference;
import java.io.IOException;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public final class CarbonMenuScreen extends GuiScreen {

    private static final int OVERLAY_COLOR = 0xB0080C12;
    private static final int PANEL_COLOR = 0xF0101620;
    private static final int HEADER_COLOR = 0xFF151D29;
    private static final int CONTENT_COLOR = 0xFF0D131C;
    private static final int BORDER_COLOR = 0xFF263448;
    private static final int ACCENT_COLOR = 0xFF22C7E8;
    private static final int PRIMARY_TEXT_COLOR = 0xFFFFFFFF;
    private static final int SECONDARY_TEXT_COLOR = 0xFF8E9BAD;

    private static final int PANEL_WIDTH = 500;
    private static final int PANEL_HEIGHT = 300;
    private static final int HEADER_HEIGHT = 46;
    private static final int FOOTER_HEIGHT = 28;
    private static final int PADDING = 16;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(0, 0, width, height, OVERLAY_COLOR);

        int panelWidth = Math.min(PANEL_WIDTH, width - 30);
        int panelHeight = Math.min(PANEL_HEIGHT, height - 30);
        int panelX = (width - panelWidth) / 2;
        int panelY = (height - panelHeight) / 2;
        int panelRight = panelX + panelWidth;
        int panelBottom = panelY + panelHeight;
        int headerBottom = panelY + HEADER_HEIGHT;
        int footerTop = panelBottom - FOOTER_HEIGHT;

        Gui.drawRect(panelX - 1, panelY - 1, panelRight + 1, panelBottom + 1, BORDER_COLOR);
        Gui.drawRect(panelX, panelY, panelRight, headerBottom, HEADER_COLOR);
        Gui.drawRect(panelX, headerBottom, panelRight, footerTop, CONTENT_COLOR);
        Gui.drawRect(panelX, footerTop, panelRight, panelBottom, PANEL_COLOR);

        fontRendererObj.drawString(
            "CARBON CLIENT",
            panelX + PADDING,
            panelY + 18,
            PRIMARY_TEXT_COLOR
        );

        int tabsX = panelX + 190;
        drawTab("Mods", tabsX, panelY, true);
        drawTab("Settings", tabsX + 70, panelY, false);
        drawTab("Profiles", tabsX + 160, panelY, false);

        fontRendererObj.drawString(
            "Version: " + Reference.VERSION,
            panelX + PADDING,
            footerTop + 10,
            SECONDARY_TEXT_COLOR
        );

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawTab(String label, int x, int panelY, boolean selected) {
        int color = selected ? PRIMARY_TEXT_COLOR : SECONDARY_TEXT_COLOR;
        int textY = panelY + 18;

        fontRendererObj.drawString(label, x, textY, color);

        if (selected) {
            int underlineY = panelY + HEADER_HEIGHT - 3;
            Gui.drawRect(
                x,
                underlineY,
                x + fontRendererObj.getStringWidth(label),
                underlineY + 2,
                ACCENT_COLOR
            );
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_RSHIFT) {
            mc.displayGuiScreen(null);
            return;
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
