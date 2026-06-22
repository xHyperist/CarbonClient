package com.carbonclient.bridge.impl.v1_8_9;

import com.carbonclient.bridge.api.game.GameBridge;
import net.minecraft.client.Minecraft;

public final class V189GameBridge implements GameBridge {

    @Override
    public boolean isInGame() {
        Minecraft minecraft = getMinecraft();
        return minecraft != null
            && minecraft.theWorld != null
            && minecraft.thePlayer != null;
    }

    @Override
    public boolean isSingleplayer() {
        Minecraft minecraft = getMinecraft();
        if (minecraft == null) {
            return false;
        }

        try {
            return minecraft.isSingleplayer();
        } catch (RuntimeException exception) {
            return false;
        }
    }

    @Override
    public String getVersionName() {
        return BridgeVersionInfo.VERSION_NAME;
    }

    @Override
    public int getDisplayWidth() {
        Minecraft minecraft = getMinecraft();
        return minecraft != null ? minecraft.displayWidth : 0;
    }

    @Override
    public int getDisplayHeight() {
        Minecraft minecraft = getMinecraft();
        return minecraft != null ? minecraft.displayHeight : 0;
    }

    @Override
    public Object getMinecraftInstance() {
        return getMinecraft();
    }

    private Minecraft getMinecraft() {
        try {
            return Minecraft.getMinecraft();
        } catch (RuntimeException exception) {
            return null;
        }
    }
}
