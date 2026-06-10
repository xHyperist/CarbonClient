package com.carbonclient.module;

public abstract class Module {

    private final String name;
    private final String description;
    private final ModuleCategory category;
    private boolean enabled;

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
}
