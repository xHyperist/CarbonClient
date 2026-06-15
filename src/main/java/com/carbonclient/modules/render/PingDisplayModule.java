package com.carbonclient.modules.render;

import com.carbonclient.event.EventListener;
import com.carbonclient.event.impl.Render2DEvent;
import com.carbonclient.module.DraggableHudModule;
import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleCategory;
import com.carbonclient.setting.impl.BooleanSetting;
import com.carbonclient.setting.impl.ColorSetting;
import com.carbonclient.setting.impl.ModeSetting;
import com.carbonclient.setting.impl.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

public final class PingDisplayModule
    extends Module
    implements DraggableHudModule {

    private static final int PADDING = 3;

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final BooleanSetting showBackground = addSetting(
        new BooleanSetting("Show Background", true)
    );
    private final ModeSetting format = addSetting(
        new ModeSetting("Format", "Compact", "Compact", "Label")
    );
    private final NumberSetting scale = addSetting(
        new NumberSetting("Scale", 1.0D, 0.5D, 2.0D, 0.1D)
    );
    private final ColorSetting textColor = addSetting(
        new ColorSetting("Text Color", 0xFFFFFFFF)
    );
    private final ColorSetting backgroundColor = addSetting(
        new ColorSetting("Background Color", 0xB0121824)
    );
    private final ColorSetting goodPingColor = addSetting(
        new ColorSetting("Good Ping Color", 0xFF6EE7A8)
    );
    private final ColorSetting mediumPingColor = addSetting(
        new ColorSetting("Medium Ping Color", 0xFFFFC857)
    );
    private final ColorSetting badPingColor = addSetting(
        new ColorSetting("Bad Ping Color", 0xFFFF4F6D)
    );
    private final BooleanSetting colorByPing = addSetting(
        new BooleanSetting("Color By Ping", true)
    );
    private final NumberSetting mediumThreshold = addSetting(
        new NumberSetting("Medium Threshold", 100.0D, 1.0D, 1000.0D, 1.0D)
    );
    private final NumberSetting badThreshold = addSetting(
        new NumberSetting("Bad Threshold", 200.0D, 1.0D, 2000.0D, 1.0D)
    );
    private final NumberSetting positionX = addHiddenSetting(
        new NumberSetting("Position X", 5.0D, 0.0D, 10000.0D, 1.0D)
    );
    private final NumberSetting positionY = addHiddenSetting(
        new NumberSetting("Position Y", 18.0D, 0.0D, 10000.0D, 1.0D)
    );
    private final EventListener<Render2DEvent> renderListener =
        new EventListener<Render2DEvent>() {
            @Override
            public void onEvent(Render2DEvent event) {
                renderHud();
            }
        };

    public PingDisplayModule() {
        super(
            "Ping Display",
            "Displays the current multiplayer server latency.",
            ModuleCategory.RENDER,
            true,
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
        int ping = getPing();
        String text = getDisplayText(ping);
        int width = minecraft.fontRendererObj.getStringWidth(text);
        int height = minecraft.fontRendererObj.FONT_HEIGHT;
        int padding = showBackground.isEnabled() ? PADDING : 0;
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
            renderX + padding,
            renderY + padding,
            getTextColor(ping)
        );

        GlStateManager.popMatrix();
    }

    private int getPing() {
        if (minecraft.isSingleplayer()
            || minecraft.thePlayer == null
            || minecraft.getNetHandler() == null) {
            return -1;
        }

        NetworkPlayerInfo playerInfo = minecraft
            .getNetHandler()
            .getPlayerInfo(minecraft.thePlayer.getUniqueID());
        if (playerInfo == null || playerInfo.getResponseTime() < 0) {
            return -1;
        }
        return playerInfo.getResponseTime();
    }

    private String getDisplayText(int ping) {
        String value = ping < 0 ? "N/A" : ping + " ms";
        return "Label".equals(format.getValue())
            ? "Ping: " + value
            : value;
    }

    private int getTextColor(int ping) {
        if (!colorByPing.isEnabled() || ping < 0) {
            return textColor.getColor();
        }
        double medium = Math.min(
            mediumThreshold.getValue(),
            badThreshold.getValue()
        );
        double bad = Math.max(
            mediumThreshold.getValue(),
            badThreshold.getValue()
        );
        if (ping >= bad) {
            return badPingColor.getColor();
        }
        if (ping >= medium) {
            return mediumPingColor.getColor();
        }
        return goodPingColor.getColor();
    }

    private int getUnscaledWidth() {
        int width = minecraft.fontRendererObj.getStringWidth(
            getDisplayText(getPing())
        );
        return width + (showBackground.isEnabled() ? PADDING * 2 : 0);
    }

    private int getUnscaledHeight() {
        return minecraft.fontRendererObj.FONT_HEIGHT
            + (showBackground.isEnabled() ? PADDING * 2 : 0);
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
        return Math.round(
            getUnscaledWidth() * scale.getValue().floatValue()
        );
    }

    @Override
    public int getHudHeight() {
        return Math.round(
            getUnscaledHeight() * scale.getValue().floatValue()
        );
    }
}
