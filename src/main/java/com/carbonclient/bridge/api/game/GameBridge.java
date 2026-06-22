package com.carbonclient.bridge.api.game;

public interface GameBridge {

    boolean isInGame();

    boolean isSingleplayer();

    String getVersionName();

    int getDisplayWidth();

    int getDisplayHeight();

    Object getMinecraftInstance();
}
