package com.carbonclient.modules.movement;

import com.carbonclient.event.EventListener;
import com.carbonclient.event.impl.ClientTickEvent;
import com.carbonclient.event.impl.Render2DEvent;
import com.carbonclient.module.DraggableHudModule;
import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleCategory;
import com.carbonclient.setting.impl.BooleanSetting;
import com.carbonclient.setting.impl.ColorSetting;
import com.carbonclient.setting.impl.KeybindSetting;
import com.carbonclient.setting.impl.ModeSetting;
import com.carbonclient.setting.impl.NumberSetting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public final class ToggleSprintModule extends Module implements DraggableHudModule {

    private static final int PADDING = 3;
    private static final int LINE_GAP = 2;

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final BooleanSetting toggleSprintEnabled =
        addSetting(new BooleanSetting("Toggle Sprint Enabled", true));
    private final BooleanSetting toggleSneakEnabled =
        addSetting(new BooleanSetting("Toggle Sneak Enabled", true));
    private final BooleanSetting useSeparateToggleButtons =
        addSetting(new BooleanSetting("Use Separate Toggle Buttons", false));
    private final KeybindSetting sprintToggleKey =
        addSetting(new KeybindSetting("Toggle Sprint Key", Keyboard.KEY_G));
    private final KeybindSetting sneakToggleKey =
        addSetting(new KeybindSetting("Toggle Sneak Key", Keyboard.KEY_V));
    private final BooleanSetting inventorySneak =
        addSetting(new BooleanSetting("Inventory Sneak", false));
    private final BooleanSetting disableSneakWhileFlying =
        addSetting(new BooleanSetting("Disable Sneak While Flying", true));
    private final BooleanSetting showHud =
        addSetting(new BooleanSetting("Show HUD", true));
    private final ModeSetting renderMode =
        addSetting(new ModeSetting("Render Mode", "Modern", "Classic", "Modern"));
    private final ColorSetting textColor =
        addSetting(new ColorSetting("Text Color", 0xFFFFFFFF));
    private final ColorSetting backgroundColor =
        addSetting(new ColorSetting("Background Color", 0xB0121A2E));
    private final BooleanSetting showBackground =
        addSetting(new BooleanSetting("Show Background", true));
    private final NumberSetting scale =
        addSetting(new NumberSetting("Scale", 1.0D, 0.5D, 2.0D, 0.1D));
    private final NumberSetting positionX =
        addHiddenSetting(
            new NumberSetting("Position X", 5.0D, 0.0D, 10000.0D, 1.0D)
        );
    private final NumberSetting positionY =
        addHiddenSetting(
            new NumberSetting("Position Y", 88.0D, 0.0D, 10000.0D, 1.0D)
        );
    private boolean sprintToggled;
    private boolean sneakToggled;
    private boolean sprintButtonWasDown;
    private boolean sneakButtonWasDown;
    private boolean sprintButtonDown;
    private boolean sneakButtonDown;
    private boolean sprintKeyHeld;
    private boolean sneakKeyHeld;
    private World activeWorld;
    private final EventListener<ClientTickEvent> tickListener =
        new EventListener<ClientTickEvent>() {
            @Override
            public void onEvent(ClientTickEvent event) {
                updateMovementToggles();
            }
        };
    private final EventListener<Render2DEvent> renderListener =
        new EventListener<Render2DEvent>() {
            @Override
            public void onEvent(Render2DEvent event) {
                renderHud();
            }
        };

    public ToggleSprintModule() {
        super(
            "ToggleSprint",
            "Provides configurable toggle sprint and toggle sneak controls.",
            ModuleCategory.MOVEMENT,
            true,
            Keyboard.KEY_NONE
        );
    }

    @Override
    protected void onEnable() {
        resetRuntimeState();
        subscribe(ClientTickEvent.class, tickListener);
        subscribe(Render2DEvent.class, renderListener);
    }

    @Override
    protected void onDisable() {
        unsubscribe(ClientTickEvent.class, tickListener);
        unsubscribe(Render2DEvent.class, renderListener);
        resetRuntimeState();
    }

    private void updateMovementToggles() {
        if (minecraft.thePlayer == null || minecraft.theWorld == null) {
            if (activeWorld != null) {
                resetRuntimeState();
            }
            return;
        }

        if (activeWorld != minecraft.theWorld) {
            resetRuntimeState();
            activeWorld = minecraft.theWorld;
        }

        int sprintKey = getSprintToggleKey();
        int sneakKey = getSneakToggleKey();
        sprintButtonDown = isPhysicalKeyDown(sprintKey);
        sneakButtonDown = isPhysicalKeyDown(sneakKey);
        sprintKeyHeld = isPhysicalKeyDown(
            minecraft.gameSettings.keyBindSprint.getKeyCode()
        );
        sneakKeyHeld = isPhysicalKeyDown(
            minecraft.gameSettings.keyBindSneak.getKeyCode()
        );
        boolean canToggle = minecraft.currentScreen == null;

        if (toggleSprintEnabled.isEnabled()
            && canToggle
            && sprintButtonDown
            && !sprintButtonWasDown) {
            sprintToggled = !sprintToggled;
            if (!sprintToggled) {
                minecraft.thePlayer.setSprinting(false);
            }
        }
        if (toggleSneakEnabled.isEnabled()
            && canToggle
            && sneakButtonDown
            && !sneakButtonWasDown) {
            sneakToggled = !sneakToggled;
        }

        sprintButtonWasDown = sprintButtonDown;
        sneakButtonWasDown = sneakButtonDown;

        if (!toggleSprintEnabled.isEnabled()) {
            if (sprintToggled) {
                minecraft.thePlayer.setSprinting(false);
            }
            sprintToggled = false;
        }
        if (!toggleSneakEnabled.isEnabled()) {
            sneakToggled = false;
        }

        applySprint(canToggle);
        applySneak(canToggle);
    }

    private void applySprint(boolean gameplayActive) {
        if (!toggleSprintEnabled.isEnabled()) {
            return;
        }

        if (!sprintToggled) {
            return;
        }

        boolean shouldSprint = gameplayActive
            && minecraft.thePlayer.movementInput.moveForward > 0.0F
            && !minecraft.thePlayer.isSneaking();
        minecraft.thePlayer.setSprinting(shouldSprint);
    }

    private void applySneak(boolean gameplayActive) {
        boolean vanillaSneakHeld = isPhysicalKeyDown(
            minecraft.gameSettings.keyBindSneak.getKeyCode()
        );

        if (!toggleSneakEnabled.isEnabled()) {
            KeyBinding.setKeyBindState(
                minecraft.gameSettings.keyBindSneak.getKeyCode(),
                vanillaSneakHeld
            );
            return;
        }

        // Inventory sneak can be considered unfair on some servers; keep it opt-in.
        boolean flyingBlocked = disableSneakWhileFlying.isEnabled()
            && minecraft.thePlayer.capabilities.isFlying;
        boolean sprintingBlocked = minecraft.thePlayer.isSprinting();

        if (!toggleSneakEnabled.isEnabled() || flyingBlocked || sprintingBlocked) {
            sneakToggled = false;
        }

        boolean screenAllowsSneak = gameplayActive || inventorySneak.isEnabled();
        boolean shouldSneak = screenAllowsSneak
            && !flyingBlocked
            && (sneakToggled || vanillaSneakHeld);

        if (shouldSneak) {
            KeyBinding.setKeyBindState(
                minecraft.gameSettings.keyBindSneak.getKeyCode(),
                true
            );
        } else {
            releaseSneakKey();
        }
    }

    private int getSprintToggleKey() {
        if (useSeparateToggleButtons.isEnabled()) {
            return sprintToggleKey.getKeyCode();
        }
        return minecraft.gameSettings.keyBindSprint.getKeyCode();
    }

    private int getSneakToggleKey() {
        if (useSeparateToggleButtons.isEnabled()) {
            return sneakToggleKey.getKeyCode();
        }
        return minecraft.gameSettings.keyBindSneak.getKeyCode();
    }

    private boolean isPhysicalKeyDown(int keyCode) {
        return keyCode > Keyboard.KEY_NONE
            && keyCode < Keyboard.KEYBOARD_SIZE
            && Keyboard.isKeyDown(keyCode);
    }

    private void resetButtonStates() {
        sprintButtonWasDown = isPhysicalKeyDown(getSprintToggleKey());
        sneakButtonWasDown = isPhysicalKeyDown(getSneakToggleKey());
    }

    private void resetRuntimeState() {
        sprintToggled = false;
        sneakToggled = false;
        sprintButtonDown = false;
        sneakButtonDown = false;
        sprintKeyHeld = false;
        sneakKeyHeld = false;
        activeWorld = null;
        resetButtonStates();

        if (minecraft.thePlayer != null) {
            minecraft.thePlayer.setSprinting(false);
        }
        releaseSneakKey();
    }

    private void releaseSneakKey() {
        KeyBinding.setKeyBindState(
            minecraft.gameSettings.keyBindSneak.getKeyCode(),
            false
        );
    }

    @Override
    public void renderHud() {
        if (!showHud.isEnabled()) {
            return;
        }

        List<String> lines = getHudLines();
        if (lines.isEmpty()) {
            return;
        }

        int contentWidth = getContentWidth(lines);
        int contentHeight = getContentHeight(lines);
        float renderScale = scale.getValue().floatValue();
        int renderX = Math.round(getPositionX() / renderScale);
        int renderY = Math.round(getPositionY() / renderScale);
        int inset = showBackground.isEnabled() ? PADDING : 0;

        GlStateManager.pushMatrix();
        GlStateManager.scale(renderScale, renderScale, 1.0F);

        if (showBackground.isEnabled()) {
            Gui.drawRect(
                renderX,
                renderY,
                renderX + contentWidth + PADDING * 2,
                renderY + contentHeight + PADDING * 2,
                backgroundColor.getColor()
            );
        }

        for (int index = 0; index < lines.size(); index++) {
            minecraft.fontRendererObj.drawString(
                lines.get(index),
                renderX + inset,
                renderY + inset
                    + index * (minecraft.fontRendererObj.FONT_HEIGHT + LINE_GAP),
                textColor.getColor()
            );
        }

        GlStateManager.popMatrix();
    }

    private List<String> getHudLines() {
        List<String> lines = new ArrayList<String>();
        MovementState sprintState = getSprintState();
        MovementState sneakState = getSneakState();

        if ("Classic".equalsIgnoreCase(renderMode.getValue())) {
            if (sprintState != MovementState.OFF) {
                lines.add("[Sprinting (" + sprintState.getClassicLabel() + ")]");
            }
            if (sneakState != MovementState.OFF) {
                lines.add("[Sneaking (" + sneakState.getClassicLabel() + ")]");
            }
            return lines;
        }

        if (sprintState != MovementState.OFF) {
            lines.add("Sprint [" + sprintState.getModernLabel() + "]");
        }
        if (sneakState != MovementState.OFF) {
            lines.add("Sneak [" + sneakState.getModernLabel() + "]");
        }
        return lines;
    }

    private MovementState getSprintState() {
        if (sprintKeyHeld) {
            return MovementState.KEY_HELD;
        }
        if (sprintToggled) {
            return MovementState.TOGGLED;
        }
        return MovementState.OFF;
    }

    private MovementState getSneakState() {
        if (sneakKeyHeld) {
            return MovementState.KEY_HELD;
        }
        if (sneakToggled) {
            return MovementState.TOGGLED;
        }
        return MovementState.OFF;
    }

    private int getContentWidth(List<String> lines) {
        int width = 0;

        for (String line : lines) {
            width = Math.max(width, minecraft.fontRendererObj.getStringWidth(line));
        }

        return width;
    }

    private int getContentHeight(List<String> lines) {
        return lines.size() * minecraft.fontRendererObj.FONT_HEIGHT
            + Math.max(0, lines.size() - 1) * LINE_GAP;
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
        List<String> lines = getHudLines();
        if (lines.isEmpty()) {
            lines = getEditorPreviewLines();
        }
        int padding = showBackground.isEnabled() ? PADDING * 2 : 0;
        return Math.round(
            (getContentWidth(lines) + padding)
                * scale.getValue().floatValue()
        );
    }

    @Override
    public int getHudHeight() {
        List<String> lines = getHudLines();
        if (lines.isEmpty()) {
            lines = getEditorPreviewLines();
        }
        int padding = showBackground.isEnabled() ? PADDING * 2 : 0;
        return Math.round(
            (getContentHeight(lines) + padding)
                * scale.getValue().floatValue()
        );
    }

    private List<String> getEditorPreviewLines() {
        List<String> lines = new ArrayList<String>();
        if ("Classic".equalsIgnoreCase(renderMode.getValue())) {
            lines.add("[Sprinting (Toggled)]");
        } else {
            lines.add("Sprint [TOGGLED]");
        }
        return lines;
    }

    private enum MovementState {
        OFF("", ""),
        KEY_HELD("Key Held", "HELD"),
        TOGGLED("Toggled", "TOGGLED");

        private final String classicLabel;
        private final String modernLabel;

        MovementState(String classicLabel, String modernLabel) {
            this.classicLabel = classicLabel;
            this.modernLabel = modernLabel;
        }

        public String getClassicLabel() {
            return classicLabel;
        }

        public String getModernLabel() {
            return modernLabel;
        }
    }
}
