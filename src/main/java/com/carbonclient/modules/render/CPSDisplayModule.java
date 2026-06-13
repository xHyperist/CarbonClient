package com.carbonclient.modules.render;

import com.carbonclient.event.EventListener;
import com.carbonclient.event.impl.MouseButtonEvent;
import com.carbonclient.event.impl.Render2DEvent;
import com.carbonclient.module.DraggableHudModule;
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

public final class CPSDisplayModule extends Module implements DraggableHudModule {

    private static final long CPS_WINDOW_MILLIS = 1000L;
    private static final int PADDING = 3;

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final BooleanSetting showBackground =
        addSetting(new BooleanSetting("Show Background", true));
    private final NumberSetting scale =
        addSetting(new NumberSetting("Scale", 1.0D, 0.5D, 2.0D, 0.1D));
    private final NumberSetting positionX =
        addHiddenSetting(
            new NumberSetting("Position X", 5.0D, 0.0D, 10000.0D, 1.0D)
        );
    private final NumberSetting positionY =
        addHiddenSetting(
            new NumberSetting("Position Y", 16.0D, 0.0D, 10000.0D, 1.0D)
        );
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
                renderHud();
            }
        };

    public CPSDisplayModule() {
        super(
            "CPS Display",
            "Displays left mouse clicks per second.",
            ModuleCategory.RENDER,
            true,
            Keyboard.KEY_C
        );
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

    @Override
    public void renderHud() {
        long now = System.currentTimeMillis();
        removeExpiredClicks(leftClicks, now);

        String text = leftClicks.size() + " CPS";
        int width = minecraft.fontRendererObj.getStringWidth(text);
        int height = minecraft.fontRendererObj.FONT_HEIGHT;
        float renderScale = scale.getValue().floatValue();
        int renderX = Math.round(getPositionX() / renderScale);
        int renderY = Math.round(getPositionY() / renderScale);

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

    @Override
    public int getPositionX() {
        return positionX.getValue().intValue();
    }

    @Override
    public int getPositionY() {
        return positionY.getValue().intValue();
    }

    @Override
    public void setPosition(int x, int y) {
        positionX.setValue((double) Math.max(0, x));
        positionY.setValue((double) Math.max(0, y));
    }

    @Override
    public int getHudWidth() {
        String text = leftClicks.size() + " CPS";
        return Math.round(
            (minecraft.fontRendererObj.getStringWidth(text) + PADDING * 2)
                * scale.getValue().floatValue()
        );
    }

    @Override
    public int getHudHeight() {
        return Math.round(
            (minecraft.fontRendererObj.FONT_HEIGHT + PADDING * 2)
                * scale.getValue().floatValue()
        );
    }

    private void removeExpiredClicks(Deque<Long> clicks, long now) {
        while (!clicks.isEmpty() && now - clicks.peekFirst() >= CPS_WINDOW_MILLIS) {
            clicks.removeFirst();
        }
    }
}
