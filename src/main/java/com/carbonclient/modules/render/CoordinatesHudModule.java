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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

public final class CoordinatesHudModule
    extends Module
    implements DraggableHudModule {

    private static final int PADDING = 3;
    private static final int LINE_GAP = 2;
    private static final String[] DIRECTIONS = {
        "S",
        "SW",
        "W",
        "NW",
        "N",
        "NE",
        "E",
        "SE"
    };

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final BooleanSetting showCoordinates = addSetting(
        new BooleanSetting("Show X/Y/Z", true)
    );
    private final BooleanSetting showDirection = addSetting(
        new BooleanSetting("Show Direction", true)
    );
    private final BooleanSetting showBiome = addSetting(
        new BooleanSetting("Show Biome", true)
    );
    private final BooleanSetting showBackground = addSetting(
        new BooleanSetting("Show Background", true)
    );
    private final ModeSetting layout = addSetting(
        new ModeSetting("Layout", "Compact", "Vertical", "Compact")
    );
    private final ModeSetting coordinatePrecision = addSetting(
        new ModeSetting(
            "Coordinate Precision",
            "Integer",
            "Integer",
            "Decimal"
        )
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
    private final NumberSetting positionX = addHiddenSetting(
        new NumberSetting("Position X", 5.0D, 0.0D, 10000.0D, 1.0D)
    );
    private final NumberSetting positionY = addHiddenSetting(
        new NumberSetting("Position Y", 115.0D, 0.0D, 10000.0D, 1.0D)
    );
    private final EventListener<Render2DEvent> renderListener =
        new EventListener<Render2DEvent>() {
            @Override
            public void onEvent(Render2DEvent event) {
                renderHud();
            }
        };

    public CoordinatesHudModule() {
        super(
            "Coordinates HUD",
            "Displays coordinates, facing direction and biome.",
            ModuleCategory.HUD,
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
        if (minecraft.thePlayer == null || minecraft.theWorld == null) {
            return;
        }

        List<String> lines = getDisplayLines();
        if (lines.isEmpty()) {
            return;
        }

        if (renderWithBridge(lines)) {
            return;
        }
        renderLegacy(lines);
    }

    private boolean renderWithBridge(List<String> lines) {
        RenderBridge renderBridge = RenderBridgeAccess.getIfReady();
        if (renderBridge == null) {
            return false;
        }

        boolean matrixPushed = false;
        try {
            int fontHeight = RenderBridgeAccess.safeFontHeight(renderBridge);
            if (fontHeight <= 0) {
                return false;
            }

            int width = getBridgeContentWidth(renderBridge, lines);
            if (width <= 0) {
                return false;
            }

            int height = getBridgeContentHeight(fontHeight, lines);
            if (height <= 0) {
                return false;
            }

            float renderScale = scale.getValue().floatValue();
            int renderX = Math.round(getPositionX() / renderScale);
            int renderY = Math.round(getPositionY() / renderScale);
            int padding = showBackground.isEnabled() ? PADDING : 0;

            GlStateManager.pushMatrix();
            matrixPushed = true;
            GlStateManager.scale(renderScale, renderScale, 1.0F);

            if (showBackground.isEnabled()) {
                renderBridge.drawRect(
                    renderX,
                    renderY,
                    renderX + width + PADDING * 2,
                    renderY + height + PADDING * 2,
                    backgroundColor.getColor()
                );
            }

            for (int index = 0; index < lines.size(); index++) {
                renderBridge.drawText(
                    lines.get(index),
                    renderX + padding,
                    renderY + padding + index * (fontHeight + LINE_GAP),
                    textColor.getColor(),
                    false
                );
            }
            return true;
        } catch (RuntimeException exception) {
            return false;
        } finally {
            if (matrixPushed) {
                GlStateManager.popMatrix();
            }
        }
    }

    private int getBridgeContentWidth(RenderBridge renderBridge, List<String> lines) {
        int width = 1;
        for (String line : lines) {
            int lineWidth = RenderBridgeAccess.safeStringWidth(renderBridge, line);
            if (lineWidth <= 0) {
                return 0;
            }
            width = Math.max(width, lineWidth);
        }
        return width;
    }

    private int getBridgeContentHeight(int fontHeight, List<String> lines) {
        return lines.isEmpty()
            ? fontHeight
            : lines.size() * fontHeight
                + Math.max(0, lines.size() - 1) * LINE_GAP;
    }

    private void renderLegacy(List<String> lines) {
        float renderScale = scale.getValue().floatValue();
        int renderX = Math.round(getPositionX() / renderScale);
        int renderY = Math.round(getPositionY() / renderScale);
        int width = getContentWidth(lines);
        int height = getContentHeight(lines);
        int padding = showBackground.isEnabled() ? PADDING : 0;

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

        for (int index = 0; index < lines.size(); index++) {
            minecraft.fontRendererObj.drawString(
                lines.get(index),
                renderX + padding,
                renderY + padding
                    + index * (minecraft.fontRendererObj.FONT_HEIGHT + LINE_GAP),
                textColor.getColor()
            );
        }

        GlStateManager.popMatrix();
    }

    private List<String> getDisplayLines() {
        List<String> lines = new ArrayList<String>();
        if (minecraft.thePlayer == null || minecraft.theWorld == null) {
            return lines;
        }

        String x = formatCoordinate(minecraft.thePlayer.posX);
        String y = formatCoordinate(minecraft.thePlayer.posY);
        String z = formatCoordinate(minecraft.thePlayer.posZ);
        String direction = getDirection(minecraft.thePlayer.rotationYaw);
        String biome = minecraft.theWorld
            .getBiomeGenForCoords(new BlockPos(minecraft.thePlayer))
            .biomeName;

        if ("Compact".equals(layout.getValue())) {
            StringBuilder compact = new StringBuilder();
            if (showCoordinates.isEnabled()) {
                compact.append("XYZ: ")
                    .append(x)
                    .append(" / ")
                    .append(y)
                    .append(" / ")
                    .append(z);
            }
            if (showDirection.isEnabled()) {
                appendCompact(compact, direction);
            }
            if (showBiome.isEnabled()) {
                appendCompact(compact, biome);
            }
            if (compact.length() > 0) {
                lines.add(compact.toString());
            }
            return lines;
        }

        if (showCoordinates.isEnabled()) {
            lines.add("X: " + x);
            lines.add("Y: " + y);
            lines.add("Z: " + z);
        }
        if (showDirection.isEnabled()) {
            lines.add("Direction: " + direction);
        }
        if (showBiome.isEnabled()) {
            lines.add("Biome: " + biome);
        }
        return lines;
    }

    private void appendCompact(StringBuilder text, String value) {
        if (text.length() > 0) {
            text.append(" | ");
        }
        text.append(value);
    }

    private String formatCoordinate(double coordinate) {
        if ("Decimal".equals(coordinatePrecision.getValue())) {
            return String.format(Locale.ROOT, "%.1f", coordinate);
        }
        return Integer.toString((int) Math.floor(coordinate));
    }

    private String getDirection(float rotationYaw) {
        int index = Math.floorMod(
            (int) Math.floor(rotationYaw * 8.0F / 360.0F + 0.5F),
            DIRECTIONS.length
        );
        return DIRECTIONS[index];
    }

    private int getContentWidth(List<String> lines) {
        int width = 1;
        for (String line : lines) {
            width = Math.max(width, minecraft.fontRendererObj.getStringWidth(line));
        }
        return width;
    }

    private int getContentHeight(List<String> lines) {
        return lines.isEmpty()
            ? minecraft.fontRendererObj.FONT_HEIGHT
            : lines.size() * minecraft.fontRendererObj.FONT_HEIGHT
                + Math.max(0, lines.size() - 1) * LINE_GAP;
    }

    private int getUnscaledWidth() {
        List<String> lines = getDisplayLines();
        int width = lines.isEmpty()
            ? minecraft.fontRendererObj.getStringWidth("XYZ: 0 / 0 / 0 | N")
            : getContentWidth(lines);
        return width + (showBackground.isEnabled() ? PADDING * 2 : 0);
    }

    private int getUnscaledHeight() {
        List<String> lines = getDisplayLines();
        int height = lines.isEmpty()
            ? minecraft.fontRendererObj.FONT_HEIGHT
            : getContentHeight(lines);
        return height + (showBackground.isEnabled() ? PADDING * 2 : 0);
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
