package com.carbonclient.setting.impl;

import com.carbonclient.setting.Setting;

public final class ColorSetting extends Setting<Integer> {

    public ColorSetting(String name, int defaultColor) {
        super(name, defaultColor);
    }

    public int getColor() {
        return getValue();
    }
}
