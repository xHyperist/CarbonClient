package com.carbonclient.input;

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
        if (Minecraft.getMinecraft().currentScreen != null) {
            return;
        }

        moduleManager.toggleByKeyCode(Keyboard.getEventKey());
    }
}
