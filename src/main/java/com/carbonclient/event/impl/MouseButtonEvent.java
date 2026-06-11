package com.carbonclient.event.impl;

import com.carbonclient.event.Event;

public final class MouseButtonEvent extends Event {

    private final int button;

    public MouseButtonEvent(int button) {
        this.button = button;
    }

    public int getButton() {
        return button;
    }
}
