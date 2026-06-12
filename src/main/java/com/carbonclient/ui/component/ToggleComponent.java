package com.carbonclient.ui.component;

import com.carbonclient.ui.render.RenderUtils;
import com.carbonclient.ui.theme.CarbonTheme;
import net.minecraft.client.gui.FontRenderer;

public final class ToggleComponent {

    public void render(
        FontRenderer fontRenderer,
        boolean enabled,
        int x,
        int y,
        int width,
        int height,
        int mouseX,
        int mouseY
    ) {
        boolean hovered = mouseX >= x
            && mouseX < x + width
            && mouseY >= y
            && mouseY < y + height;
        RenderUtils.drawToggle(x, y, width, height, enabled, hovered);
        RenderUtils.drawCenteredText(
            fontRenderer,
            enabled ? "ON" : "OFF",
            x,
            y,
            width,
            height,
            CarbonTheme.TEXT
        );
    }
}
