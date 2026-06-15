package com.carbonclient.ui.component;

import com.carbonclient.ui.render.RenderUtils;
import com.carbonclient.ui.theme.CarbonTheme;
import net.minecraft.client.gui.FontRenderer;

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
        int sliderY = y + CarbonTheme.SPACE_8;
        int fillWidth = (int) Math.round(width * clampedProgress);

        RenderUtils.drawPanel(
            x,
            sliderY,
            width,
            CarbonTheme.SPACE_4,
            CarbonTheme.TRACK
        );
        RenderUtils.drawPanel(
            x,
            sliderY,
            fillWidth,
            CarbonTheme.SPACE_4,
            CarbonTheme.ACCENT
        );
        RenderUtils.drawPanel(
            x + fillWidth - 2,
            sliderY - 3,
            4,
            10,
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
