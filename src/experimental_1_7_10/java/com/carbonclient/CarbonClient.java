package com.carbonclient;

import com.carbonclient.common.Reference;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = Reference.MOD_ID,
    name = Reference.MOD_NAME,
    version = Reference.VERSION,
    acceptedMinecraftVersions = "[1.7.10]"
)
public final class CarbonClient {

    private Logger logger;

    @Mod.EventHandler
    public void preInitialize(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info(
            "{} {} 1.7.10 experimental bootstrap preInit.",
            Reference.MOD_NAME,
            Reference.VERSION
        );
    }

    @Mod.EventHandler
    public void initialize(FMLInitializationEvent event) {
        logger.info("{} 1.7.10 experimental bootstrap init.", Reference.MOD_NAME);
    }

    @Mod.EventHandler
    public void postInitialize(FMLPostInitializationEvent event) {
        logger.info(
            "{} 1.7.10 experimental bootstrap postInit.",
            Reference.MOD_NAME
        );
    }
}
