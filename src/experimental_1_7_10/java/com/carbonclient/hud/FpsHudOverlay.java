package com.carbonclient.hud;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public final class FpsHudOverlay {

    private static final int X = 5;
    private static final int Y = 0;
    private static final float SCALE = 1.0F;
    private static final int PADDING = 4;
    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int BACKGROUND_COLOR = 0xB0121824;

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private long lastFpsUpdateTime = System.currentTimeMillis();
    private int frameCounter;
    private int displayedFps;

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        if (event.type != ElementType.TEXT) {
            return;
        }

        updateFpsCounter();

        ExperimentalHudRenderer.drawPanelText(
            minecraft,
            displayedFps + " FPS",
            X,
            Y,
            SCALE,
            PADDING,
            TEXT_COLOR,
            BACKGROUND_COLOR,
            true
        );
    }

    private void updateFpsCounter() {
        frameCounter++;

        long now = System.currentTimeMillis();
        if (now - lastFpsUpdateTime >= 1000L) {
            displayedFps = frameCounter;
            frameCounter = 0;
            lastFpsUpdateTime = now;
        }
    }
}
