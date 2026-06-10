package com.carbonclient;

import com.carbonclient.client.Client;
import com.carbonclient.common.Reference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
    modid = Reference.MOD_ID,
    name = Reference.MOD_NAME,
    version = Reference.VERSION,
    clientSideOnly = true,
    acceptedMinecraftVersions = "[1.8.9]"
)
public final class CarbonClient {

    @Mod.Instance(Reference.MOD_ID)
    public static CarbonClient instance;

    private final Client client = new Client();

    @Mod.EventHandler
    public void preInitialize(FMLPreInitializationEvent event) {
        client.preInitialize(event);
    }

    @Mod.EventHandler
    public void initialize(FMLInitializationEvent event) {
        client.initialize(event);
    }

    @Mod.EventHandler
    public void postInitialize(FMLPostInitializationEvent event) {
        client.postInitialize(event);
    }

    public Client getClient() {
        return client;
    }
}
