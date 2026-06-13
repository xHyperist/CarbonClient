package com.carbonclient.setting.impl;

import com.carbonclient.setting.Setting;
import java.awt.Color;

public final class ColorSetting extends Setting<Integer> {

    public static final String TYPE_STATIC = "Static";
    public static final String TYPE_WAVE = "Wave";
    public static final String TYPE_RAINBOW = "Rainbow";

    private float hue;
    private float saturation;
    private float brightness;
    private float alpha;
    private boolean chroma;
    private String type = TYPE_STATIC;
    private double speed = 1.0D;

    public ColorSetting(String name, int defaultColor) {
        super(name, defaultColor);
        setBaseColor(defaultColor);
    }

    public int getColor() {
        float renderHue = hue;
        float renderBrightness = brightness;
        double animation = getAnimationProgress();

        if (chroma || TYPE_RAINBOW.equals(type)) {
            renderHue = (float) animation;
        } else if (TYPE_WAVE.equals(type)) {
            renderBrightness = clamp(
                (float) (brightness * (0.75D + Math.sin(animation * Math.PI * 2.0D) * 0.25D))
            );
        }

        int rgb = Color.HSBtoRGB(renderHue, saturation, renderBrightness);
        return ((int) (alpha * 255.0F) << 24) | (rgb & 0xFFFFFF);
    }

    public int getBaseColor() {
        int rgb = Color.HSBtoRGB(hue, saturation, brightness);
        return ((int) (alpha * 255.0F) << 24) | (rgb & 0xFFFFFF);
    }

    public void setBaseColor(int color) {
        setValue(color);
        float[] hsb = Color.RGBtoHSB(
            color >> 16 & 0xFF,
            color >> 8 & 0xFF,
            color & 0xFF,
            null
        );
        hue = hsb[0];
        saturation = hsb[1];
        brightness = hsb[2];
        alpha = (color >>> 24 & 0xFF) / 255.0F;
    }

    public String getHexColor() {
        return String.format("#%06X", getBaseColor() & 0xFFFFFF);
    }

    public boolean setHexColor(String hexColor) {
        Integer parsedColor = parseHexColor(hexColor);
        if (parsedColor == null) {
            return false;
        }

        setBaseColor(((int) (alpha * 255.0F) << 24) | parsedColor);
        return true;
    }

    public float getHue() {
        return hue;
    }

    public void setHue(float hue) {
        this.hue = wrap(hue);
        syncBaseValue();
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = clamp(saturation);
        syncBaseValue();
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = clamp(brightness);
        syncBaseValue();
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = clamp(alpha);
        syncBaseValue();
    }

    public boolean isChroma() {
        return chroma;
    }

    public void setChroma(boolean chroma) {
        this.chroma = chroma;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (TYPE_STATIC.equalsIgnoreCase(type)) {
            this.type = TYPE_STATIC;
        } else if (TYPE_WAVE.equalsIgnoreCase(type)) {
            this.type = TYPE_WAVE;
        } else if (TYPE_RAINBOW.equalsIgnoreCase(type)) {
            this.type = TYPE_RAINBOW;
        }
    }

    public void cycleType() {
        if (TYPE_STATIC.equals(type)) {
            type = TYPE_WAVE;
        } else if (TYPE_WAVE.equals(type)) {
            type = TYPE_RAINBOW;
        } else {
            type = TYPE_STATIC;
        }
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = Math.max(0.1D, Math.min(5.0D, speed));
    }

    private void syncBaseValue() {
        setValue(getBaseColor());
    }

    private double getAnimationProgress() {
        double period = 10000.0D / speed;
        return (System.currentTimeMillis() % (long) period) / period;
    }

    private Integer parseHexColor(String hexColor) {
        if (hexColor == null) {
            return null;
        }

        String value = hexColor.trim();
        if (value.startsWith("#")) {
            value = value.substring(1);
        }
        if (!value.matches("[0-9a-fA-F]{6}")) {
            return null;
        }

        try {
            return Integer.parseInt(value, 16) & 0xFFFFFF;
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private float clamp(float value) {
        return Math.max(0.0F, Math.min(1.0F, value));
    }

    private float wrap(float value) {
        float wrapped = value % 1.0F;
        return wrapped < 0.0F ? wrapped + 1.0F : wrapped;
    }
}
