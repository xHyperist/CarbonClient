package com.carbonclient.modules.render;

import com.carbonclient.event.EventListener;
import com.carbonclient.event.impl.Render2DEvent;
import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public final class KeystrokesModule extends Module {

    private static final int TEXT_COLOR = 0xFFFFFF;
    private static final int INACTIVE_COLOR = 0xB0121824;
    private static final int ACTIVE_COLOR = 0xD0506078;
    private static final int X = 5;
    private static final int Y = 36;
    private static final int KEY_WIDTH = 20;
    private static final int KEY_HEIGHT = 15;
    private static final int MOUSE_WIDTH = 31;
    private static final int GAP = 2;

    private final Minecraft minecraft = Minecraft.getMinecraft();
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
        int secondRowY = Y + KEY_HEIGHT + GAP;
        int mouseRowY = secondRowY + KEY_HEIGHT + GAP;

        drawKey(
            "W",
            X + KEY_WIDTH + GAP,
            Y,
            KEY_WIDTH,
            isKeyDown(minecraft.gameSettings.keyBindForward)
        );
        drawKey(
            "A",
            X,
            secondRowY,
            KEY_WIDTH,
            isKeyDown(minecraft.gameSettings.keyBindLeft)
        );
        drawKey(
            "S",
            X + KEY_WIDTH + GAP,
            secondRowY,
            KEY_WIDTH,
            isKeyDown(minecraft.gameSettings.keyBindBack)
        );
        drawKey(
            "D",
            X + (KEY_WIDTH + GAP) * 2,
            secondRowY,
            KEY_WIDTH,
            isKeyDown(minecraft.gameSettings.keyBindRight)
        );
        drawKey("LMB", X, mouseRowY, MOUSE_WIDTH, Mouse.isButtonDown(0));
        drawKey(
            "RMB",
            X + MOUSE_WIDTH + GAP,
            mouseRowY,
            MOUSE_WIDTH,
            Mouse.isButtonDown(1)
        );
    }

    private boolean isKeyDown(KeyBinding keyBinding) {
        return keyBinding.isKeyDown();
    }

    private void drawKey(String label, int x, int y, int width, boolean active) {
        int color = active ? ACTIVE_COLOR : INACTIVE_COLOR;
        int textX = x + (width - minecraft.fontRendererObj.getStringWidth(label)) / 2;
        int textY = y + (KEY_HEIGHT - minecraft.fontRendererObj.FONT_HEIGHT) / 2;

        Gui.drawRect(x, y, x + width, y + KEY_HEIGHT, color);
        minecraft.fontRendererObj.drawString(label, textX, textY, TEXT_COLOR);
    }
}
