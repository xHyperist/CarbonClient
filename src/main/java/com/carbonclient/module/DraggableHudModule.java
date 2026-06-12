package com.carbonclient.module;

public interface DraggableHudModule {

    int getPositionX();

    int getPositionY();

    void setPosition(int x, int y);

    int getHudWidth();

    int getHudHeight();

    void renderHud();
}
