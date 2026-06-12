package com.carbonclient.setting;

public abstract class Setting<T> {

    private final String name;
    private final T defaultValue;
    private T value;
    private boolean visibleInOptions = true;

    protected Setting(String name, T defaultValue) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Setting name cannot be empty.");
        }
        if (defaultValue == null) {
            throw new IllegalArgumentException("Default value cannot be null.");
        }

        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public final String getName() {
        return name;
    }

    public final T getValue() {
        return value;
    }

    public final void setValue(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Setting value cannot be null.");
        }

        this.value = validate(value);
    }

    public final T getDefaultValue() {
        return defaultValue;
    }

    public final void reset() {
        value = defaultValue;
    }

    public final boolean isVisibleInOptions() {
        return visibleInOptions;
    }

    public final void setVisibleInOptions(boolean visibleInOptions) {
        this.visibleInOptions = visibleInOptions;
    }

    protected T validate(T value) {
        return value;
    }
}
