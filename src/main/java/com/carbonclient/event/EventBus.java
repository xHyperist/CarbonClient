package com.carbonclient.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EventBus {

    private final Map<Class<? extends Event>, List<EventListener<? extends Event>>> listeners =
        new HashMap<Class<? extends Event>, List<EventListener<? extends Event>>>();

    public <T extends Event> void subscribe(
        Class<T> eventType,
        EventListener<T> listener
    ) {
        if (eventType == null || listener == null) {
            throw new IllegalArgumentException("Event type and listener cannot be null.");
        }

        List<EventListener<? extends Event>> eventListeners = listeners.get(eventType);
        if (eventListeners == null) {
            eventListeners = new ArrayList<EventListener<? extends Event>>();
            listeners.put(eventType, eventListeners);
        }

        if (!eventListeners.contains(listener)) {
            eventListeners.add(listener);
        }
    }

    public <T extends Event> void unsubscribe(
        Class<T> eventType,
        EventListener<T> listener
    ) {
        List<EventListener<? extends Event>> eventListeners = listeners.get(eventType);
        if (eventListeners == null) {
            return;
        }

        eventListeners.remove(listener);
        if (eventListeners.isEmpty()) {
            listeners.remove(eventType);
        }
    }

    public <T extends Event> void post(T event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null.");
        }

        List<EventListener<? extends Event>> eventListeners =
            listeners.get(event.getClass());
        if (eventListeners == null) {
            return;
        }

        List<EventListener<? extends Event>> snapshot =
            new ArrayList<EventListener<? extends Event>>(eventListeners);

        for (EventListener<? extends Event> listener : snapshot) {
            notifyListener(listener, event);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Event> void notifyListener(
        EventListener<? extends Event> listener,
        T event
    ) {
        ((EventListener<T>) listener).onEvent(event);
    }
}
