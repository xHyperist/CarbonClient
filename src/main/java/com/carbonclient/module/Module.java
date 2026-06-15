package com.carbonclient.module;

import com.carbonclient.event.Event;
import com.carbonclient.event.EventBus;
import com.carbonclient.event.EventListener;
import com.carbonclient.setting.Setting;
import com.carbonclient.setting.impl.KeybindSetting;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.lwjgl.input.Keyboard;

public abstract class Module {

    private final String name;
    private final String description;
    private final ModuleCategory category;
    private final boolean defaultEnabled;
    private final int defaultKeyCode;
    private final List<Setting<?>> settings = new ArrayList<Setting<?>>();
    private EventBus eventBus;
    private boolean enabled;
    private int keyCode;
    private KeybindSetting primaryKeybindSetting;

    protected Module(String name, String description, ModuleCategory category) {
        this(name, description, category, false, 0);
    }

    protected Module(
        String name,
        String description,
        ModuleCategory category,
        boolean defaultEnabled,
        int defaultKeyCode
    ) {
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
        this.defaultEnabled = defaultEnabled;
        this.defaultKeyCode = normalizeKeyCode(defaultKeyCode);
        this.keyCode = this.defaultKeyCode;
    }

    public final void toggle() {
        setEnabled(!enabled);
    }

    public void onKeybindPressed() {
        toggle();
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

    protected final <T extends Setting<?>> T addSetting(T setting) {
        if (setting == null) {
            throw new IllegalArgumentException("Setting cannot be null.");
        }

        for (Setting<?> existing : settings) {
            if (existing.getName().equalsIgnoreCase(setting.getName())) {
                throw new IllegalArgumentException(
                    "A setting named '" + setting.getName() + "' already exists."
                );
            }
        }

        settings.add(setting);
        return setting;
    }

    protected final <T extends Setting<?>> T addHiddenSetting(T setting) {
        T addedSetting = addSetting(setting);
        addedSetting.setVisibleInOptions(false);
        return addedSetting;
    }

    protected final KeybindSetting addPrimaryKeybindSetting(String name) {
        if (primaryKeybindSetting != null) {
            throw new IllegalStateException(
                "Primary keybind setting is already registered."
            );
        }

        primaryKeybindSetting = addSetting(
            new KeybindSetting(name, defaultKeyCode)
        );
        return primaryKeybindSetting;
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

    public final List<Setting<?>> getSettings() {
        return Collections.unmodifiableList(settings);
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public final int getKeyCode() {
        return primaryKeybindSetting == null
            ? keyCode
            : primaryKeybindSetting.getKeyCode();
    }

    public final void setKeyCode(int keyCode) {
        this.keyCode = normalizeKeyCode(keyCode);
        if (primaryKeybindSetting != null) {
            primaryKeybindSetting.setValue(this.keyCode);
        }
    }

    public final boolean isDefaultEnabled() {
        return defaultEnabled;
    }

    public final int getDefaultKeyCode() {
        return defaultKeyCode;
    }

    public final void resetToDefaults() {
        if (enabled) {
            setEnabled(false);
        }

        for (Setting<?> setting : settings) {
            setting.reset();
        }

        keyCode = defaultKeyCode;
        setEnabled(defaultEnabled);
    }

    private static int normalizeKeyCode(int keyCode) {
        return keyCode >= Keyboard.KEY_NONE && keyCode < Keyboard.KEYBOARD_SIZE
            ? keyCode
            : Keyboard.KEY_NONE;
    }
}
