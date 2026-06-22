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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public final class KeystrokesModule extends Module implements DraggableHudModule {

    private static final int KEY_WIDTH = 20;
    private static final int KEY_HEIGHT = 15;
    private static final int MOUSE_WIDTH = 31;
    private static final int GAP = 2;
    private static final String STYLE_MODERN = "Modern";
    private static final String STYLE_CLASSIC = "Classic";
    private static final String STYLE_MINIMAL = "Minimal";

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final BooleanSetting showClicks =
        addSetting(new BooleanSetting("Show Clicks", true));
    private final BooleanSetting showMovementKeys =
        addSetting(new BooleanSetting("Show Movement Keys", true));
    private final BooleanSetting showSpace =
        addSetting(new BooleanSetting("Show Space", true));
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
            new NumberSetting("Position Y", 36.0D, 0.0D, 10000.0D, 1.0D)
        );
    private final ColorSetting textColor =
        addSetting(new ColorSetting("Text Color", 0xFFFFFFFF));
    private final ColorSetting backgroundColor =
        addSetting(new ColorSetting("Background Color", 0xB0121824));
    private final ColorSetting pressedColor =
        addSetting(new ColorSetting("Pressed Color", 0xD0506078));
    private final EventListener<Render2DEvent> renderListener =
        new EventListener<Render2DEvent>() {
            @Override
            public void onEvent(Render2DEvent event) {
                renderHud();
            }
        };

    public KeystrokesModule() {
        super(
            "Keystrokes",
            "Displays movement keys and mouse button states.",
            ModuleCategory.HUD,
            true,
            Keyboard.KEY_K
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
        float renderScale = scale.getValue().floatValue();
        int renderX = Math.round(getPositionX() / renderScale);
        int renderY = Math.round(getPositionY() / renderScale);
        int gap = getGap();
        int rowY = renderY;

        GlStateManager.pushMatrix();
        GlStateManager.scale(renderScale, renderScale, 1.0F);

        if (showMovementKeys.isEnabled()) {
            drawKey(
                "W",
                renderX + KEY_WIDTH + gap,
                renderY,
                KEY_WIDTH,
                isKeyDown(minecraft.gameSettings.keyBindForward)
            );
            int secondRowY = rowY + KEY_HEIGHT + gap;
            drawKey(
                "A",
                renderX,
                secondRowY,
                KEY_WIDTH,
                isKeyDown(minecraft.gameSettings.keyBindLeft)
            );
            drawKey(
                "S",
                renderX + KEY_WIDTH + gap,
                secondRowY,
                KEY_WIDTH,
                isKeyDown(minecraft.gameSettings.keyBindBack)
            );
            drawKey(
                "D",
                renderX + (KEY_WIDTH + gap) * 2,
                secondRowY,
                KEY_WIDTH,
                isKeyDown(minecraft.gameSettings.keyBindRight)
            );
            rowY = secondRowY + KEY_HEIGHT + gap;
        }

        if (showClicks.isEnabled()) {
            drawKey("LMB", renderX, rowY, MOUSE_WIDTH, Mouse.isButtonDown(0));
            drawKey(
                "RMB",
                renderX + MOUSE_WIDTH + gap,
                rowY,
                MOUSE_WIDTH,
                Mouse.isButtonDown(1)
            );
            rowY += KEY_HEIGHT + gap;
        }

        if (showSpace.isEnabled()) {
            drawKey(
                "SPACE",
                renderX,
                rowY,
                getUnscaledWidth(),
                isKeyDown(minecraft.gameSettings.keyBindJump)
            );
        }

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
        return Math.round(getUnscaledWidth() * scale.getValue().floatValue());
    }

    @Override
    public int getHudHeight() {
        return Math.round(getUnscaledHeight() * scale.getValue().floatValue());
    }

    private boolean isKeyDown(KeyBinding keyBinding) {
        return keyBinding.isKeyDown();
    }

    private void drawKey(String label, int x, int y, int width, boolean active) {
        int color = active ? pressedColor.getColor() : backgroundColor.getColor();
        int textX = x + (width - minecraft.fontRendererObj.getStringWidth(label)) / 2;
        int textY = y + (KEY_HEIGHT - minecraft.fontRendererObj.FONT_HEIGHT) / 2;

        if (active || showBackground.isEnabled()) {
            Gui.drawRect(x, y, x + width, y + KEY_HEIGHT, color);
        }
        minecraft.fontRendererObj.drawString(label, textX, textY, textColor.getColor());
    }

    private int getUnscaledWidth() {
        int gap = getGap();
        int width = 0;
        if (showMovementKeys.isEnabled()) {
            width = Math.max(width, KEY_WIDTH * 3 + gap * 2);
        }
        if (showClicks.isEnabled()) {
            width = Math.max(width, MOUSE_WIDTH * 2 + gap);
        }
        if (showSpace.isEnabled()) {
            width = Math.max(width, MOUSE_WIDTH * 2 + gap);
        }
        return width == 0 ? KEY_WIDTH : width;
    }

    private int getUnscaledHeight() {
        int rows = 0;
        if (showMovementKeys.isEnabled()) {
            rows += 2;
        }
        if (showClicks.isEnabled()) {
            rows++;
        }
        if (showSpace.isEnabled()) {
            rows++;
        }
        if (rows == 0) {
            return KEY_HEIGHT;
        }
        return rows * KEY_HEIGHT + (rows - 1) * getGap();
    }

    private int getGap() {
        if (STYLE_MINIMAL.equals(styleMode.getValue())) {
            return 1;
        }
        if (STYLE_CLASSIC.equals(styleMode.getValue())) {
            return GAP;
        }
        return 3;
    }
}
