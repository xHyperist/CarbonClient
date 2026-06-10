package com.carbonclient.modules.render;

import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleCategory;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class FPSDisplayModule extends Module {

    private static final int TEXT_COLOR = 0xFFFFFF;
    private static final int X = 5;
    private static final int Y = 5;

    private final Minecraft minecraft = Minecraft.getMinecraft();

    public FPSDisplayModule() {
        super(
            "FPS Display",
            "Displays the current FPS in the top-left corner.",
            ModuleCategory.RENDER
        );
    }

    @Override
    protected void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        if (!isEnabled()) {
            return;
        }

        String text = "FPS: " + Minecraft.getDebugFPS();
        minecraft.fontRendererObj.drawStringWithShadow(text, X, Y, TEXT_COLOR);
    }
}
