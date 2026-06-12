package com.carbonclient.modules.render;

import com.carbonclient.event.EventListener;
import com.carbonclient.event.impl.Render2DEvent;
import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleCategory;
import com.carbonclient.setting.impl.BooleanSetting;
import com.carbonclient.setting.impl.ColorSetting;
import com.carbonclient.setting.impl.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

public final class FPSDisplayModule extends Module {

    private static final int PADDING = 3;
    private static final int X = 5;
    private static final int Y = 0;

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final BooleanSetting showBackground =
        addSetting(new BooleanSetting("Show Background", true));
    private final NumberSetting scale =
        addSetting(new NumberSetting("Scale", 1.0D, 0.5D, 2.0D, 0.1D));
    private final ColorSetting textColor =
        addSetting(new ColorSetting("Text Color", 0xFFFFFFFF));
    private final ColorSetting backgroundColor =
        addSetting(new ColorSetting("Background Color", 0xB0121824));
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
        String text = Minecraft.getDebugFPS() + " FPS";
        int width = minecraft.fontRendererObj.getStringWidth(text);
        int height = minecraft.fontRendererObj.FONT_HEIGHT;
        float renderScale = scale.getValue().floatValue();
        int renderX = Math.round(X / renderScale);
        int renderY = Math.round(Y / renderScale);

        GlStateManager.pushMatrix();
        GlStateManager.scale(renderScale, renderScale, 1.0F);

        if (showBackground.isEnabled()) {
            Gui.drawRect(
                renderX,
                renderY,
                renderX + width + PADDING * 2,
                renderY + height + PADDING * 2,
                backgroundColor.getColor()
            );
        }
        minecraft.fontRendererObj.drawString(
            text,
            renderX + PADDING,
            renderY + PADDING,
            textColor.getColor()
        );

        GlStateManager.popMatrix();
    }
}
