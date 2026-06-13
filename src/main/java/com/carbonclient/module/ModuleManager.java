package com.carbonclient.module;

import com.carbonclient.event.EventBus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ModuleManager {

    private final EventBus eventBus;
    private final List<Module> modules = new ArrayList<Module>();

    public ModuleManager(EventBus eventBus) {
        if (eventBus == null) {
            throw new IllegalArgumentException("EventBus cannot be null.");
        }

        this.eventBus = eventBus;
    }

    public void register(Module module) {
        if (module == null) {
            throw new IllegalArgumentException("Module cannot be null.");
        }
        if (getModule(module.getName()) != null) {
            throw new IllegalArgumentException(
                "A module named '" + module.getName() + "' is already registered."
            );
        }

        module.attachEventBus(eventBus);
        modules.add(module);
        module.resetToDefaults();
    }

    public Module getModule(String name) {
        if (name == null) {
            return null;
        }

        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }

        return null;
    }

    public List<Module> getModulesByKeyCode(int keyCode) {
        List<Module> matches = new ArrayList<Module>();

        if (keyCode == 0) {
            return matches;
        }

        for (Module module : modules) {
            if (module.getKeyCode() == keyCode) {
                matches.add(module);
            }
        }

        return matches;
    }

    public boolean toggleByKeyCode(int keyCode) {
        List<Module> matches = getModulesByKeyCode(keyCode);

        for (Module module : matches) {
            module.toggle();
        }

        return !matches.isEmpty();
    }

    public boolean toggle(String name) {
        Module module = getModule(name);
        if (module == null) {
            return false;
        }

        module.toggle();
        return true;
    }

    public boolean setEnabled(String name, boolean enabled) {
        Module module = getModule(name);
        if (module == null) {
            return false;
        }

        module.setEnabled(enabled);
        return true;
    }

    public List<Module> getModules() {
        return Collections.unmodifiableList(modules);
    }

    public void resetAllToDefaults() {
        for (Module module : modules) {
            module.resetToDefaults();
        }
    }
}
