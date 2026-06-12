package com.carbonclient.gui;

import com.carbonclient.common.Reference;
import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleCategory;
import com.carbonclient.module.ModuleManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public final class CarbonMenuScreen extends GuiScreen {

    private static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);

    private static final int OVERLAY_COLOR = 0xC0080B14;
    private static final int BACKGROUND_COLOR = 0xFF0D1220;
    private static final int PANEL_COLOR = 0xFF121A2E;
    private static final int CARD_COLOR = 0xE0182238;
    private static final int CARD_HOVER_COLOR = 0xF01C2942;
    private static final int BORDER_COLOR = 0xFF273653;
    private static final int PRIMARY_ACCENT = 0xFFFF4FA3;
    private static final int SECONDARY_ACCENT = 0xFF4DA6FF;
    private static final int CYAN_GLOW = 0xFF6EE7FF;
    private static final int PRIMARY_TEXT_COLOR = 0xFFFFFFFF;
    private static final int SECONDARY_TEXT_COLOR = 0xFF9AA8BE;
    private static final int BUTTON_COLOR = 0xFF202D49;
    private static final int BUTTON_HOVER_COLOR = 0xFF2A3B5E;

    private static final int PANEL_WIDTH = 500;
    private static final int PANEL_HEIGHT = 300;
    private static final int HEADER_HEIGHT = 46;
    private static final int FOOTER_HEIGHT = 28;
    private static final int PADDING = 16;
    private static final int GRID_COLUMNS = 3;
    private static final int CARD_GAP = 10;
    private static final int CARD_HEIGHT = 126;
    private static final int BUTTON_HEIGHT = 18;

    private final ModuleManager moduleManager;

    public CarbonMenuScreen(ModuleManager moduleManager) {
        if (moduleManager == null) {
            throw new IllegalArgumentException("ModuleManager cannot be null.");
        }

        this.moduleManager = moduleManager;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(0, 0, width, height, OVERLAY_COLOR);

        int panelWidth = getPanelWidth();
        int panelHeight = getPanelHeight();
        int panelX = getPanelX(panelWidth);
        int panelY = getPanelY(panelHeight);
        int panelRight = panelX + panelWidth;
        int panelBottom = panelY + panelHeight;
        int headerBottom = panelY + HEADER_HEIGHT;
        int footerTop = panelBottom - FOOTER_HEIGHT;

        Gui.drawRect(panelX - 1, panelY - 1, panelRight + 1, panelBottom + 1, BORDER_COLOR);
        Gui.drawRect(panelX, panelY, panelRight, headerBottom, PANEL_COLOR);
        Gui.drawRect(panelX, headerBottom, panelRight, footerTop, BACKGROUND_COLOR);
        Gui.drawRect(panelX, footerTop, panelRight, panelBottom, PANEL_COLOR);
        Gui.drawRect(panelX, panelY, panelX + 3, headerBottom, PRIMARY_ACCENT);

        fontRendererObj.drawString(
            "CARBON CLIENT",
            panelX + PADDING,
            panelY + 18,
            PRIMARY_TEXT_COLOR
        );

        int tabsX = panelX + 190;
        drawTab("Mods", tabsX, panelY, true);
        drawTab("Settings", tabsX + 70, panelY, false);
        drawTab("Profiles", tabsX + 160, panelY, false);

        drawModuleCards(mouseX, mouseY, panelX, headerBottom, panelWidth);

        fontRendererObj.drawString(
            "Version: " + Reference.VERSION,
            panelX + PADDING,
            footerTop + 10,
            SECONDARY_TEXT_COLOR
        );

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawTab(String label, int x, int panelY, boolean selected) {
        int color = selected ? PRIMARY_TEXT_COLOR : SECONDARY_TEXT_COLOR;
        int textY = panelY + 18;

        fontRendererObj.drawString(label, x, textY, color);

        if (selected) {
            int underlineY = panelY + HEADER_HEIGHT - 3;
            Gui.drawRect(
                x,
                underlineY,
                x + fontRendererObj.getStringWidth(label),
                underlineY + 2,
                PRIMARY_ACCENT
            );
        }
    }

    private void drawModuleCards(
        int mouseX,
        int mouseY,
        int panelX,
        int contentTop,
        int panelWidth
    ) {
        List<Module> modules = getVisibleModules();
        int gridWidth = panelWidth - PADDING * 2;
        int cardWidth = (gridWidth - CARD_GAP * (GRID_COLUMNS - 1)) / GRID_COLUMNS;

        for (int index = 0; index < modules.size(); index++) {
            Module module = modules.get(index);
            int column = index % GRID_COLUMNS;
            int row = index / GRID_COLUMNS;
            int cardX = panelX + PADDING + column * (cardWidth + CARD_GAP);
            int cardY = contentTop + PADDING + row * (CARD_HEIGHT + CARD_GAP);

            drawModuleCard(module, mouseX, mouseY, cardX, cardY, cardWidth);
        }
    }

    private void drawModuleCard(
        Module module,
        int mouseX,
        int mouseY,
        int x,
        int y,
        int width
    ) {
        boolean hovered = isInside(mouseX, mouseY, x, y, width, CARD_HEIGHT);
        int accent = module.isEnabled() ? CYAN_GLOW : PRIMARY_ACCENT;

        Gui.drawRect(x - 1, y - 1, x + width + 1, y + CARD_HEIGHT + 1, BORDER_COLOR);
        Gui.drawRect(
            x,
            y,
            x + width,
            y + CARD_HEIGHT,
            hovered ? CARD_HOVER_COLOR : CARD_COLOR
        );
        Gui.drawRect(x, y, x + 3, y + CARD_HEIGHT, accent);

        fontRendererObj.drawString(
            module.getName(),
            x + 10,
            y + 12,
            PRIMARY_TEXT_COLOR
        );
        fontRendererObj.drawString(
            module.getCategory().name(),
            x + 10,
            y + 30,
            SECONDARY_TEXT_COLOR
        );

        String status = module.isEnabled() ? "ENABLED" : "DISABLED";
        fontRendererObj.drawString(status, x + 10, y + 50, accent);

        int buttonY = y + CARD_HEIGHT - BUTTON_HEIGHT - 10;
        int buttonGap = 6;
        int buttonWidth = (width - 20 - buttonGap) / 2;
        int toggleX = x + 10;
        int optionsX = toggleX + buttonWidth + buttonGap;

        drawButton(
            module.isEnabled() ? "Disable" : "Enable",
            toggleX,
            buttonY,
            buttonWidth,
            mouseX,
            mouseY,
            accent
        );
        drawButton(
            "Options",
            optionsX,
            buttonY,
            buttonWidth,
            mouseX,
            mouseY,
            SECONDARY_ACCENT
        );
    }

    private void drawButton(
        String label,
        int x,
        int y,
        int width,
        int mouseX,
        int mouseY,
        int accent
    ) {
        boolean hovered = isInside(mouseX, mouseY, x, y, width, BUTTON_HEIGHT);

        Gui.drawRect(
            x,
            y,
            x + width,
            y + BUTTON_HEIGHT,
            hovered ? BUTTON_HOVER_COLOR : BUTTON_COLOR
        );
        Gui.drawRect(x, y + BUTTON_HEIGHT - 2, x + width, y + BUTTON_HEIGHT, accent);

        int textX = x + (width - fontRendererObj.getStringWidth(label)) / 2;
        int textY = y + (BUTTON_HEIGHT - fontRendererObj.FONT_HEIGHT) / 2;
        fontRendererObj.drawString(label, textX, textY, PRIMARY_TEXT_COLOR);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
        throws IOException {
        if (mouseButton == 0 && handleModuleCardClick(mouseX, mouseY)) {
            return;
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private boolean handleModuleCardClick(int mouseX, int mouseY) {
        int panelWidth = getPanelWidth();
        int panelHeight = getPanelHeight();
        int panelX = getPanelX(panelWidth);
        int panelY = getPanelY(panelHeight);
        int contentTop = panelY + HEADER_HEIGHT;
        int gridWidth = panelWidth - PADDING * 2;
        int cardWidth = (gridWidth - CARD_GAP * (GRID_COLUMNS - 1)) / GRID_COLUMNS;
        List<Module> modules = getVisibleModules();

        for (int index = 0; index < modules.size(); index++) {
            Module module = modules.get(index);
            int column = index % GRID_COLUMNS;
            int row = index / GRID_COLUMNS;
            int cardX = panelX + PADDING + column * (cardWidth + CARD_GAP);
            int cardY = contentTop + PADDING + row * (CARD_HEIGHT + CARD_GAP);
            int buttonY = cardY + CARD_HEIGHT - BUTTON_HEIGHT - 10;
            int buttonGap = 6;
            int buttonWidth = (cardWidth - 20 - buttonGap) / 2;
            int toggleX = cardX + 10;
            int optionsX = toggleX + buttonWidth + buttonGap;

            if (isInside(mouseX, mouseY, toggleX, buttonY, buttonWidth, BUTTON_HEIGHT)) {
                module.toggle();
                return true;
            }
            if (isInside(mouseX, mouseY, optionsX, buttonY, buttonWidth, BUTTON_HEIGHT)) {
                LOGGER.info("Options requested for module: {}", module.getName());
                return true;
            }
        }

        return false;
    }

    private List<Module> getVisibleModules() {
        List<Module> visibleModules = new ArrayList<Module>();

        for (Module module : moduleManager.getModules()) {
            if (module.getCategory() == ModuleCategory.RENDER
                || module.getCategory() == ModuleCategory.HUD) {
                visibleModules.add(module);
            }
        }

        return visibleModules;
    }

    private boolean isInside(
        int mouseX,
        int mouseY,
        int x,
        int y,
        int width,
        int height
    ) {
        return mouseX >= x
            && mouseX < x + width
            && mouseY >= y
            && mouseY < y + height;
    }

    private int getPanelWidth() {
        return Math.min(PANEL_WIDTH, width - 30);
    }

    private int getPanelHeight() {
        return Math.min(PANEL_HEIGHT, height - 30);
    }

    private int getPanelX(int panelWidth) {
        return (width - panelWidth) / 2;
    }

    private int getPanelY(int panelHeight) {
        return (height - panelHeight) / 2;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_RSHIFT) {
            mc.displayGuiScreen(null);
            return;
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
