package com.carbonclient.client;

import com.carbonclient.common.Reference;
import com.carbonclient.config.ConfigManager;
import com.carbonclient.client.service.ServiceRegistry;
import com.carbonclient.event.EventBus;
import com.carbonclient.event.bridge.ForgeEventBridge;
import com.carbonclient.input.KeyInputHandler;
import com.carbonclient.module.ModuleManager;
import com.carbonclient.module.impl.ExampleModule;
import com.carbonclient.notification.NotificationManager;
import com.carbonclient.notification.NotificationRenderer;
import com.carbonclient.profile.ProfileManager;
import com.carbonclient.profile.ProfileStorage;
import com.carbonclient.modules.movement.ToggleSprintModule;
import com.carbonclient.modules.render.ArmorHudModule;
import com.carbonclient.modules.render.CPSDisplayModule;
import com.carbonclient.modules.render.CoordinatesHudModule;
import com.carbonclient.modules.render.CrosshairModule;
import com.carbonclient.modules.render.FPSDisplayModule;
import com.carbonclient.modules.render.KeystrokesModule;
import com.carbonclient.modules.render.PingDisplayModule;
import com.carbonclient.modules.render.PotionHudModule;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

public final class Client {

    private final ServiceRegistry serviceRegistry = new ServiceRegistry();
    private final EventBus eventBus = new EventBus();
    private final NotificationManager notificationManager =
        new NotificationManager();
    private final NotificationRenderer notificationRenderer =
        new NotificationRenderer(eventBus, notificationManager);
    private final ModuleManager moduleManager = new ModuleManager(eventBus);
    private final KeyInputHandler keyInputHandler = new KeyInputHandler(
        moduleManager,
        notificationManager,
        notificationRenderer
    );
    private final ForgeEventBridge forgeEventBridge = new ForgeEventBridge(eventBus);
    private ConfigManager configManager;
    private ProfileManager profileManager;
    private Logger logger;

    public void preInitialize(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        moduleManager.setNotificationManager(notificationManager);
        configManager = new ConfigManager(
            event.getModConfigurationDirectory().getParentFile(),
            moduleManager,
            logger,
            notificationManager
        );
        profileManager = new ProfileManager(
            new ProfileStorage(
                event.getModConfigurationDirectory().getParentFile()
            ),
            configManager,
            notificationManager,
            logger
        );
        keyInputHandler.setConfigManager(configManager);
        keyInputHandler.setProfileManager(profileManager);
        logger.info("{} v{} is starting.", Reference.MOD_NAME, Reference.VERSION);
    }

    public void initialize(FMLInitializationEvent event) {
        moduleManager.register(new ExampleModule());
        FPSDisplayModule fpsDisplay = new FPSDisplayModule();
        moduleManager.register(fpsDisplay);
        CPSDisplayModule cpsDisplay = new CPSDisplayModule();
        moduleManager.register(cpsDisplay);
        KeystrokesModule keystrokes = new KeystrokesModule();
        moduleManager.register(keystrokes);
        ToggleSprintModule toggleSprint = new ToggleSprintModule();
        moduleManager.register(toggleSprint);
        ArmorHudModule armorHud = new ArmorHudModule();
        moduleManager.register(armorHud);
        PotionHudModule potionHud = new PotionHudModule();
        moduleManager.register(potionHud);
        CoordinatesHudModule coordinatesHud = new CoordinatesHudModule();
        moduleManager.register(coordinatesHud);
        PingDisplayModule pingDisplay = new PingDisplayModule();
        moduleManager.register(pingDisplay);
        CrosshairModule crosshair = new CrosshairModule();
        moduleManager.register(crosshair);
        configManager.load();
        profileManager.initialize();
        registerConfigShutdownHook();
        MinecraftForge.EVENT_BUS.register(forgeEventBridge);
        FMLCommonHandler.instance().bus().register(keyInputHandler);
        logger.info(
            "Registered module: {} (enabled: {}, keyCode: {})",
            fpsDisplay.getName(),
            fpsDisplay.isEnabled(),
            fpsDisplay.getKeyCode()
        );
        logger.info(
            "Registered module: {} (enabled: {}, keyCode: {})",
            cpsDisplay.getName(),
            cpsDisplay.isEnabled(),
            cpsDisplay.getKeyCode()
        );
        logger.info(
            "Registered module: {} (enabled: {}, keyCode: {})",
            keystrokes.getName(),
            keystrokes.isEnabled(),
            keystrokes.getKeyCode()
        );
        logger.info(
            "Registered module: {} (enabled: {}, keyCode: {})",
            toggleSprint.getName(),
            toggleSprint.isEnabled(),
            toggleSprint.getKeyCode()
        );
        logger.info(
            "Registered module: {} (enabled: {}, keyCode: {})",
            armorHud.getName(),
            armorHud.isEnabled(),
            armorHud.getKeyCode()
        );
        logger.info(
            "Registered module: {} (enabled: {}, keyCode: {})",
            potionHud.getName(),
            potionHud.isEnabled(),
            potionHud.getKeyCode()
        );
        logger.info(
            "Registered module: {} (enabled: {}, keyCode: {})",
            coordinatesHud.getName(),
            coordinatesHud.isEnabled(),
            coordinatesHud.getKeyCode()
        );
        logger.info(
            "Registered module: {} (enabled: {}, keyCode: {})",
            pingDisplay.getName(),
            pingDisplay.isEnabled(),
            pingDisplay.getKeyCode()
        );
        logger.info(
            "Registered module: {} (enabled: {}, keyCode: {})",
            crosshair.getName(),
            crosshair.isEnabled(),
            crosshair.getKeyCode()
        );
        logger.info("{} initialized successfully.", Reference.MOD_NAME);
    }

    public void postInitialize(FMLPostInitializationEvent event) {
        logger.info("{} finished loading.", Reference.MOD_NAME);
    }

    public Logger getLogger() {
        return logger;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public NotificationRenderer getNotificationRenderer() {
        return notificationRenderer;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    private void registerConfigShutdownHook() {
        Runtime.getRuntime().addShutdownHook(
            new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        configManager.save();
                    }
                },
                "Carbon Client Config Saver"
            )
        );
    }
}
