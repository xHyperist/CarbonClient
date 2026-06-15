package com.carbonclient.gui;

import com.carbonclient.config.ConfigManager;
import com.carbonclient.module.DraggableHudModule;
import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleManager;
import com.carbonclient.ui.theme.CarbonTheme;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public final class HudLayoutEditorScreen extends GuiScreen {

    private final ModuleManager moduleManager;
    private final ConfigManager configManager;
    private DraggableHudModule selectedHud;
    private int dragOffsetX;
    private int dragOffsetY;

    public HudLayoutEditorScreen(
        ModuleManager moduleManager,
        ConfigManager configManager
    ) {
        this.moduleManager = moduleManager;
        this.configManager = configManager;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(0, 0, width, height, CarbonTheme.OVERLAY);
        drawCenteredString(
            fontRendererObj,
            "HUD Layout Editor - Drag modules, press ESC to save and exit",
            width / 2,
            8,
            CarbonTheme.TEXT
        );

        for (DraggableHudModule hud : getHudModules()) {
            Module module = (Module) hud;

            if (!module.isEnabled()) {
                hud.renderHud();
            }

            int outlineColor = hud == selectedHud
                ? CarbonTheme.PRIMARY
                : CarbonTheme.ACCENT;
            drawOutline(
                hud.getPositionX(),
                hud.getPositionY(),
                hud.getHudWidth(),
                hud.getHudHeight(),
                outlineColor
            );
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
        throws IOException {
        if (mouseButton == 0) {
            List<DraggableHudModule> hudModules = getHudModules();

            for (int index = hudModules.size() - 1; index >= 0; index--) {
                DraggableHudModule hud = hudModules.get(index);
                if (isInside(hud, mouseX, mouseY)) {
                    selectedHud = hud;
                    dragOffsetX = mouseX - hud.getPositionX();
                    dragOffsetY = mouseY - hud.getPositionY();
                    return;
                }
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(
        int mouseX,
        int mouseY,
        int clickedMouseButton,
        long timeSinceLastClick
    ) {
        if (clickedMouseButton == 0 && selectedHud != null) {
            int maxX = Math.max(0, width - selectedHud.getHudWidth());
            int maxY = Math.max(0, height - selectedHud.getHudHeight());
            int x = Math.max(0, Math.min(maxX, mouseX - dragOffsetX));
            int y = Math.max(0, Math.min(maxY, mouseY - dragOffsetY));
            selectedHud.setPosition(x, y);
        }

        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        selectedHud = null;
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            configManager.save();
            mc.displayGuiScreen(null);
            return;
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private List<DraggableHudModule> getHudModules() {
        List<DraggableHudModule> modules = new ArrayList<DraggableHudModule>();

        for (Module module : moduleManager.getModules()) {
            if (module instanceof DraggableHudModule) {
                modules.add((DraggableHudModule) module);
            }
        }

        return modules;
    }

    private boolean isInside(DraggableHudModule hud, int mouseX, int mouseY) {
        return mouseX >= hud.getPositionX()
            && mouseX < hud.getPositionX() + hud.getHudWidth()
            && mouseY >= hud.getPositionY()
            && mouseY < hud.getPositionY() + hud.getHudHeight();
    }

    private void drawOutline(int x, int y, int width, int height, int color) {
        Gui.drawRect(x - 1, y - 1, x + width + 1, y, color);
        Gui.drawRect(x - 1, y + height, x + width + 1, y + height + 1, color);
        Gui.drawRect(x - 1, y, x, y + height, color);
        Gui.drawRect(x + width, y, x + width + 1, y + height, color);
    }
}
