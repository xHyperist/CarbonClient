package com.carbonclient.bridge.impl.v1_8_9;

import com.carbonclient.bridge.api.input.InputBridge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public final class V189InputBridge implements InputBridge {

    @Override
    public boolean isKeyDown(int keyCode) {
        if (keyCode < 0) {
            return false;
        }

        try {
            if (!Keyboard.isCreated()) {
                return false;
            }
            return Keyboard.isKeyDown(keyCode);
        } catch (RuntimeException exception) {
            return false;
        }
    }

    @Override
    public boolean isMouseButtonDown(int button) {
        if (button < 0) {
            return false;
        }

        try {
            if (!Mouse.isCreated()) {
                return false;
            }
            return Mouse.isButtonDown(button);
        } catch (RuntimeException exception) {
            return false;
        }
    }

    @Override
    public String getKeyName(int keyCode) {
        if (keyCode <= 0) {
            return "NONE";
        }

        try {
            if (!Keyboard.isCreated()) {
                return "UNKNOWN";
            }
            String keyName = Keyboard.getKeyName(keyCode);
            return keyName != null ? keyName : "UNKNOWN";
        } catch (RuntimeException exception) {
            return "UNKNOWN";
        }
    }
}
