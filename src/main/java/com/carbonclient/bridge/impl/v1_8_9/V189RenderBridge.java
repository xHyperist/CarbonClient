package com.carbonclient.bridge.impl.v1_8_9;

import com.carbonclient.bridge.api.render.RenderBridge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public final class V189RenderBridge implements RenderBridge {

    @Override
    public void drawRect(int left, int top, int right, int bottom, int color) {
        Gui.drawRect(left, top, right, bottom, color);
    }

    @Override
    public void drawText(String text, float x, float y, int color, boolean shadow) {
        FontRenderer fontRenderer = getFontRenderer();
        if (fontRenderer == null) {
            throw new IllegalStateException("Font renderer is not available");
        }

        String safeText = text != null ? text : "";
        fontRenderer.drawString(safeText, x, y, color, shadow);
    }

    @Override
    public int getStringWidth(String text) {
        FontRenderer fontRenderer = getFontRenderer();
        if (fontRenderer == null) {
            return 0;
        }

        String safeText = text != null ? text : "";
        try {
            return fontRenderer.getStringWidth(safeText);
        } catch (RuntimeException exception) {
            return 0;
        }
    }

    @Override
    public int getFontHeight() {
        FontRenderer fontRenderer = getFontRenderer();
        return fontRenderer != null ? fontRenderer.FONT_HEIGHT : 0;
    }

    private FontRenderer getFontRenderer() {
        try {
            Minecraft minecraft = Minecraft.getMinecraft();
            return minecraft != null ? minecraft.fontRendererObj : null;
        } catch (RuntimeException exception) {
            return null;
        }
    }
}
