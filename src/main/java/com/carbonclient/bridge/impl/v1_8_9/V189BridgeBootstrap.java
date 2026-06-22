package com.carbonclient.bridge.impl.v1_8_9;

import com.carbonclient.bridge.registry.BridgeRegistry;

public final class V189BridgeBootstrap {

    private static boolean bootstrapped;

    private V189BridgeBootstrap() {
    }

    public static void bootstrap() {
        if (bootstrapped) {
            return;
        }

        BridgeRegistry.registerGameBridge(new V189GameBridge());
        BridgeRegistry.registerInputBridge(new V189InputBridge());
        BridgeRegistry.registerRenderBridge(new V189RenderBridge());
        bootstrapped = true;
    }
}
