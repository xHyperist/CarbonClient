package com.carbonclient.bridge.diagnostics;

public final class BridgeDiagnosticsReport {

    private final boolean gameBridgeAvailable;
    private final boolean inputBridgeAvailable;
    private final boolean renderBridgeAvailable;
    private final boolean eventBridgeAvailable;
    private final boolean entityBridgeAvailable;
    private final boolean worldBridgeAvailable;
    private final boolean configBridgeAvailable;
    private final String gameVersionName;
    private final String bridgeStatus;
    private final boolean passiveReady;
    private final String summary;

    public BridgeDiagnosticsReport(
        boolean gameBridgeAvailable,
        boolean inputBridgeAvailable,
        boolean renderBridgeAvailable,
        boolean eventBridgeAvailable,
        boolean entityBridgeAvailable,
        boolean worldBridgeAvailable,
        boolean configBridgeAvailable,
        String gameVersionName,
        String bridgeStatus,
        boolean passiveReady,
        String summary
    ) {
        this.gameBridgeAvailable = gameBridgeAvailable;
        this.inputBridgeAvailable = inputBridgeAvailable;
        this.renderBridgeAvailable = renderBridgeAvailable;
        this.eventBridgeAvailable = eventBridgeAvailable;
        this.entityBridgeAvailable = entityBridgeAvailable;
        this.worldBridgeAvailable = worldBridgeAvailable;
        this.configBridgeAvailable = configBridgeAvailable;
        this.gameVersionName = gameVersionName;
        this.bridgeStatus = bridgeStatus;
        this.passiveReady = passiveReady;
        this.summary = summary;
    }

    public boolean isGameBridgeAvailable() {
        return gameBridgeAvailable;
    }

    public boolean isInputBridgeAvailable() {
        return inputBridgeAvailable;
    }

    public boolean isRenderBridgeAvailable() {
        return renderBridgeAvailable;
    }

    public boolean isEventBridgeAvailable() {
        return eventBridgeAvailable;
    }

    public boolean isEntityBridgeAvailable() {
        return entityBridgeAvailable;
    }

    public boolean isWorldBridgeAvailable() {
        return worldBridgeAvailable;
    }

    public boolean isConfigBridgeAvailable() {
        return configBridgeAvailable;
    }

    public String getGameVersionName() {
        return gameVersionName;
    }

    public String getBridgeStatus() {
        return bridgeStatus;
    }

    public boolean isPassiveReady() {
        return passiveReady;
    }

    public String getSummary() {
        return summary;
    }
}
