package com.carbonclient.config;

import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleManager;
import com.carbonclient.notification.NotificationManager;
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
    private final NotificationManager notificationManager;

    public ConfigManager(
        File gameDirectory,
        ModuleManager moduleManager,
        Logger logger,
        NotificationManager notificationManager
    ) {
        if (gameDirectory == null
            || moduleManager == null
            || logger == null
            || notificationManager == null) {
            throw new IllegalArgumentException(
                "ConfigManager dependencies cannot be null."
            );
        }

        this.moduleManager = moduleManager;
        this.configDirectory = new File(gameDirectory, "carbon");
        this.configFile = new File(configDirectory, "config.json");
        this.logger = logger;
        this.notificationManager = notificationManager;
    }

    public void load() {
        moduleManager.resetAllToDefaults();

        if (!configFile.isFile()) {
            logger.info("No Carbon config found. Using default values.");
            save();
            notificationManager.success(
                "Config Created",
                "Default settings were applied."
            );
            return;
        }

        try (Reader reader = new InputStreamReader(
            new FileInputStream(configFile),
            StandardCharsets.UTF_8
        )) {
            JsonElement rootElement = new JsonParser().parse(reader);
            if (!rootElement.isJsonObject()) {
                logger.warn("Carbon config root is not a JSON object. Using defaults.");
                notificationManager.warning(
                    "Config Reset",
                    "Invalid config root; defaults were restored."
                );
            } else {
                JsonObject modulesObject = getObject(
                    rootElement.getAsJsonObject(),
                    "modules"
                );
                if (modulesObject == null) {
                    logger.warn(
                        "Carbon config has no valid modules object. Using defaults."
                    );
                    notificationManager.warning(
                        "Config Reset",
                        "Missing module data; defaults were restored."
                    );
                } else {
                    for (Module module : moduleManager.getModules()) {
                        loadModule(
                            module,
                            getObject(modulesObject, module.getName())
                        );
                    }
                    logger.info(
                        "Loaded Carbon config from {}",
                        configFile.getAbsolutePath()
                    );
                }
            }
        } catch (Exception exception) {
            logger.warn(
                "Could not load Carbon config. Default values will be used.",
                exception
            );
            moduleManager.resetAllToDefaults();
            notificationManager.error(
                "Config Load Failed",
                "Defaults were restored safely."
            );
        }

        save();
    }

    public synchronized void save() {
        try {
            if (!configDirectory.exists() && !configDirectory.mkdirs()) {
                throw new IllegalStateException(
                    "Could not create config directory: " + configDirectory
                );
            }

            writeAtomically(createSnapshot());
            logger.info("Saved Carbon config to {}", configFile.getAbsolutePath());
        } catch (Exception exception) {
            logger.warn("Could not save Carbon config.", exception);
            notificationManager.error(
                "Config Save Failed",
                "Changes could not be written to disk."
            );
        }
    }

    public synchronized JsonObject createSnapshot() {
        JsonObject root = new JsonObject();
        JsonObject modulesObject = new JsonObject();

        for (Module module : moduleManager.getModules()) {
            modulesObject.add(module.getName(), serializeModule(module));
        }

        root.add("modules", modulesObject);
        return root;
    }

    public synchronized JsonObject createDefaultSnapshot() {
        JsonObject root = new JsonObject();
        JsonObject modulesObject = new JsonObject();

        for (Module module : moduleManager.getModules()) {
            modulesObject.add(module.getName(), serializeDefaultModule(module));
        }

        root.add("modules", modulesObject);
        return root;
    }

    public synchronized void applySnapshot(JsonObject root) {
        if (root == null) {
            throw new IllegalArgumentException("Config snapshot cannot be null.");
        }

        JsonObject modulesObject = getObject(root, "modules");
        if (modulesObject == null) {
            throw new IllegalArgumentException(
                "Config snapshot has no valid modules object."
            );
        }

        moduleManager.resetAllToDefaults();
        for (Module module : moduleManager.getModules()) {
            loadModule(module, getObject(modulesObject, module.getName()));
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
        if (value == null) {
            return;
        }

        try {
            if (setting instanceof ColorSetting) {
                loadColorSetting((ColorSetting) setting, value);
            } else if (!value.isJsonPrimitive()) {
                return;
            } else if (setting instanceof BooleanSetting) {
                ((BooleanSetting) setting).setValue(value.getAsBoolean());
            } else if (setting instanceof KeybindSetting) {
                ((KeybindSetting) setting).setValue(value.getAsInt());
            } else if (setting instanceof NumberSetting) {
                ((NumberSetting) setting).setValue(value.getAsDouble());
            } else if (setting instanceof ModeSetting) {
                ((ModeSetting) setting).setValue(value.getAsString());
            }
        } catch (Exception exception) {
            setting.reset();
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
            if (setting instanceof ColorSetting) {
                settingsObject.add(
                    setting.getName(),
                    serializeColorSetting((ColorSetting) setting)
                );
            } else {
                addSettingValue(settingsObject, setting);
            }
        }

        moduleObject.add("settings", settingsObject);
        return moduleObject;
    }

    private JsonObject serializeDefaultModule(Module module) {
        JsonObject moduleObject = new JsonObject();
        JsonObject settingsObject = new JsonObject();

        moduleObject.addProperty("enabled", module.isDefaultEnabled());
        moduleObject.addProperty("keyCode", module.getDefaultKeyCode());

        for (Setting<?> setting : module.getSettings()) {
            if (setting instanceof ColorSetting) {
                settingsObject.add(
                    setting.getName(),
                    serializeDefaultColorSetting((ColorSetting) setting)
                );
            } else {
                addDefaultSettingValue(settingsObject, setting);
            }
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
        }
    }

    private void addDefaultSettingValue(
        JsonObject settingsObject,
        Setting<?> setting
    ) {
        if (setting instanceof BooleanSetting) {
            settingsObject.addProperty(
                setting.getName(),
                (Boolean) setting.getDefaultValue()
            );
        } else if (setting instanceof KeybindSetting) {
            settingsObject.addProperty(
                setting.getName(),
                (Integer) setting.getDefaultValue()
            );
        } else if (setting instanceof NumberSetting) {
            settingsObject.addProperty(
                setting.getName(),
                (Double) setting.getDefaultValue()
            );
        } else if (setting instanceof ModeSetting) {
            settingsObject.addProperty(
                setting.getName(),
                (String) setting.getDefaultValue()
            );
        }
    }

    private void loadColorSetting(ColorSetting setting, JsonElement value) {
        if (value.isJsonPrimitive()) {
            setting.setBaseColor(value.getAsInt());
            return;
        }
        if (!value.isJsonObject()) {
            return;
        }

        JsonObject colorObject = value.getAsJsonObject();
        JsonElement color = colorObject.get("color");
        if (color != null && color.isJsonPrimitive()) {
            setting.setBaseColor(color.getAsInt());
        }
        JsonElement hue = colorObject.get("hue");
        JsonElement saturation = colorObject.get("saturation");
        JsonElement brightness = colorObject.get("brightness");
        JsonElement alpha = colorObject.get("alpha");
        JsonElement chroma = colorObject.get("chroma");
        JsonElement type = colorObject.get("type");
        JsonElement speed = colorObject.get("speed");

        if (hue != null) {
            setting.setHue(hue.getAsFloat());
        }
        if (saturation != null) {
            setting.setSaturation(saturation.getAsFloat());
        }
        if (brightness != null) {
            setting.setBrightness(brightness.getAsFloat());
        }
        if (alpha != null) {
            setting.setAlpha(alpha.getAsFloat());
        }
        if (chroma != null) {
            setting.setChroma(chroma.getAsBoolean());
        }
        if (type != null) {
            setting.setType(type.getAsString());
        }
        if (speed != null) {
            setting.setSpeed(speed.getAsDouble());
        }
    }

    private JsonObject serializeColorSetting(ColorSetting setting) {
        JsonObject colorObject = new JsonObject();
        colorObject.addProperty("color", setting.getBaseColor());
        colorObject.addProperty("hue", setting.getHue());
        colorObject.addProperty("saturation", setting.getSaturation());
        colorObject.addProperty("brightness", setting.getBrightness());
        colorObject.addProperty("alpha", setting.getAlpha());
        colorObject.addProperty("chroma", setting.isChroma());
        colorObject.addProperty("type", setting.getType());
        colorObject.addProperty("speed", setting.getSpeed());
        return colorObject;
    }

    private JsonObject serializeDefaultColorSetting(ColorSetting setting) {
        JsonObject colorObject = new JsonObject();
        int defaultColor = setting.getDefaultValue();
        float[] hsb = java.awt.Color.RGBtoHSB(
            defaultColor >> 16 & 0xFF,
            defaultColor >> 8 & 0xFF,
            defaultColor & 0xFF,
            null
        );

        colorObject.addProperty("color", defaultColor);
        colorObject.addProperty("hue", hsb[0]);
        colorObject.addProperty("saturation", hsb[1]);
        colorObject.addProperty("brightness", hsb[2]);
        colorObject.addProperty(
            "alpha",
            (defaultColor >>> 24 & 0xFF) / 255.0F
        );
        colorObject.addProperty("chroma", false);
        colorObject.addProperty("type", ColorSetting.TYPE_STATIC);
        colorObject.addProperty("speed", 1.0D);
        return colorObject;
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
