package com.carbonclient.client;

import com.carbonclient.common.Reference;
import com.carbonclient.module.ModuleManager;
import com.carbonclient.module.impl.ExampleModule;
import com.carbonclient.modules.render.FPSDisplayModule;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

public final class Client {

    private final ModuleManager moduleManager = new ModuleManager();
    private Logger logger;

    public void preInitialize(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("{} v{} is starting.", Reference.MOD_NAME, Reference.VERSION);
    }

    public void initialize(FMLInitializationEvent event) {
        moduleManager.register(new ExampleModule());
        FPSDisplayModule fpsDisplay = new FPSDisplayModule();
        moduleManager.register(fpsDisplay);
        fpsDisplay.setEnabled(true);
        logger.info(
            "Registered module: {} (enabled: {})",
            fpsDisplay.getName(),
            fpsDisplay.isEnabled()
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
}
