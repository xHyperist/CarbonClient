package com.carbonclient.bridge.api.config;

public interface ConfigBridge {

    String getConfigVersion();

    boolean supportsLegacyMigration();
}
