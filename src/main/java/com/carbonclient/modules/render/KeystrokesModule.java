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
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public final class KeystrokesModule extends Module {

    private static final int X = 5;
    private static final int Y = 36;
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
                renderKeystrokes();
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

    private void renderKeystrokes() {
        float renderScale = scale.getValue().floatValue();
        int renderX = Math.round(X / renderScale);
        int renderY = Math.round(Y / renderScale);
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
