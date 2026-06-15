package com.carbonclient.event.bridge;

import com.carbonclient.event.EventBus;
import com.carbonclient.event.impl.ClientTickEvent;
import com.carbonclient.event.impl.CrosshairRenderEvent;
import com.carbonclient.event.impl.MouseButtonEvent;
import com.carbonclient.event.impl.Render2DEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
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
    public void onCrosshairRender(RenderGameOverlayEvent.Pre event) {
        if (event.type != ElementType.CROSSHAIRS) {
            return;
        }

        CrosshairRenderEvent carbonEvent = new CrosshairRenderEvent(
            event.resolution.getScaledWidth(),
            event.resolution.getScaledHeight(),
            event.partialTicks
        );
        eventBus.post(carbonEvent);
        if (carbonEvent.shouldHideVanilla()) {
            event.setCanceled(true);
        }
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
