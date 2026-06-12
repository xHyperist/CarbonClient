package com.carbonclient.setting.impl;

import com.carbonclient.setting.Setting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ModeSetting extends Setting<String> {

    private final List<String> modes;

    public ModeSetting(String name, String defaultValue, String... modes) {
        super(name, defaultValue);

        if (modes == null || modes.length == 0) {
            throw new IllegalArgumentException("ModeSetting requires at least one mode.");
        }

        this.modes = Collections.unmodifiableList(
            new ArrayList<String>(Arrays.asList(modes))
        );

        if (!containsMode(defaultValue)) {
            throw new IllegalArgumentException("Default value must be one of the modes.");
        }
    }

    @Override
    protected String validate(String value) {
        for (String mode : modes) {
            if (mode.equalsIgnoreCase(value)) {
                return mode;
            }
        }

        throw new IllegalArgumentException("Unknown mode: " + value);
    }

    private boolean containsMode(String value) {
        for (String mode : modes) {
            if (mode.equalsIgnoreCase(value)) {
                return true;
            }
        }

        return false;
    }

    public List<String> getModes() {
        return modes;
    }
}
