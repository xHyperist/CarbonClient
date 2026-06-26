package com.carbonclient.hud;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.ArrayDeque;
import java.util.Deque;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import org.lwjgl.input.Mouse;

public final class CpsHudOverlay {

    private static final long CPS_WINDOW_MILLIS = 1000L;
    private static final int LEFT_MOUSE_BUTTON = 0;
    private static final int X = 5;
    private static final int Y = 20;
    private static final float SCALE = 1.0F;
    private static final int PADDING = 4;
    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int BACKGROUND_COLOR = 0xB0121824;

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final Deque<Long> leftClicks = new ArrayDeque<Long>();
    private boolean leftMouseWasDown;

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        if (event.type != ElementType.TEXT) {
            return;
        }

        long now = System.currentTimeMillis();
        recordLeftClickIfNeeded(now);
        removeExpiredClicks(now);

        ExperimentalHudRenderer.drawPanelText(
            minecraft,
            leftClicks.size() + " CPS",
            X,
            Y,
            SCALE,
            PADDING,
            TEXT_COLOR,
            BACKGROUND_COLOR,
            true
        );
    }

    private void recordLeftClickIfNeeded(long now) {
        boolean leftMouseDown = Mouse.isButtonDown(LEFT_MOUSE_BUTTON);
        if (leftMouseDown && !leftMouseWasDown) {
            leftClicks.addLast(now);
        }
        leftMouseWasDown = leftMouseDown;
    }

    private void removeExpiredClicks(long now) {
        while (!leftClicks.isEmpty()
            && now - leftClicks.peekFirst() >= CPS_WINDOW_MILLIS) {
            leftClicks.removeFirst();
        }
    }
}
