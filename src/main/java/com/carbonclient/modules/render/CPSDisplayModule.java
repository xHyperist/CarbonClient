package com.carbonclient.modules.render;

import com.carbonclient.event.EventListener;
import com.carbonclient.event.impl.MouseButtonEvent;
import com.carbonclient.event.impl.Render2DEvent;
import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleCategory;
import com.carbonclient.setting.impl.BooleanSetting;
import com.carbonclient.setting.impl.ColorSetting;
import com.carbonclient.setting.impl.NumberSetting;
import java.util.ArrayDeque;
import java.util.Deque;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

public final class CPSDisplayModule extends Module {

    private static final long CPS_WINDOW_MILLIS = 1000L;
    private static final int PADDING = 3;
    private static final int X = 5;
    private static final int Y = 16;

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final BooleanSetting showBackground =
        addSetting(new BooleanSetting("Show Background", true));
    private final NumberSetting scale =
        addSetting(new NumberSetting("Scale", 1.0D, 0.5D, 2.0D, 0.1D));
    private final ColorSetting textColor =
        addSetting(new ColorSetting("Text Color", 0xFFFFFFFF));
    private final ColorSetting backgroundColor =
        addSetting(new ColorSetting("Background Color", 0xB0121824));
    private final Deque<Long> leftClicks = new ArrayDeque<Long>();
    private final EventListener<MouseButtonEvent> mouseListener =
        new EventListener<MouseButtonEvent>() {
            @Override
            public void onEvent(MouseButtonEvent event) {
                recordClick(event.getButton());
            }
        };
    private final EventListener<Render2DEvent> renderListener =
        new EventListener<Render2DEvent>() {
            @Override
            public void onEvent(Render2DEvent event) {
                renderCps();
            }
        };

    public CPSDisplayModule() {
        super(
            "CPS Display",
            "Displays left mouse clicks per second.",
            ModuleCategory.RENDER
        );
        setKeyCode(Keyboard.KEY_C);
    }

    @Override
    protected void onEnable() {
        subscribe(MouseButtonEvent.class, mouseListener);
        subscribe(Render2DEvent.class, renderListener);
    }

    @Override
    protected void onDisable() {
        unsubscribe(MouseButtonEvent.class, mouseListener);
        unsubscribe(Render2DEvent.class, renderListener);
        leftClicks.clear();
    }

    private void recordClick(int button) {
        if (button == 0) {
            leftClicks.addLast(System.currentTimeMillis());
        }
    }

    private void renderCps() {
        long now = System.currentTimeMillis();
        removeExpiredClicks(leftClicks, now);

        String text = leftClicks.size() + " CPS";
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

    private void removeExpiredClicks(Deque<Long> clicks, long now) {
        while (!clicks.isEmpty() && now - clicks.peekFirst() >= CPS_WINDOW_MILLIS) {
            clicks.removeFirst();
        }
    }
}
