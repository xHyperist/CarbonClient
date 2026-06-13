package com.carbonclient.client;

import com.carbonclient.common.Reference;
import com.carbonclient.config.ConfigManager;
import com.carbonclient.client.service.ServiceRegistry;
import com.carbonclient.event.EventBus;
import com.carbonclient.event.bridge.ForgeEventBridge;
import com.carbonclient.input.KeyInputHandler;
import com.carbonclient.module.ModuleManager;
import com.carbonclient.module.impl.ExampleModule;
import com.carbonclient.modules.movement.ToggleSprintModule;
import com.carbonclient.modules.render.CPSDisplayModule;
import com.carbonclient.modules.render.FPSDisplayModule;
import com.carbonclient.modules.render.KeystrokesModule;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

public final class Client {

    private final ServiceRegistry serviceRegistry = new ServiceRegistry();
    private final EventBus eventBus = new EventBus();
    private final ModuleManager moduleManager = new ModuleManager(eventBus);
    private final KeyInputHandler keyInputHandler = new KeyInputHandler(moduleManager);
    private final ForgeEventBridge forgeEventBridge = new ForgeEventBridge(eventBus);
    private ConfigManager configManager;
    private Logger logger;

    public void preInitialize(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        configManager = new ConfigManager(
            event.getModConfigurationDirectory().getParentFile(),
            moduleManager,
            logger
        );
        keyInputHandler.setConfigManager(configManager);
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
        configManager.load();
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
