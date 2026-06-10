package com.carbonclient.modules.render;

import com.carbonclient.event.EventListener;
import com.carbonclient.event.impl.Render2DEvent;
import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleCategory;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public final class FPSDisplayModule extends Module {

    private static final int TEXT_COLOR = 0xFFFFFF;
    private static final int X = 5;
    private static final int Y = 5;

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final EventListener<Render2DEvent> renderListener =
        new EventListener<Render2DEvent>() {
            @Override
            public void onEvent(Render2DEvent event) {
                renderFps();
            }
        };

    public FPSDisplayModule() {
        super(
            "FPS Display",
            "Displays the current FPS in the top-left corner.",
            ModuleCategory.RENDER
        );
        setKeyCode(Keyboard.KEY_F);
    }

    @Override
    protected void onEnable() {
        subscribe(Render2DEvent.class, renderListener);
    }

    @Override
    protected void onDisable() {
        unsubscribe(Render2DEvent.class, renderListener);
    }

    private void renderFps() {
        String text = "FPS: " + Minecraft.getDebugFPS();
        minecraft.fontRendererObj.drawStringWithShadow(text, X, Y, TEXT_COLOR);
    }
}
