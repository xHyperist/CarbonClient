package com.carbonclient.bridge.api.event;

public interface EventBridge {

    void initialize();

    void shutdown();

    String getBridgeName();
}
