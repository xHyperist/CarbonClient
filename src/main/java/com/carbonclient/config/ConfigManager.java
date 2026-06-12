package com.carbonclient.config;

import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleManager;
import com.carbonclient.setting.Setting;
import com.carbonclient.setting.impl.BooleanSetting;
import com.carbonclient.setting.impl.ColorSetting;
import com.carbonclient.setting.impl.KeybindSetting;
import com.carbonclient.setting.impl.ModeSetting;
import com.carbonclient.setting.impl.NumberSetting;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.apache.logging.log4j.Logger;

public final class ConfigManager {

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    private final ModuleManager moduleManager;
    private final File configDirectory;
    private final File configFile;
    private final Logger logger;

    public ConfigManager(File gameDirectory, ModuleManager moduleManager, Logger logger) {
        if (gameDirectory == null || moduleManager == null || logger == null) {
            throw new IllegalArgumentException(
                "Game directory, ModuleManager and logger cannot be null."
            );
        }

        this.moduleManager = moduleManager;
        this.configDirectory = new File(gameDirectory, "carbon");
        this.configFile = new File(configDirectory, "config.json");
        this.logger = logger;
    }

    public void load() {
        if (!configFile.isFile()) {
            logger.info("No Carbon config found. Using default values.");
            return;
        }

        try (Reader reader = new InputStreamReader(
            new FileInputStream(configFile),
            StandardCharsets.UTF_8
        )) {
            JsonElement rootElement = new JsonParser().parse(reader);
            if (!rootElement.isJsonObject()) {
                logger.warn("Carbon config root is not a JSON object. Using defaults.");
                return;
            }

            JsonObject modulesObject = getObject(rootElement.getAsJsonObject(), "modules");
            if (modulesObject == null) {
                logger.warn("Carbon config has no valid modules object. Using defaults.");
                return;
            }

            for (Module module : moduleManager.getModules()) {
                loadModule(module, getObject(modulesObject, module.getName()));
            }

            logger.info("Loaded Carbon config from {}", configFile.getAbsolutePath());
        } catch (Exception exception) {
            logger.warn(
                "Could not load Carbon config. Default values will be used.",
                exception
            );
        }
    }

    public synchronized void save() {
        try {
            if (!configDirectory.exists() && !configDirectory.mkdirs()) {
                throw new IllegalStateException(
                    "Could not create config directory: " + configDirectory
                );
            }

            JsonObject root = new JsonObject();
            JsonObject modulesObject = new JsonObject();

            for (Module module : moduleManager.getModules()) {
                modulesObject.add(module.getName(), serializeModule(module));
            }

            root.add("modules", modulesObject);
            writeAtomically(root);
            logger.info("Saved Carbon config to {}", configFile.getAbsolutePath());
        } catch (Exception exception) {
            logger.warn("Could not save Carbon config.", exception);
        }
    }

    private void loadModule(Module module, JsonObject moduleObject) {
        if (moduleObject == null) {
            return;
        }

        try {
            JsonElement keyCode = moduleObject.get("keyCode");
            if (keyCode != null && keyCode.isJsonPrimitive()) {
                module.setKeyCode(keyCode.getAsInt());
            }
        } catch (Exception exception) {
            logger.warn("Ignoring invalid keyCode for module {}", module.getName());
        }

        JsonObject settingsObject = getObject(moduleObject, "settings");
        if (settingsObject != null) {
            for (Setting<?> setting : module.getSettings()) {
                loadSetting(module, setting, settingsObject.get(setting.getName()));
            }
        }

        try {
            JsonElement enabled = moduleObject.get("enabled");
            if (enabled != null && enabled.isJsonPrimitive()) {
                module.setEnabled(enabled.getAsBoolean());
            }
        } catch (Exception exception) {
            logger.warn("Ignoring invalid enabled state for module {}", module.getName());
        }
    }

    private void loadSetting(
        Module module,
        Setting<?> setting,
        JsonElement value
    ) {
        if (value == null || !value.isJsonPrimitive()) {
            return;
        }

        try {
            if (setting instanceof BooleanSetting) {
                ((BooleanSetting) setting).setValue(value.getAsBoolean());
            } else if (setting instanceof KeybindSetting) {
                ((KeybindSetting) setting).setValue(value.getAsInt());
            } else if (setting instanceof NumberSetting) {
                ((NumberSetting) setting).setValue(value.getAsDouble());
            } else if (setting instanceof ModeSetting) {
                ((ModeSetting) setting).setValue(value.getAsString());
            } else if (setting instanceof ColorSetting) {
                ((ColorSetting) setting).setValue(value.getAsInt());
            }
        } catch (Exception exception) {
            logger.warn(
                "Ignoring invalid setting {} for module {}",
                setting.getName(),
                module.getName()
            );
        }
    }

    private JsonObject serializeModule(Module module) {
        JsonObject moduleObject = new JsonObject();
        JsonObject settingsObject = new JsonObject();

        moduleObject.addProperty("enabled", module.isEnabled());
        moduleObject.addProperty("keyCode", module.getKeyCode());

        for (Setting<?> setting : module.getSettings()) {
            addSettingValue(settingsObject, setting);
        }

        moduleObject.add("settings", settingsObject);
        return moduleObject;
    }

    private void addSettingValue(JsonObject settingsObject, Setting<?> setting) {
        if (setting instanceof BooleanSetting) {
            settingsObject.addProperty(setting.getName(), (Boolean) setting.getValue());
        } else if (setting instanceof KeybindSetting) {
            settingsObject.addProperty(setting.getName(), (Integer) setting.getValue());
        } else if (setting instanceof NumberSetting) {
            settingsObject.addProperty(setting.getName(), (Double) setting.getValue());
        } else if (setting instanceof ModeSetting) {
            settingsObject.addProperty(setting.getName(), (String) setting.getValue());
        } else if (setting instanceof ColorSetting) {
            settingsObject.addProperty(setting.getName(), (Integer) setting.getValue());
        }
    }

    private void writeAtomically(JsonObject root) throws Exception {
        File temporaryFile = new File(configDirectory, "config.json.tmp");

        try (Writer writer = new OutputStreamWriter(
            new FileOutputStream(temporaryFile),
            StandardCharsets.UTF_8
        )) {
            GSON.toJson(root, writer);
        }

        try {
            Files.move(
                temporaryFile.toPath(),
                configFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE
            );
        } catch (Exception exception) {
            Files.move(
                temporaryFile.toPath(),
                configFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING
            );
        }
    }

    private JsonObject getObject(JsonObject parent, String name) {
        JsonElement element = parent.get(name);
        return element != null && element.isJsonObject()
            ? element.getAsJsonObject()
            : null;
    }

    public File getConfigFile() {
        return configFile;
    }
}
