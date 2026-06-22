package com.carbonclient.visual.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;

public final class TimeChangerVisual {

    public static final String MODE_DAY = "Day";
    public static final String MODE_NIGHT = "Night";
    public static final String MODE_SUNSET = "Sunset";
    public static final String MODE_SUNRISE = "Sunrise";
    public static final String MODE_CUSTOM = "Custom";

    private static final boolean DEFAULT_ENABLED = false;
    private static final String DEFAULT_MODE = MODE_DAY;
    private static final double DEFAULT_CUSTOM_TIME = 6000.0D;
    private static final boolean DEFAULT_SMOOTH_TRANSITION = true;
    private static final double MINIMUM_TIME = 0.0D;
    private static final double MAXIMUM_TIME = 24000.0D;
    private static final double TIME_STEP = 250.0D;

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private boolean enabled = DEFAULT_ENABLED;
    private String mode = DEFAULT_MODE;
    private double customTime = DEFAULT_CUSTOM_TIME;
    private boolean smoothTransition = DEFAULT_SMOOTH_TRANSITION;
    private Long capturedWorldTime;
    private long enabledTicks;
    private double visualTime = DEFAULT_CUSTOM_TIME;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }
        this.enabled = enabled;
        if (enabled) {
            captureWorldTime();
            visualTime = getCurrentWorldTime();
        } else {
            restoreWorldTime();
        }
    }

    public String getMode() {
        return mode;
    }

    public void cycleMode() {
        if (MODE_DAY.equals(mode)) {
            mode = MODE_NIGHT;
        } else if (MODE_NIGHT.equals(mode)) {
            mode = MODE_SUNSET;
        } else if (MODE_SUNSET.equals(mode)) {
            mode = MODE_SUNRISE;
        } else if (MODE_SUNRISE.equals(mode)) {
            mode = MODE_CUSTOM;
        } else {
            mode = MODE_DAY;
        }
    }

    public void setMode(String mode) {
        if (isValidMode(mode)) {
            this.mode = mode;
        }
    }

    public double getCustomTime() {
        return customTime;
    }

    public void setCustomTime(double customTime) {
        this.customTime = clampToStep(
            customTime,
            MINIMUM_TIME,
            MAXIMUM_TIME,
            TIME_STEP
        );
    }

    public boolean isSmoothTransition() {
        return smoothTransition;
    }

    public void setSmoothTransition(boolean smoothTransition) {
        this.smoothTransition = smoothTransition;
    }

    public double getMinimumTime() {
        return MINIMUM_TIME;
    }

    public double getMaximumTime() {
        return MAXIMUM_TIME;
    }

    public boolean isCustomMode() {
        return MODE_CUSTOM.equals(mode);
    }

    public void resetToDefaults() {
        setEnabled(DEFAULT_ENABLED);
        setMode(DEFAULT_MODE);
        setCustomTime(DEFAULT_CUSTOM_TIME);
        setSmoothTransition(DEFAULT_SMOOTH_TRANSITION);
    }

    public void onTick() {
        WorldClient world = minecraft.theWorld;
        if (world == null) {
            capturedWorldTime = null;
            enabledTicks = 0L;
            return;
        }

        if (!enabled) {
            return;
        }

        captureWorldTime();
        enabledTicks++;
        long target = getTargetTime();
        if (smoothTransition) {
            visualTime += getShortestDelta(visualTime, target) * 0.12D;
        } else {
            visualTime = target;
        }
        world.setWorldTime(Math.round(normalizeTime(visualTime)));
    }

    public void restoreWorldTime() {
        WorldClient world = minecraft.theWorld;
        if (world != null && capturedWorldTime != null) {
            world.setWorldTime(capturedWorldTime.longValue() + enabledTicks);
        }
        capturedWorldTime = null;
        enabledTicks = 0L;
    }

    public JsonObject serialize() {
        JsonObject object = new JsonObject();
        object.addProperty("enabled", enabled);
        object.addProperty("mode", mode);
        object.addProperty("customTime", customTime);
        object.addProperty("smoothTransition", smoothTransition);
        return object;
    }

    public JsonObject serializeDefaults() {
        JsonObject object = new JsonObject();
        object.addProperty("enabled", DEFAULT_ENABLED);
        object.addProperty("mode", DEFAULT_MODE);
        object.addProperty("customTime", DEFAULT_CUSTOM_TIME);
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
            JsonElement modeElement = object.get("mode");
            if (modeElement != null && modeElement.isJsonPrimitive()) {
                setMode(modeElement.getAsString());
            }
        } catch (Exception ignored) {
            setMode(DEFAULT_MODE);
        }

        try {
            JsonElement customTimeElement = object.get("customTime");
            if (customTimeElement != null && customTimeElement.isJsonPrimitive()) {
                setCustomTime(customTimeElement.getAsDouble());
            }
        } catch (Exception ignored) {
            setCustomTime(DEFAULT_CUSTOM_TIME);
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

    private void captureWorldTime() {
        WorldClient world = minecraft.theWorld;
        if (world != null && capturedWorldTime == null) {
            capturedWorldTime = Long.valueOf(world.getWorldTime());
            enabledTicks = 0L;
        }
    }

    private long getTargetTime() {
        if (MODE_NIGHT.equals(mode)) {
            return 18000L;
        }
        if (MODE_SUNSET.equals(mode)) {
            return 12000L;
        }
        if (MODE_SUNRISE.equals(mode)) {
            return 23000L;
        }
        if (MODE_CUSTOM.equals(mode)) {
            return Math.round(customTime);
        }
        return 6000L;
    }

    private double getCurrentWorldTime() {
        return minecraft.theWorld == null
            ? DEFAULT_CUSTOM_TIME
            : normalizeTime(minecraft.theWorld.getWorldTime());
    }

    private double getShortestDelta(double current, double target) {
        double delta = normalizeTime(target) - normalizeTime(current);
        if (delta > 12000.0D) {
            delta -= 24000.0D;
        } else if (delta < -12000.0D) {
            delta += 24000.0D;
        }
        return delta;
    }

    private double normalizeTime(double time) {
        double normalized = time % 24000.0D;
        return normalized < 0.0D ? normalized + 24000.0D : normalized;
    }

    private boolean isValidMode(String mode) {
        return MODE_DAY.equals(mode)
            || MODE_NIGHT.equals(mode)
            || MODE_SUNSET.equals(mode)
            || MODE_SUNRISE.equals(mode)
            || MODE_CUSTOM.equals(mode);
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
