package com.carbonclient.ui.component;

import com.carbonclient.setting.impl.ColorSetting;
import com.carbonclient.ui.render.RenderUtils;
import com.carbonclient.ui.theme.CarbonTheme;
import java.awt.Color;
import net.minecraft.client.gui.FontRenderer;

public final class ColorPickerComponent {

    public static final int WIDTH = 300;
    public static final int HEIGHT = 268;

    private static final int SB_X = 12;
    private static final int SB_Y = 28;
    private static final int SB_WIDTH = 210;
    private static final int SB_HEIGHT = 105;
    private static final int HUE_X = 232;
    private static final int ALPHA_X = 254;
    private static final int BAR_WIDTH = 12;
    private static final int BAR_HEIGHT = 105;
    private static final int SWATCH_SIZE = 18;
    private static final int SWATCH_GAP = 7;
    private static final int[] PRESETS = {
        0xFFFFFFFF,
        0xFF000000,
        0xFFA8B0C2,
        0xFFFF4FA3,
        0xFF4DA6FF,
        0xFF6EE7FF,
        0xFF121A2E,
        0xFF0D1220
    };

    public void render(
        FontRenderer fontRenderer,
        ColorSetting setting,
        String hexInput,
        boolean inputFocused,
        int x,
        int y,
        int mouseX,
        int mouseY
    ) {
        RenderUtils.drawPanel(x, y, WIDTH, HEIGHT, CarbonTheme.PANEL);
        RenderUtils.drawOutline(x, y, WIDTH, HEIGHT, CarbonTheme.BORDER);
        RenderUtils.drawPanel(x, y, 3, HEIGHT, CarbonTheme.PRIMARY);
        RenderUtils.drawText(
            fontRenderer,
            setting.getName(),
            x + 10,
            y + 9,
            CarbonTheme.TEXT
        );

        drawSaturationBrightness(setting, x + SB_X, y + SB_Y);
        drawHueSlider(setting, x + HUE_X, y + SB_Y);
        drawAlphaSlider(setting, x + ALPHA_X, y + SB_Y);
        drawPresets(setting, x, y, mouseX, mouseY);
        drawHexInput(fontRenderer, setting, hexInput, inputFocused, x, y);
        drawModeControls(fontRenderer, setting, x, y, mouseX, mouseY);
        drawSpeedSlider(fontRenderer, setting, x, y);
    }

    private void drawSaturationBrightness(ColorSetting setting, int x, int y) {
        for (int column = 0; column < SB_WIDTH; column += 2) {
            float saturation = column / (float) (SB_WIDTH - 1);
            for (int row = 0; row < SB_HEIGHT; row += 2) {
                float brightness = 1.0F - row / (float) (SB_HEIGHT - 1);
                int color = 0xFF000000 | (
                    Color.HSBtoRGB(setting.getHue(), saturation, brightness)
                        & 0xFFFFFF
                );
                RenderUtils.drawPanel(x + column, y + row, 2, 2, color);
            }
        }

        int markerX = x + Math.round(setting.getSaturation() * (SB_WIDTH - 1));
        int markerY = y + Math.round((1.0F - setting.getBrightness()) * (SB_HEIGHT - 1));
        RenderUtils.drawOutline(markerX - 2, markerY - 2, 4, 4, CarbonTheme.TEXT);
    }

    private void drawHueSlider(ColorSetting setting, int x, int y) {
        for (int row = 0; row < BAR_HEIGHT; row++) {
            float hue = row / (float) (BAR_HEIGHT - 1);
            RenderUtils.drawPanel(
                x,
                y + row,
                BAR_WIDTH,
                1,
                0xFF000000 | (Color.HSBtoRGB(hue, 1.0F, 1.0F) & 0xFFFFFF)
            );
        }

        int markerY = y + Math.round(setting.getHue() * (BAR_HEIGHT - 1));
        RenderUtils.drawOutline(x - 1, markerY - 1, BAR_WIDTH + 2, 2, CarbonTheme.TEXT);
    }

