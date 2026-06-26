package com.carbonclient.hud;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public final class ClockHudOverlay {

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        if (event.type != ElementType.TEXT) {
            return;
        }

        if (minecraft == null || minecraft.fontRendererObj == null) {
            return;
        }

        String text = "Carbon 1.7.10 | " + timeFormat.format(new Date());
        minecraft.fontRendererObj.drawStringWithShadow(text, 4, 4, 0xFFFFFFFF);
    }
}
