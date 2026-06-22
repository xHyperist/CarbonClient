package com.carbonclient.input;

import com.carbonclient.config.ConfigManager;
import com.carbonclient.gui.CarbonMenuScreen;
import com.carbonclient.module.ModuleManager;
import com.carbonclient.notification.NotificationManager;
import com.carbonclient.notification.NotificationRenderer;
import com.carbonclient.profile.ProfileManager;
import com.carbonclient.visual.VisualManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;

public final class KeyInputHandler {

    private final ModuleManager moduleManager;
    private final VisualManager visualManager;
    private final NotificationManager notificationManager;
    private final NotificationRenderer notificationRenderer;
    private ConfigManager configManager;
    private ProfileManager profileManager;

    public KeyInputHandler(
        ModuleManager moduleManager,
        VisualManager visualManager,
        NotificationManager notificationManager,
        NotificationRenderer notificationRenderer
    ) {
        this.moduleManager = moduleManager;
        this.visualManager = visualManager;
        this.notificationManager = notificationManager;
        this.notificationRenderer = notificationRenderer;
    }

    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        if (!Keyboard.getEventKeyState() || Keyboard.isRepeatEvent()) {
            return;
        }

        Minecraft minecraft = Minecraft.getMinecraft();
        int keyCode = Keyboard.getEventKey();

        if (keyCode == Keyboard.KEY_RSHIFT) {
            if (minecraft.currentScreen == null
                && configManager != null
                && profileManager != null) {
                minecraft.displayGuiScreen(
                    new CarbonMenuScreen(
                        moduleManager,
                        visualManager,
                        configManager,
                        profileManager,
                        notificationManager,
                        notificationRenderer
                    )
                );
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