    private void drawAlphaSlider(ColorSetting setting, int x, int y) {
        int baseRgb = setting.getBaseColor() & 0xFFFFFF;
        for (int row = 0; row < BAR_HEIGHT; row++) {
            int alpha = Math.round(
                (1.0F - row / (float) (BAR_HEIGHT - 1)) * 255.0F
            );
            RenderUtils.drawPanel(
                x,
                y + row,
                BAR_WIDTH,
                1,
                alpha << 24 | baseRgb
            );
        }

        int markerY = y + Math.round((1.0F - setting.getAlpha()) * (BAR_HEIGHT - 1));
        RenderUtils.drawOutline(x - 1, markerY - 1, BAR_WIDTH + 2, 2, CarbonTheme.TEXT);
    }

    private void drawPresets(
        ColorSetting setting,
        int x,
        int y,
        int mouseX,
        int mouseY
    ) {
        for (int index = 0; index < PRESETS.length; index++) {
            int swatchX = getSwatchX(x, index);
            int swatchY = y + 145;
            boolean selected = (setting.getBaseColor() & 0xFFFFFF)
                == (PRESETS[index] & 0xFFFFFF);
            RenderUtils.drawOutline(
                swatchX,
                swatchY,
                SWATCH_SIZE,
                SWATCH_SIZE,
                selected || isInside(
                    mouseX,
                    mouseY,
                    swatchX,
                    swatchY,
                    SWATCH_SIZE,
                    SWATCH_SIZE
                ) ? CarbonTheme.ACCENT : CarbonTheme.BORDER
            );
            RenderUtils.drawPanel(
                swatchX,
                swatchY,
                SWATCH_SIZE,
                SWATCH_SIZE,
                PRESETS[index]
            );
        }
    }

    private void drawHexInput(
        FontRenderer fontRenderer,
        ColorSetting setting,
        String hexInput,
        boolean inputFocused,
        int x,
        int y
    ) {
        int inputX = x + 12;
        int inputY = y + 176;
        RenderUtils.drawPanel(inputX, inputY, 132, 22, CarbonTheme.BUTTON);
        RenderUtils.drawOutline(
            inputX,
            inputY,
            132,
            22,
            inputFocused ? CarbonTheme.ACCENT : CarbonTheme.BORDER
        );
        RenderUtils.drawPanel(
            inputX + 4,
            inputY + 4,
            14,
            14,
            setting.getColor()
        );
        RenderUtils.drawText(
            fontRenderer,
            hexInput + (inputFocused ? "_" : ""),
            inputX + 24,
            inputY + 7,
            CarbonTheme.TEXT
        );
    }

    private void drawModeControls(
        FontRenderer fontRenderer,
        ColorSetting setting,
        int x,
        int y,
        int mouseX,
        int mouseY
    ) {
        drawControl(
            fontRenderer,
            "Chroma: " + (setting.isChroma() ? "ON" : "OFF"),
            x + 154,
            y + 176,
            62,
            setting.isChroma() ? CarbonTheme.ACCENT : CarbonTheme.PRIMARY,
            mouseX,
            mouseY
        );
        drawControl(
            fontRenderer,
            setting.getType(),
            x + 226,
            y + 176,
            62,
            CarbonTheme.SECONDARY,
            mouseX,
            mouseY
        );
    }

    private void drawSpeedSlider(
        FontRenderer fontRenderer,
        ColorSetting setting,
        int x,
        int y
    ) {
        int sliderX = x + 12;
        int sliderY = y + 222;
        int sliderWidth = WIDTH - 24;
        double progress = (setting.getSpeed() - 0.1D) / 4.9D;
        int fill = (int) Math.round(progress * sliderWidth);

        RenderUtils.drawText(
            fontRenderer,
            String.format("Speed %.1f", setting.getSpeed()),
            sliderX,
            y + 207,
            CarbonTheme.MUTED_TEXT
        );
        RenderUtils.drawPanel(sliderX, sliderY, sliderWidth, 5, CarbonTheme.BUTTON);
        RenderUtils.drawPanel(sliderX, sliderY, fill, 5, CarbonTheme.ACCENT);
        RenderUtils.drawPanel(
            sliderX + fill - 2,
            sliderY - 3,
            4,
            11,
            CarbonTheme.TEXT
        );
        RenderUtils.drawText(
            fontRenderer,
            "Static / Wave / Rainbow",
            sliderX,
            y + 242,
            CarbonTheme.MUTED_TEXT
        );
    }

