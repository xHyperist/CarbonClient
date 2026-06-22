package com.carbonclient.bridge.api.input;

public interface InputBridge {

    boolean isKeyDown(int keyCode);

    boolean isMouseButtonDown(int button);

    String getKeyName(int keyCode);
}
