package com.carbonclient.bridge.registry;

import com.carbonclient.bridge.api.config.ConfigBridge;
import com.carbonclient.bridge.api.entity.EntityBridge;
import com.carbonclient.bridge.api.event.EventBridge;
import com.carbonclient.bridge.api.game.GameBridge;
import com.carbonclient.bridge.api.input.InputBridge;
import com.carbonclient.bridge.api.render.RenderBridge;
import com.carbonclient.bridge.api.world.WorldBridge;

public final class BridgeRegistry {

    private static GameBridge gameBridge;
    private static RenderBridge renderBridge;
    private static InputBridge inputBridge;
    private static EventBridge eventBridge;
    private static EntityBridge entityBridge;
    private static WorldBridge worldBridge;
    private static ConfigBridge configBridge;

    private BridgeRegistry() {
    }

    /*
     * Bridge getters may return null until a version-specific implementation is
     * registered. Runtime code should check the matching has*Bridge() method first.
     * Null registrations are ignored so an existing bridge is not accidentally
     * cleared by a failed or optional bootstrap path.
     */

    public static void registerGameBridge(GameBridge bridge) {
        if (bridge == null) {
            return;
        }
        gameBridge = bridge;
    }

    public static GameBridge getGameBridge() {
        return gameBridge;
    }

    public static boolean hasGameBridge() {
        return gameBridge != null;
    }

    public static void registerRenderBridge(RenderBridge bridge) {
        if (bridge == null) {
            return;
        }
        renderBridge = bridge;
    }

    public static RenderBridge getRenderBridge() {
        return renderBridge;
    }

    public static boolean hasRenderBridge() {
        return renderBridge != null;
    }

    public static void registerInputBridge(InputBridge bridge) {
        if (bridge == null) {
            return;
        }
        inputBridge = bridge;
    }

    public static InputBridge getInputBridge() {
        return inputBridge;
    }

    public static boolean hasInputBridge() {
        return inputBridge != null;
    }

    public static void registerEventBridge(EventBridge bridge) {
        if (bridge == null) {
            return;
        }
        eventBridge = bridge;
    }

    public static EventBridge getEventBridge() {
        return eventBridge;
    }

    public static boolean hasEventBridge() {
        return eventBridge != null;
    }

    public static void registerEntityBridge(EntityBridge bridge) {
        if (bridge == null) {
            return;
        }
        entityBridge = bridge;
    }

    public static EntityBridge getEntityBridge() {
        return entityBridge;
    }

    public static boolean hasEntityBridge() {
        return entityBridge != null;
    }

    public static void registerWorldBridge(WorldBridge bridge) {
        if (bridge == null) {
            return;
        }
        worldBridge = bridge;
    }

    public static WorldBridge getWorldBridge() {
        return worldBridge;
    }

    public static boolean hasWorldBridge() {
        return worldBridge != null;
    }

    public static void registerConfigBridge(ConfigBridge bridge) {
        if (bridge == null) {
            return;
        }
        configBridge = bridge;
    }

    public static ConfigBridge getConfigBridge() {
        return configBridge;
    }

    public static boolean hasConfigBridge() {
        return configBridge != null;
    }
}
