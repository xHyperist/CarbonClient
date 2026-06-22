package com.carbonclient.bridge.api.render;

public interface RenderBridge {

    void drawRect(int left, int top, int right, int bottom, int color);

    void drawText(String text, float x, float y, int color, boolean shadow);

    int getStringWidth(String text);

    int getFontHeight();
}
