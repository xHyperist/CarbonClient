package com.carbonclient.visual.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;

public final class FullbrightVisual {

    private static final boolean DEFAULT_ENABLED = false;
    private static final double DEFAULT_BRIGHTNESS_LEVEL = 10.0D;
    private static final boolean DEFAULT_SMOOTH_TRANSITION = true;
    private static final double MINIMUM_BRIGHTNESS = 1.0D;
    private static final double MAXIMUM_BRIGHTNESS = 15.0D;
    private static final double BRIGHTNESS_STEP = 0.5D;

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private boolean enabled = DEFAULT_ENABLED;
    private double brightnessLevel = DEFAULT_BRIGHTNESS_LEVEL;
    private boolean smoothTransition = DEFAULT_SMOOTH_TRANSITION;
    private Float originalGamma;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }
        this.enabled = enabled;
        if (enabled) {
            captureOriginalGamma();
        }
    }

    public double getBrightnessLevel() {
        return brightnessLevel;
    }

    public void setBrightnessLevel(double brightnessLevel) {
        this.brightnessLevel = clampToStep(
            brightnessLevel,
            MINIMUM_BRIGHTNESS,
            MAXIMUM_BRIGHTNESS,
            BRIGHTNESS_STEP
        );
    }

    public boolean isSmoothTransition() {
        return smoothTransition;
    }

    public void setSmoothTransition(boolean smoothTransition) {
        this.smoothTransition = smoothTransition;
    }

    public double getMinimumBrightness() {
        return MINIMUM_BRIGHTNESS;
    }

    public double getMaximumBrightness() {
        return MAXIMUM_BRIGHTNESS;
    }

    public void resetToDefaults() {
        setEnabled(DEFAULT_ENABLED);
        setBrightnessLevel(DEFAULT_BRIGHTNESS_LEVEL);
        setSmoothTransition(DEFAULT_SMOOTH_TRANSITION);
    }

    public void onTick() {
        if (minecraft.gameSettings == null) {
            return;
        }

        if (enabled) {
            captureOriginalGamma();
            applyGamma((float) brightnessLevel);
            return;
        }

        if (originalGamma != null) {
            applyGamma(originalGamma.floatValue());
            if (!smoothTransition
                || Math.abs(minecraft.gameSettings.gammaSetting
                    - originalGamma.floatValue()) < 0.02F) {
                minecraft.gameSettings.gammaSetting = originalGamma.floatValue();
                originalGamma = null;
            }
        }
    }

    public void restoreOriginalGamma() {
        if (minecraft.gameSettings != null && originalGamma != null) {
            minecraft.gameSettings.gammaSetting = originalGamma.floatValue();
            originalGamma = null;
        }
    }

    public JsonObject serialize() {
        JsonObject object = new JsonObject();
        object.addProperty("enabled", enabled);
        object.addProperty("brightnessLevel", brightnessLevel);
        object.addProperty("smoothTransition", smoothTransition);
        return object;
    }

    public JsonObject serializeDefaults() {
        JsonObject object = new JsonObject();
        object.addProperty("enabled", DEFAULT_ENABLED);
        object.addProperty("brightnessLevel", DEFAULT_BRIGHTNESS_LEVEL);
        object.addProperty("smoothTransition", DEFAULT_SMOOTH_TRANSITION);
        return object;
    }

    public void load(JsonObject object) {
        if (object == null) {
            return;
        }

        try {
            JsonElement enabledElement = object.get("enabled");
            if (enabledElement != null && enabledElement.isJsonPrimitive()) {
                setEnabled(enabledElement.getAsBoolean());
            }
        } catch (Exception ignored) {
            setEnabled(DEFAULT_ENABLED);
        }

        try {
            JsonElement brightnessElement = object.get("brightnessLevel");
            if (brightnessElement != null && brightnessElement.isJsonPrimitive()) {
                setBrightnessLevel(brightnessElement.getAsDouble());
            }
        } catch (Exception ignored) {
            setBrightnessLevel(DEFAULT_BRIGHTNESS_LEVEL);
        }

        try {
            JsonElement smoothElement = object.get("smoothTransition");
            if (smoothElement != null && smoothElement.isJsonPrimitive()) {
                setSmoothTransition(smoothElement.getAsBoolean());
            }
        } catch (Exception ignored) {
            setSmoothTransition(DEFAULT_SMOOTH_TRANSITION);
        }
    }

    private void captureOriginalGamma() {
        if (minecraft.gameSettings != null && originalGamma == null) {
            originalGamma = Float.valueOf(minecraft.gameSettings.gammaSetting);
        }
    }

    private void applyGamma(float targetGamma) {
        if (smoothTransition) {
            float current = minecraft.gameSettings.gammaSetting;
            minecraft.gameSettings.gammaSetting = current
                + (targetGamma - current) * 0.2F;
        } else {
            minecraft.gameSettings.gammaSetting = targetGamma;
        }
    }

    private double clampToStep(
        double value,
        double minimum,
        double maximum,
        double step
    ) {
        double clamped = Math.max(minimum, Math.min(maximum, value));
        return Math.round(clamped / step) * step;
    }
}
