package com.carbonclient.ui.component;

import com.carbonclient.ui.render.RenderUtils;
import com.carbonclient.ui.theme.CarbonTheme;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public final class SliderComponent {

    public void render(
        FontRenderer fontRenderer,
        String valueText,
        double progress,
        int x,
        int y,
        int width,
        int controlWidth
    ) {
        double clampedProgress = Math.max(0.0D, Math.min(1.0D, progress));
        int sliderY = y + 8;
        int fillWidth = (int) Math.round(width * clampedProgress);

        Gui.drawRect(x, sliderY, x + width, sliderY + 4, CarbonTheme.BUTTON);
        Gui.drawRect(x, sliderY, x + fillWidth, sliderY + 4, CarbonTheme.ACCENT);
        Gui.drawRect(
            x + fillWidth - 2,
            sliderY - 3,
            x + fillWidth + 2,
            sliderY + 7,
            CarbonTheme.TEXT
        );
        RenderUtils.drawText(
            fontRenderer,
            valueText,
            x + controlWidth - fontRenderer.getStringWidth(valueText),
            y + 5,
            CarbonTheme.TEXT
        );
    }
}
