package com.carbonclient.event.impl;

import com.carbonclient.event.Event;

public final class CrosshairRenderEvent extends Event {

    private final int screenWidth;
    private final int screenHeight;
    private final float partialTicks;
    private boolean hideVanilla;

    public CrosshairRenderEvent(
        int screenWidth,
        int screenHeight,
        float partialTicks
    ) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.partialTicks = partialTicks;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public boolean shouldHideVanilla() {
        return hideVanilla;
    }

    public void setHideVanilla(boolean hideVanilla) {
        this.hideVanilla = hideVanilla;
    }
}
