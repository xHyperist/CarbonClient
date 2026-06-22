package com.carbonclient.bridge.api.world;

public interface WorldBridge {

    boolean hasWorld();

    long getWorldTime();

    String getBiomeNameAtPlayer();

    int getDimensionId();
}
