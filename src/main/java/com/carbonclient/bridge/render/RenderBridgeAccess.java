package com.carbonclient.bridge.render;

import com.carbonclient.bridge.api.render.RenderBridge;
import com.carbonclient.bridge.diagnostics.BridgeDiagnostics;
import com.carbonclient.bridge.registry.BridgeRegistry;

public final class RenderBridgeAccess {

    private RenderBridgeAccess() {
    }

    public static RenderBridge getIfReady() {
        if (!BridgeDiagnostics.isPassiveBridgeReady()) {
            return null;
        }
        if (!BridgeRegistry.hasRenderBridge()) {
            return null;
        }

        try {
            return BridgeRegistry.getRenderBridge();
        } catch (Exception exception) {
            return null;
        }
    }

    public static int safeFontHeight(RenderBridge bridge) {
        if (bridge == null) {
            return -1;
        }

        try {
            int height = bridge.getFontHeight();
            return height > 0 ? height : -1;
        } catch (Exception exception) {
            return -1;
        }
    }

    public static int safeStringWidth(RenderBridge bridge, String text) {
        if (bridge == null || text == null) {
            return -1;
        }

        try {
            int width = bridge.getStringWidth(text);
            return width > 0 ? width : -1;
        } catch (Exception exception) {
            return -1;
        }
    }

    public static boolean hasValidMetrics(RenderBridge bridge, String... texts) {
        if (safeFontHeight(bridge) <= 0) {
            return false;
        }
        if (texts == null) {
            return false;
        }

        for (String text : texts) {
            if (safeStringWidth(bridge, text) <= 0) {
                return false;
            }
        }
        return true;
    }
}
