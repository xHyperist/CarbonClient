package com.carbonclient.event;

public interface EventListener<T extends Event> {

    void onEvent(T event);
}
