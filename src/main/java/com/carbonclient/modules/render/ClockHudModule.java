package com.carbonclient.modules.render;

import com.carbonclient.bridge.api.render.RenderBridge;
import com.carbonclient.bridge.render.RenderBridgeAccess;
import com.carbonclient.event.EventListener;
import com.carbonclient.event.impl.Render2DEvent;
import com.carbonclient.module.DraggableHudModule;
import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleCategory;
import com.carbonclient.setting.impl.BooleanSetting;
import com.carbonclient.setting.impl.ColorSetting;
import com.carbonclient.setting.impl.ModeSetting;
import com.carbonclient.setting.impl.NumberSetting;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

public final class ClockHudModule extends Module implements DraggableHudModule {

    private static final int PADDING = 3;
    private static final String FORMAT_24H = "24H";
    private static final String FORMAT_12H = "12H";
    private static final DateTimeFormatter FORMATTER_24H_MINUTES =
        DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter FORMATTER_24H_SECONDS =
        DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter FORMATTER_12H_MINUTES =
        DateTimeFormatter.ofPattern("hh:mm a");
    private static final DateTimeFormatter FORMATTER_12H_SECONDS =
        DateTimeFormatter.ofPattern("hh:mm:ss a");

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final BooleanSetting showSeconds = addSetting(
        new BooleanSetting("Show Seconds", false)
    );
    private final ModeSetting timeFormat = addSetting(
        new ModeSetting("Time Format", FORMAT_24H, FORMAT_24H, FORMAT_12H)
    );
    private final BooleanSetting showPrefix = addSetting(
        new BooleanSetting("Show Prefix", false)
    );
    private final ModeSetting prefixText = addSetting(
        new ModeSetting("Prefix Text", "Time", "Clock", "Time", "Saat")
    );
    private final BooleanSetting showBackground = addSetting(
        new BooleanSetting("Show Background", true)
    );
    private final NumberSetting scale = addSetting(
        new NumberSetting("Scale", 1.0D, 0.5D, 2.0D, 0.05D)
    );
    private final ColorSetting textColor = addSetting(
        new ColorSetting("Text Color", 0xFFFFFFFF)
    );
    private final ColorSetting backgroundColor = addSetting(
        new ColorSetting("Background Color", 0x6F000000)
    );
    private final NumberSetting positionX = addHiddenSetting(
        new NumberSetting("Position X", 5.0D, 0.0D, 10000.0D, 1.0D)
    );
    private final NumberSetting positionY = addHiddenSetting(
        new NumberSetting("Position Y", 108.0D, 0.0D, 10000.0D, 1.0D)
    );
    private final EventListener<Render2DEvent> renderListener =
        new EventListener<Render2DEvent>() {
            @Override
            public void onEvent(Render2DEvent event) {
                renderHud();
            }
        };

    public ClockHudModule() {
        super(
            "Clock HUD",
            "Displays the current local time. Saat.",
            ModuleCategory.HUD,
            false,
            Keyboard.KEY_NONE
        );
    }

    @Override
    protected void onEnable() {
        subscribe(Render2DEvent.class, renderListener);
    }

    @Override
    protected void onDisable() {
        unsubscribe(Render2DEvent.class, renderListener);
    }

    @Override
    public void renderHud() {
        String text = getDisplayText();
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
            int textWidth = RenderBridgeAccess.safeStringWidth(renderBridge, text);
            int textHeight = RenderBridgeAccess.safeFontHeight(renderBridge);
            if (textWidth <= 0 || textHeight <= 0) {
                return false;
            }

            int padding = getPadding();
            float renderScale = scale.getValue().floatValue();
            int renderX = Math.round(getPositionX() / renderScale);
            int renderY = Math.round(getPositionY() / renderScale);

            GlStateManager.pushMatrix();
            matrixPushed = true;
            GlStateManager.scale(renderScale, renderScale, 1.0F);

            if (showBackground.isEnabled()) {
                renderBridge.drawRect(
                    renderX,
                    renderY,
                    renderX + textWidth + padding * 2,
                    renderY + textHeight + padding * 2,
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
        int padding = getPadding();
        int textWidth = minecraft.fontRendererObj.getStringWidth(text);
        int textHeight = minecraft.fontRendererObj.FONT_HEIGHT;
        float renderScale = scale.getValue().floatValue();
        int renderX = Math.round(getPositionX() / renderScale);
        int renderY = Math.round(getPositionY() / renderScale);

        GlStateManager.pushMatrix();
        GlStateManager.scale(renderScale, renderScale, 1.0F);

        if (showBackground.isEnabled()) {
            Gui.drawRect(
                renderX,
                renderY,
                renderX + textWidth + padding * 2,
                renderY + textHeight + padding * 2,
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
        int padding = getPadding();
        return Math.round(
            (minecraft.fontRendererObj.getStringWidth(getDisplayText())
                + padding * 2)
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

    private String getDisplayText() {
        String time = LocalTime.now().format(getFormatter());
        if (!showPrefix.isEnabled()) {
            return time;
        }

        return prefixText.getValue() + ": " + time;
    }

    private DateTimeFormatter getFormatter() {
        if (FORMAT_12H.equals(timeFormat.getValue())) {
            return showSeconds.isEnabled()
                ? FORMATTER_12H_SECONDS
                : FORMATTER_12H_MINUTES;
        }

        return showSeconds.isEnabled()
            ? FORMATTER_24H_SECONDS
            : FORMATTER_24H_MINUTES;
    }

    private int getPadding() {
        return showBackground.isEnabled() ? PADDING : 0;
    }
}
