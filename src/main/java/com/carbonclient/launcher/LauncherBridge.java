package com.carbonclient.launcher;

public interface LauncherBridge {

    boolean isConnected();

    void notifyClientReady(String clientVersion);
}
