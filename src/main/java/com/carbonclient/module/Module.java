package com.carbonclient.module;

import com.carbonclient.event.Event;
import com.carbonclient.event.EventBus;
import com.carbonclient.event.EventListener;

public abstract class Module {

    private final String name;
    private final String description;
    private final ModuleCategory category;
    private EventBus eventBus;
    private boolean enabled;
    private int keyCode;

    protected Module(String name, String description, ModuleCategory category) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Module name cannot be empty.");
        }
        if (description == null) {
            throw new IllegalArgumentException("Module description cannot be null.");
        }
        if (category == null) {
            throw new IllegalArgumentException("Module category cannot be null.");
        }

        this.name = name;
        this.description = description;
        this.category = category;
    }

    public final void toggle() {
        setEnabled(!enabled);
    }

    public final void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }

        this.enabled = enabled;

        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }

        onToggle();
    }

    protected void onEnable() {
    }

    protected void onDisable() {
    }

    protected void onToggle() {
    }

    final void attachEventBus(EventBus eventBus) {
        if (this.eventBus != null) {
            throw new IllegalStateException("Module is already attached to an EventBus.");
        }

        this.eventBus = eventBus;
    }

    protected final <T extends Event> void subscribe(
        Class<T> eventType,
        EventListener<T> listener
    ) {
        requireEventBus().subscribe(eventType, listener);
    }

    protected final <T extends Event> void unsubscribe(
        Class<T> eventType,
        EventListener<T> listener
    ) {
        requireEventBus().unsubscribe(eventType, listener);
    }

    private EventBus requireEventBus() {
        if (eventBus == null) {
            throw new IllegalStateException("Module must be registered before it is enabled.");
        }

        return eventBus;
    }

    public final String getName() {
        return name;
    }

    public final String getDescription() {
        return description;
    }

    public final ModuleCategory getCategory() {
        return category;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public final int getKeyCode() {
        return keyCode;
    }

    public final void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }
}
