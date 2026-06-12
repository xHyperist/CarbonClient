package com.carbonclient.event.bridge;

import com.carbonclient.event.EventBus;
import com.carbonclient.event.impl.ClientTickEvent;
import com.carbonclient.event.impl.MouseButtonEvent;
import com.carbonclient.event.impl.Render2DEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import org.lwjgl.input.Mouse;

public final class ForgeEventBridge {

    private final EventBus eventBus;

    public ForgeEventBridge(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        eventBus.post(new Render2DEvent(event.partialTicks));
    }

    @SubscribeEvent
    public void onMouseInput(MouseInputEvent event) {
        if (!Mouse.getEventButtonState()) {
            return;
        }
        if (Minecraft.getMinecraft().currentScreen != null) {
            return;
        }

        int button = Mouse.getEventButton();
        if (button == 0 || button == 1) {
            eventBus.post(new MouseButtonEvent(button));
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == Phase.END) {
            eventBus.post(new ClientTickEvent());
        }
    }
}
