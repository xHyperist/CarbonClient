package com.carbonclient.bridge.diagnostics;

import com.carbonclient.bridge.api.game.GameBridge;
import com.carbonclient.bridge.impl.v1_8_9.BridgeVersionInfo;
import com.carbonclient.bridge.registry.BridgeRegistry;

public final class BridgeDiagnostics {

    private BridgeDiagnostics() {
    }

    public static BridgeDiagnosticsReport createReport() {
        boolean gameAvailable = safeHasGameBridge();
        boolean inputAvailable = safeHasInputBridge();
        boolean renderAvailable = safeHasRenderBridge();
        boolean eventAvailable = safeHasEventBridge();
        boolean entityAvailable = safeHasEntityBridge();
        boolean worldAvailable = safeHasWorldBridge();
        boolean configAvailable = safeHasConfigBridge();
        String versionName = resolveVersionName(gameAvailable);
        String bridgeStatus = resolveBridgeStatus();
        boolean passiveReady = gameAvailable
            && inputAvailable
            && renderAvailable
            && "PASSIVE_IMPLEMENTATION".equals(bridgeStatus);
        String summary = "BridgeDiagnostics{"
            + "version=" + versionName
            + ", status=" + bridgeStatus
            + ", coreReady=" + (gameAvailable && inputAvailable && renderAvailable)
            + ", event=" + eventAvailable
            + ", entity=" + entityAvailable
            + ", world=" + worldAvailable
            + ", config=" + configAvailable
            + "}";

        return new BridgeDiagnosticsReport(
            gameAvailable,
            inputAvailable,
            renderAvailable,
            eventAvailable,
            entityAvailable,
            worldAvailable,
            configAvailable,
            versionName,
            bridgeStatus,
            passiveReady,
            summary
        );
    }

    public static boolean areCoreBridgesAvailable() {
        return safeHasGameBridge()
            && safeHasInputBridge()
            && safeHasRenderBridge();
    }

    public static boolean isPassiveBridgeReady() {
        return areCoreBridgesAvailable()
            && "PASSIVE_IMPLEMENTATION".equals(resolveBridgeStatus());
    }

    public static String getSummaryLine() {
        return createReport().getSummary();
    }

    private static String resolveVersionName(boolean gameAvailable) {
        if (!gameAvailable) {
            return "UNKNOWN";
        }

        try {
            GameBridge gameBridge = BridgeRegistry.getGameBridge();
            if (gameBridge == null) {
                return "UNKNOWN";
            }

            String versionName = gameBridge.getVersionName();
            return versionName != null ? versionName : "UNKNOWN";
        } catch (RuntimeException exception) {
            return "UNKNOWN";
        }
    }

    private static String resolveBridgeStatus() {
        try {
            String status = BridgeVersionInfo.BRIDGE_STATUS;
            return status != null ? status : "UNKNOWN";
        } catch (RuntimeException exception) {
            return "UNKNOWN";
        }
    }

    private static boolean safeHasGameBridge() {
        try {
            return BridgeRegistry.hasGameBridge();
        } catch (RuntimeException exception) {
            return false;
        }
    }

    private static boolean safeHasInputBridge() {
        try {
            return BridgeRegistry.hasInputBridge();
        } catch (RuntimeException exception) {
            return false;
        }
    }

    private static boolean safeHasRenderBridge() {
        try {
            return BridgeRegistry.hasRenderBridge();
        } catch (RuntimeException exception) {
            return false;
        }
    }

    private static boolean safeHasEventBridge() {
        try {
            return BridgeRegistry.hasEventBridge();
        } catch (RuntimeException exception) {
            return false;
        }
    }

    private static boolean safeHasEntityBridge() {
        try {
            return BridgeRegistry.hasEntityBridge();
        } catch (RuntimeException exception) {
            return false;
        }
    }

    private static boolean safeHasWorldBridge() {
        try {
            return BridgeRegistry.hasWorldBridge();
        } catch (RuntimeException exception) {
            return false;
        }
    }

    private static boolean safeHasConfigBridge() {
        try {
            return BridgeRegistry.hasConfigBridge();
        } catch (RuntimeException exception) {
            return false;
        }
    }
}
