package com.carbonclient.modules.render;

import com.carbonclient.event.EventListener;
import com.carbonclient.event.impl.Render2DEvent;
import com.carbonclient.module.DraggableHudModule;
import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleCategory;
import com.carbonclient.setting.impl.BooleanSetting;
import com.carbonclient.setting.impl.ColorSetting;
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

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final BooleanSetting showClicks =
        addSetting(new BooleanSetting("Show Clicks", true));
    private final BooleanSetting showMovementKeys =
        addSetting(new BooleanSetting("Show Movement Keys", true));
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
            ModuleCategory.RENDER
        );
        setKeyCode(Keyboard.KEY_K);
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
        int secondRowY = renderY + KEY_HEIGHT + GAP;
        int mouseRowY = showMovementKeys.isEnabled()
            ? secondRowY + KEY_HEIGHT + GAP
            : renderY;

        GlStateManager.pushMatrix();
        GlStateManager.scale(renderScale, renderScale, 1.0F);

        if (showMovementKeys.isEnabled()) {
            drawKey(
                "W",
                renderX + KEY_WIDTH + GAP,
                renderY,
                KEY_WIDTH,
                isKeyDown(minecraft.gameSettings.keyBindForward)
            );
            drawKey(
                "A",
                renderX,
                secondRowY,
                KEY_WIDTH,
                isKeyDown(minecraft.gameSettings.keyBindLeft)
            );
            drawKey(
                "S",
                renderX + KEY_WIDTH + GAP,
                secondRowY,
                KEY_WIDTH,
                isKeyDown(minecraft.gameSettings.keyBindBack)
            );
            drawKey(
                "D",
                renderX + (KEY_WIDTH + GAP) * 2,
                secondRowY,
                KEY_WIDTH,
                isKeyDown(minecraft.gameSettings.keyBindRight)
            );
        }

        if (showClicks.isEnabled()) {
            drawKey("LMB", renderX, mouseRowY, MOUSE_WIDTH, Mouse.isButtonDown(0));
            drawKey(
                "RMB",
                renderX + MOUSE_WIDTH + GAP,
                mouseRowY,
                MOUSE_WIDTH,
                Mouse.isButtonDown(1)
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
        return Math.round((MOUSE_WIDTH * 2 + GAP) * scale.getValue().floatValue());
    }

    @Override
    public int getHudHeight() {
        int rows = showMovementKeys.isEnabled() ? 2 : 0;
        if (showClicks.isEnabled()) {
            rows++;
        }
        int contentHeight = rows == 0 ? KEY_HEIGHT : rows * KEY_HEIGHT + (rows - 1) * GAP;
        return Math.round(contentHeight * scale.getValue().floatValue());
    }

    private boolean isKeyDown(KeyBinding keyBinding) {
        return keyBinding.isKeyDown();
    }

    private void drawKey(String label, int x, int y, int width, boolean active) {
        int color = active ? pressedColor.getColor() : backgroundColor.getColor();
        int textX = x + (width - minecraft.fontRendererObj.getStringWidth(label)) / 2;
        int textY = y + (KEY_HEIGHT - minecraft.fontRendererObj.FONT_HEIGHT) / 2;

        Gui.drawRect(x, y, x + width, y + KEY_HEIGHT, color);
        minecraft.fontRendererObj.drawString(label, textX, textY, textColor.getColor());
    }
}
