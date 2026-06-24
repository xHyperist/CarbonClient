package com.carbonclient.modules.render;

import com.carbonclient.bridge.api.render.RenderBridge;
import com.carbonclient.bridge.render.RenderBridgeAccess;
import com.carbonclient.event.EventListener;
import com.carbonclient.event.impl.MouseButtonEvent;
import com.carbonclient.event.impl.Render2DEvent;
import com.carbonclient.module.DraggableHudModule;
import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleCategory;
import com.carbonclient.setting.impl.BooleanSetting;
import com.carbonclient.setting.impl.ColorSetting;
import com.carbonclient.setting.impl.ModeSetting;
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
    private static final String STYLE_MODERN = "Modern";
    private static final String STYLE_CLASSIC = "Classic";
    private static final String STYLE_MINIMAL = "Minimal";

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final BooleanSetting showBackground =
        addSetting(new BooleanSetting("Show Background", true));
    private final ModeSetting styleMode =
        addSetting(
            new ModeSetting(
                "Style Mode",
                STYLE_MODERN,
                STYLE_MODERN,
                STYLE_CLASSIC,
                STYLE_MINIMAL
            )
        );
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
            ModuleCategory.HUD,
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
        if (renderWithBridge(text)) {
            return;
        }
        renderLegacy(text);
    }

    private boolean renderWithBridge(String text) {
        RenderBridge renderBridge = RenderBridgeAccess.getIfReady();
        if (renderBridge == null) {
            return false;
        }

        boolean matrixPushed = false;
        try {
            int width = RenderBridgeAccess.safeStringWidth(renderBridge, text);
            int height = RenderBridgeAccess.safeFontHeight(renderBridge);
            if (width <= 0 || height <= 0) {
                return false;
            }

            float renderScale = scale.getValue().floatValue();
            int renderX = Math.round(getPositionX() / renderScale);
            int renderY = Math.round(getPositionY() / renderScale);
            int padding = getPadding();

            GlStateManager.pushMatrix();
            matrixPushed = true;
            GlStateManager.scale(renderScale, renderScale, 1.0F);

            if (showBackground.isEnabled()) {
                renderBridge.drawRect(
                    renderX,
                    renderY,
                    renderX + width + padding * 2,
                    renderY + height + padding * 2,
                    backgroundColor.getColor()
                );
            }
            renderBridge.drawText(
                text,
                renderX + padding,
                renderY + padding,
                textColor.getColor(),
                false
            );
            return true;
        } catch (RuntimeException exception) {
            return false;
        } finally {
            if (matrixPushed) {
                GlStateManager.popMatrix();
            }
        }
    }

    private void renderLegacy(String text) {
        int width = minecraft.fontRendererObj.getStringWidth(text);
        int height = minecraft.fontRendererObj.FONT_HEIGHT;
        float renderScale = scale.getValue().floatValue();
        int renderX = Math.round(getPositionX() / renderScale);
        int renderY = Math.round(getPositionY() / renderScale);
        int padding = getPadding();

        GlStateManager.pushMatrix();
        GlStateManager.scale(renderScale, renderScale, 1.0F);

        if (showBackground.isEnabled()) {
            Gui.drawRect(
                renderX,
                renderY,
                renderX + width + padding * 2,
                renderY + height + padding * 2,
                backgroundColor.getColor()
            );
        }
        minecraft.fontRendererObj.drawString(
            text,
            renderX + padding,
            renderY + padding,
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
        int padding = getPadding();
        return Math.round(
            (minecraft.fontRendererObj.getStringWidth(text) + padding * 2)
                * scale.getValue().floatValue()
        );
    }

    @Override
    public int getHudHeight() {
        int padding = getPadding();
        return Math.round(
            (minecraft.fontRendererObj.FONT_HEIGHT + padding * 2)
                * scale.getValue().floatValue()
        );
    }

    private int getPadding() {
        if (!showBackground.isEnabled()) {
            return 0;
        }
        if (STYLE_MINIMAL.equals(styleMode.getValue())) {
            return 1;
        }
        if (STYLE_CLASSIC.equals(styleMode.getValue())) {
            return PADDING;
        }
        return 4;
    }

    private void removeExpiredClicks(Deque<Long> clicks, long now) {
        while (!clicks.isEmpty() && now - clicks.peekFirst() >= CPS_WINDOW_MILLIS) {
            clicks.removeFirst();
        }
    }
}
