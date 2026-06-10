package com.carbonclient.event.bridge;

import com.carbonclient.event.EventBus;
import com.carbonclient.event.impl.Render2DEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class ForgeEventBridge {

    private final EventBus eventBus;

    public ForgeEventBridge(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        eventBus.post(new Render2DEvent(event.partialTicks));
    }
}
