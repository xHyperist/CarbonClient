package com.carbonclient.ui.component;

import com.carbonclient.ui.render.RenderUtils;
import com.carbonclient.ui.theme.CarbonTheme;
import net.minecraft.client.gui.FontRenderer;

public final class ButtonComponent {

    public void render(
        FontRenderer fontRenderer,
        String label,
        int x,
        int y,
        int width,
        int height,
        int mouseX,
        int mouseY,
        int accent
    ) {
        RenderUtils.drawButton(
            x,
            y,
            width,
            height,
            isHovered(mouseX, mouseY, x, y, width, height),
            accent
        );
        RenderUtils.drawCenteredText(
            fontRenderer,
            label,
            x,
            y,
            width,
            height,
            CarbonTheme.TEXT
        );
    }

    public boolean isHovered(
        int mouseX,
        int mouseY,
        int x,
        int y,
        int width,
        int height
    ) {
        return mouseX >= x
            && mouseX < x + width
            && mouseY >= y
            && mouseY < y + height;
    }
}
