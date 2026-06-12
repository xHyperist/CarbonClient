package com.carbonclient.input;

import com.carbonclient.gui.CarbonMenuScreen;
import com.carbonclient.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;

public final class KeyInputHandler {

    private final ModuleManager moduleManager;

    public KeyInputHandler(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        if (!Keyboard.getEventKeyState() || Keyboard.isRepeatEvent()) {
            return;
        }

        Minecraft minecraft = Minecraft.getMinecraft();
        int keyCode = Keyboard.getEventKey();

        if (keyCode == Keyboard.KEY_RSHIFT) {
            if (minecraft.currentScreen == null) {
                minecraft.displayGuiScreen(new CarbonMenuScreen());
            } else if (minecraft.currentScreen instanceof CarbonMenuScreen) {
                minecraft.displayGuiScreen(null);
            }
            return;
        }

        if (minecraft.currentScreen != null) {
            return;
        }

        moduleManager.toggleByKeyCode(keyCode);
    }
}
