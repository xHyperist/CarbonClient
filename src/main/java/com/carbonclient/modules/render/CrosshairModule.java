package com.carbonclient.modules.render;

import com.carbonclient.event.EventListener;
import com.carbonclient.event.impl.CrosshairRenderEvent;
import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleCategory;
import com.carbonclient.setting.impl.BooleanSetting;
import com.carbonclient.setting.impl.ColorSetting;
import com.carbonclient.setting.impl.ModeSetting;
import com.carbonclient.setting.impl.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public final class CrosshairModule extends Module {

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final BooleanSetting customEnabled = addSetting(
        new BooleanSetting("Enabled", true)
    );
    private final BooleanSetting hideVanillaCrosshair = addSetting(
        new BooleanSetting("Hide Vanilla Crosshair", true)
    );
    private final ModeSetting crosshairType = addSetting(
        new ModeSetting(
            "Crosshair Type",
            "Cross",
            "Cross",
            "Dot",
            "Circle",
            "T",
            "Plus"
        )
    );
    private final ColorSetting color = addSetting(
        new ColorSetting("Color", 0xFFFFFFFF)
    );
    private final ColorSetting outlineColor = addSetting(
        new ColorSetting("Outline Color", 0xFF000000)
    );
    private final BooleanSetting showOutline = addSetting(
        new BooleanSetting("Show Outline", true)
    );
    private final NumberSetting size = addSetting(
        new NumberSetting("Size", 5.0D, 1.0D, 20.0D, 1.0D)
    );
    private final NumberSetting gap = addSetting(
        new NumberSetting("Gap", 3.0D, 0.0D, 20.0D, 1.0D)
    );
    private final NumberSetting thickness = addSetting(
        new NumberSetting("Thickness", 1.0D, 1.0D, 6.0D, 1.0D)
    );
    private final NumberSetting opacity = addSetting(
        new NumberSetting("Opacity / Alpha", 1.0D, 0.1D, 1.0D, 0.05D)
    );
    private final BooleanSetting dynamicGap = addSetting(
        new BooleanSetting("Dynamic Gap", false)
    );
    private final NumberSetting dynamicGapAmount = addSetting(
        new NumberSetting("Dynamic Gap Amount", 3.0D, 1.0D, 12.0D, 1.0D)
    );
    private final BooleanSetting showDot = addSetting(
        new BooleanSetting("Show Dot", false)
    );
    private final NumberSetting dotSize = addSetting(
        new NumberSetting("Dot Size", 2.0D, 1.0D, 8.0D, 1.0D)
    );
    private final BooleanSetting showInThirdPerson = addSetting(
        new BooleanSetting("Show In Third Person", false)
    );
    private final EventListener<CrosshairRenderEvent> renderListener =
        new EventListener<CrosshairRenderEvent>() {
            @Override
            public void onEvent(CrosshairRenderEvent event) {
                renderCrosshair(event);
            }
        };

    public CrosshairModule() {
        super(
            "Crosshair",
            "Replaces the vanilla crosshair with a configurable PvP crosshair.",
            ModuleCategory.RENDER,
            true,
            Keyboard.KEY_NONE
        );
    }

    @Override
    protected void onEnable() {
        subscribe(CrosshairRenderEvent.class, renderListener);
    }

    @Override
    protected void onDisable() {
        unsubscribe(CrosshairRenderEvent.class, renderListener);
    }

    private void renderCrosshair(CrosshairRenderEvent event) {
        if (!customEnabled.isEnabled()
            || minecraft.currentScreen != null
            || minecraft.thePlayer == null
            || minecraft.theWorld == null
            || minecraft.gameSettings.thirdPersonView != 0
                && !showInThirdPerson.isEnabled()) {
            return;
        }

        if (hideVanillaCrosshair.isEnabled()) {
            event.setHideVanilla(true);
        }

        renderCrosshairAt(
            event.getScreenWidth() / 2,
            event.getScreenHeight() / 2,
            false
        );
    }

    public void renderCrosshairAt(
        int centerX,
        int centerY,
        boolean previewMode
    ) {
        if (!customEnabled.isEnabled()) {
            return;
        }

        int renderGap = gap.getValue().intValue()
            + getDynamicGap(previewMode);
        int renderSize = size.getValue().intValue();
        int renderThickness = thickness.getValue().intValue();
        String type = crosshairType.getValue();

        if ("Dot".equals(type)) {
            drawCenterDot(centerX, centerY);
            return;
        }
        if ("Circle".equals(type)) {
            drawCircle(centerX, centerY, renderGap + renderSize, renderThickness);
        } else if ("T".equals(type)) {
            drawHorizontalArms(
                centerX,
                centerY,
                renderGap,
                renderSize,
                renderThickness
            );
            drawSegment(
                centerX - renderThickness / 2,
                centerY + renderGap,
                renderThickness,
                renderSize
            );
        } else if ("Plus".equals(type)) {
            drawSegment(
                centerX - renderSize,
                centerY - renderThickness / 2,
                renderSize * 2 + 1,
                renderThickness
            );
            drawSegment(
                centerX - renderThickness / 2,
                centerY - renderSize,
                renderThickness,
                renderSize * 2 + 1
            );
        } else {
            drawHorizontalArms(
                centerX,
                centerY,
                renderGap,
                renderSize,
                renderThickness
            );
            drawSegment(
                centerX - renderThickness / 2,
                centerY - renderGap - renderSize,
                renderThickness,
                renderSize
            );
            drawSegment(
                centerX - renderThickness / 2,
                centerY + renderGap,
                renderThickness,
                renderSize
            );
        }

        if (showDot.isEnabled()) {
            drawCenterDot(centerX, centerY);
        }
    }

    private void drawHorizontalArms(
        int centerX,
        int centerY,
        int renderGap,
        int renderSize,
        int renderThickness
    ) {
        drawSegment(
            centerX - renderGap - renderSize,
            centerY - renderThickness / 2,
            renderSize,
            renderThickness
        );
        drawSegment(
            centerX + renderGap,
            centerY - renderThickness / 2,
            renderSize,
            renderThickness
        );
    }

    private void drawCircle(
        int centerX,
        int centerY,
        int radius,
        int renderThickness
    ) {
        if (showOutline.isEnabled()) {
            drawCircleLayer(
                centerX,
                centerY,
                radius,
                renderThickness + 2,
                applyOpacity(outlineColor.getColor())
            );
        }
        drawCircleLayer(
            centerX,
            centerY,
            radius,
            renderThickness,
            applyOpacity(color.getColor())
        );
    }

    private void drawCircleLayer(
        int centerX,
        int centerY,
        int radius,
        int pointSize,
        int renderColor
    ) {
        int halfPoint = pointSize / 2;
        for (int angle = 0; angle < 360; angle += 8) {
            double radians = Math.toRadians(angle);
            int x = centerX + (int) Math.round(Math.cos(radians) * radius);
            int y = centerY + (int) Math.round(Math.sin(radians) * radius);
            Gui.drawRect(
                x - halfPoint,
                y - halfPoint,
                x - halfPoint + pointSize,
                y - halfPoint + pointSize,
                renderColor
            );
        }
    }

    private void drawCenterDot(int centerX, int centerY) {
        int renderDotSize = dotSize.getValue().intValue();
        drawSegment(
            centerX - renderDotSize / 2,
            centerY - renderDotSize / 2,
            renderDotSize,
            renderDotSize
        );
    }

    private void drawSegment(int x, int y, int width, int height) {
        if (showOutline.isEnabled()) {
            Gui.drawRect(
                x - 1,
                y - 1,
                x + width + 1,
                y + height + 1,
                applyOpacity(outlineColor.getColor())
            );
        }
        Gui.drawRect(
            x,
            y,
            x + width,
            y + height,
            applyOpacity(color.getColor())
        );
    }

    private int getDynamicGap(boolean previewMode) {
        if (!dynamicGap.isEnabled()) {
            return 0;
        }
        if (previewMode) {
            return dynamicGapAmount.getValue().intValue();
        }

        boolean moving = Math.abs(minecraft.thePlayer.motionX) > 0.01D
            || Math.abs(minecraft.thePlayer.motionZ) > 0.01D
            || !minecraft.thePlayer.onGround;
        boolean interacting = Mouse.isButtonDown(0) || Mouse.isButtonDown(1);
        return moving || interacting
            ? dynamicGapAmount.getValue().intValue()
            : 0;
    }

    private int applyOpacity(int baseColor) {
        int baseAlpha = baseColor >>> 24 & 0xFF;
        int alpha = Math.round(
            baseAlpha * opacity.getValue().floatValue()
        );
        return alpha << 24 | baseColor & 0xFFFFFF;
    }
}