    private void drawControl(
        FontRenderer fontRenderer,
        String text,
        int x,
        int y,
        int width,
        int accent,
        int mouseX,
        int mouseY
    ) {
        RenderUtils.drawButton(
            x,
            y,
            width,
            22,
            isInside(mouseX, mouseY, x, y, width, 22),
            accent
        );
        RenderUtils.drawCenteredText(
            fontRenderer,
            text,
            x,
            y,
            width,
            22,
            CarbonTheme.TEXT
        );
    }

    public boolean isSaturationBrightness(int mouseX, int mouseY, int x, int y) {
        return isInside(mouseX, mouseY, x + SB_X, y + SB_Y, SB_WIDTH, SB_HEIGHT);
    }

    public void updateSaturationBrightness(
        ColorSetting setting,
        int mouseX,
        int mouseY,
        int x,
        int y
    ) {
        setting.setSaturation(
            clamp((mouseX - (x + SB_X)) / (float) (SB_WIDTH - 1))
        );
        setting.setBrightness(
            1.0F - clamp((mouseY - (y + SB_Y)) / (float) (SB_HEIGHT - 1))
        );
    }

    public boolean isHueSlider(int mouseX, int mouseY, int x, int y) {
        return isInside(mouseX, mouseY, x + HUE_X, y + SB_Y, BAR_WIDTH, BAR_HEIGHT);
    }

    public void updateHue(ColorSetting setting, int mouseY, int y) {
        setting.setHue(clamp((mouseY - (y + SB_Y)) / (float) (BAR_HEIGHT - 1)));
    }

    public boolean isAlphaSlider(int mouseX, int mouseY, int x, int y) {
        return isInside(mouseX, mouseY, x + ALPHA_X, y + SB_Y, BAR_WIDTH, BAR_HEIGHT);
    }

    public void updateAlpha(ColorSetting setting, int mouseY, int y) {
        setting.setAlpha(
            1.0F - clamp((mouseY - (y + SB_Y)) / (float) (BAR_HEIGHT - 1))
        );
    }

    public int getPresetAt(int mouseX, int mouseY, int x, int y) {
        for (int index = 0; index < PRESETS.length; index++) {
            if (isInside(
                mouseX,
                mouseY,
                getSwatchX(x, index),
                y + 145,
                SWATCH_SIZE,
                SWATCH_SIZE
            )) {
                return PRESETS[index];
            }
        }
        return -1;
    }

    public boolean isHexInputHovered(int mouseX, int mouseY, int x, int y) {
        return isInside(mouseX, mouseY, x + 12, y + 176, 132, 22);
    }

    public boolean isChromaHovered(int mouseX, int mouseY, int x, int y) {
        return isInside(mouseX, mouseY, x + 154, y + 176, 62, 22);
    }

    public boolean isTypeHovered(int mouseX, int mouseY, int x, int y) {
        return isInside(mouseX, mouseY, x + 226, y + 176, 62, 22);
    }

    public boolean isSpeedSlider(int mouseX, int mouseY, int x, int y) {
        return isInside(mouseX, mouseY, x + 12, y + 216, WIDTH - 24, 18);
    }

    public void updateSpeed(ColorSetting setting, int mouseX, int x) {
        double progress = clamp((mouseX - (x + 12)) / (float) (WIDTH - 24));
        setting.setSpeed(0.1D + progress * 4.9D);
    }

    public boolean isInsidePanel(int mouseX, int mouseY, int x, int y) {
        return isInside(mouseX, mouseY, x, y, WIDTH, HEIGHT);
    }

    private int getSwatchX(int x, int index) {
        int contentWidth = PRESETS.length * SWATCH_SIZE
            + (PRESETS.length - 1) * SWATCH_GAP;
        return x + (WIDTH - contentWidth) / 2
            + index * (SWATCH_SIZE + SWATCH_GAP);
    }

    private float clamp(float value) {
        return Math.max(0.0F, Math.min(1.0F, value));
    }

    private boolean isInside(
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
