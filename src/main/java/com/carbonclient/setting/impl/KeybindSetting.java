package com.carbonclient.setting.impl;

import com.carbonclient.setting.Setting;
import org.lwjgl.input.Keyboard;

public final class KeybindSetting extends Setting<Integer> {

    public KeybindSetting(String name, int defaultKeyCode) {
        super(name, defaultKeyCode);
    }

    @Override
    protected Integer validate(Integer keyCode) {
        if (keyCode < Keyboard.KEY_NONE || keyCode >= Keyboard.KEYBOARD_SIZE) {
            return Keyboard.KEY_NONE;
        }

        return keyCode;
    }

    public int getKeyCode() {
        return getValue();
    }

    public String getKeyName() {
        if (getKeyCode() == Keyboard.KEY_NONE) {
            return "NONE";
        }

        String keyName = Keyboard.getKeyName(getKeyCode());
        return keyName == null ? "UNKNOWN" : keyName;
    }
}
