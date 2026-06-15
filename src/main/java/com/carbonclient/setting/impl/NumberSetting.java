package com.carbonclient.setting.impl;

import com.carbonclient.setting.Setting;

public final class NumberSetting extends Setting<Double> {

    private final double minimum;
    private final double maximum;
    private final double increment;

    public NumberSetting(
        String name,
        double defaultValue,
        double minimum,
        double maximum,
        double increment
    ) {
        super(name, defaultValue);

        if (minimum > maximum) {
            throw new IllegalArgumentException("Minimum cannot exceed maximum.");
        }
        if (increment <= 0.0D) {
            throw new IllegalArgumentException("Increment must be greater than zero.");
        }
        if (defaultValue < minimum || defaultValue > maximum) {
            throw new IllegalArgumentException("Default value is outside the allowed range.");
        }

        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
    }

    @Override
    protected Double validate(Double value) {
        if (value == null || value.isNaN() || value.isInfinite()) {
            return getDefaultValue();
        }

        double clamped = Math.max(minimum, Math.min(maximum, value));
        double stepped = minimum
            + Math.round((clamped - minimum) / increment) * increment;
        return Math.max(minimum, Math.min(maximum, stepped));
    }

    public double getMinimum() {
        return minimum;
    }

    public double getMaximum() {
        return maximum;
    }

    public double getIncrement() {
        return increment;
    }
}
