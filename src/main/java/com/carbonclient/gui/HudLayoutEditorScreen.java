package com.carbonclient.gui;

import com.carbonclient.config.ConfigManager;
import com.carbonclient.module.DraggableHudModule;
import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleManager;
import com.carbonclient.notification.NotificationManager;
import com.carbonclient.notification.NotificationRenderer;
import com.carbonclient.profile.ProfileManager;
import com.carbonclient.ui.render.RenderUtils;
import com.carbonclient.ui.theme.CarbonTheme;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public final class HudLayoutEditorScreen extends GuiScreen {

    private final ModuleManager moduleManager;
    private final ConfigManager configManager;
    private final ProfileManager profileManager;
    private final NotificationManager notificationManager;
    private final NotificationRenderer notificationRenderer;
    private DraggableHudModule selectedHud;
    private int dragOffsetX;
    private int dragOffsetY;

    public HudLayoutEditorScreen(
        ModuleManager moduleManager,
        ConfigManager configManager,
        ProfileManager profileManager,
        NotificationManager notificationManager,
        NotificationRenderer notificationRenderer
    ) {
        if (moduleManager == null
            || configManager == null
            || profileManager == null
            || notificationManager == null
            || notificationRenderer == null) {
            throw new IllegalArgumentException(
                "HudLayoutEditorScreen dependencies cannot be null."
            );
        }

        this.moduleManager = moduleManager;
        this.configManager = configManager;
        this.profileManager = profileManager;
        this.notificationManager = notificationManager;
        this.notificationRenderer = notificationRenderer;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtils.drawPanel(0, 0, width, height, CarbonTheme.OVERLAY);
        int toolbarWidth = Math.min(360, width - CarbonTheme.SPACE_24);
        int toolbarX = (width - toolbarWidth) / 2;
        RenderUtils.drawPanel(
            toolbarX,
            CarbonTheme.SPACE_4,
            toolbarWidth,
            24,
            CarbonTheme.PANEL
        );
        RenderUtils.drawOutline(
            toolbarX,
            CarbonTheme.SPACE_4,
            toolbarWidth,
            24,
            CarbonTheme.BORDER
        );
        RenderUtils.drawCenteredText(
            fontRendererObj,
            getToolbarText(),
            toolbarX,
            CarbonTheme.SPACE_4,
            toolbarWidth,
            24,
            CarbonTheme.TEXT
        );

        for (DraggableHudModule hud : getHudModules()) {
            Module module = (Module) hud;
            clampToScreen(hud);

            if (!module.isEnabled()) {
                hud.renderHud();
            }

            int outlineColor = hud == selectedHud
                ? CarbonTheme.PRIMARY
                : CarbonTheme.ACCENT;
            RenderUtils.drawOutline(
                hud.getPositionX(),
                hud.getPositionY(),
                hud.getHudWidth(),
                hud.getHudHeight(),
                outlineColor
            );
            if (hud == selectedHud) {
                RenderUtils.drawOutline(
                    hud.getPositionX() - CarbonTheme.SPACE_2,
                    hud.getPositionY() - CarbonTheme.SPACE_2,
                    hud.getHudWidth() + CarbonTheme.SPACE_4,
                    hud.getHudHeight() + CarbonTheme.SPACE_4,
                    CarbonTheme.ACCENT
                );
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
        notificationRenderer.render(width, height);
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
            selectedHud = null;
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
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            persistActiveState();
            notificationManager.success(
                "HUD Layout Saved",
                "HUD positions were saved."
            );
            mc.displayGuiScreen(null);
            return;
        }

        super.keyTyped(typedChar, keyCode);
    }

    private void persistActiveState() {
        configManager.save();
        profileManager.saveActiveProfileSilently();
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

    private void clampToScreen(DraggableHudModule hud) {
        int hudWidth = Math.max(1, hud.getHudWidth());
        int hudHeight = Math.max(1, hud.getHudHeight());
        int maxX = Math.max(0, width - hudWidth);
        int maxY = Math.max(0, height - hudHeight);
        int x = Math.max(0, Math.min(maxX, hud.getPositionX()));
        int y = Math.max(0, Math.min(maxY, hud.getPositionY()));

        if (x != hud.getPositionX() || y != hud.getPositionY()) {
            hud.setPosition(x, y);
        }
    }

    private String getToolbarText() {
        if (selectedHud instanceof Module) {
            return "Selected: "
                + ((Module) selectedHud).getName()
                + "  |  ESC to save";
        }
        return "HUD Editor  |  Drag a module  |  ESC to save";
    }
}
