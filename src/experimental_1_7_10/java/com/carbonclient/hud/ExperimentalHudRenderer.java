package com.carbonclient.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

public final class ExperimentalHudRenderer {

    private ExperimentalHudRenderer() {
    }

    public static void drawPanelText(
        Minecraft minecraft,
        String text,
        int x,
        int y,
        float scale,
        int padding,
        int textColor,
        int backgroundColor,
        boolean showBackground
    ) {
        if (minecraft == null || minecraft.fontRendererObj == null || text == null) {
            return;
        }

        int renderX = Math.round(x / scale);
        int renderY = Math.round(y / scale);
        int textWidth = minecraft.fontRendererObj.getStringWidth(text);
        int textHeight = minecraft.fontRendererObj.FONT_HEIGHT;

        GL11.glPushMatrix();
        try {
            GL11.glScalef(scale, scale, 1.0F);

            if (showBackground) {
                Gui.drawRect(
                    renderX,
                    renderY,
                    renderX + textWidth + padding * 2,
                    renderY + textHeight + padding * 2,
                    backgroundColor
                );
            }

            minecraft.fontRendererObj.drawString(
                text,
                renderX + padding,
                renderY + padding,
                textColor
            );
        } finally {
            GL11.glPopMatrix();
        }
    }
}
