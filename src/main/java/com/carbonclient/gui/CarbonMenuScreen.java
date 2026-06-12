package com.carbonclient.gui;

import com.carbonclient.common.Reference;
import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleCategory;
import com.carbonclient.module.ModuleManager;
import com.carbonclient.setting.Setting;
import com.carbonclient.setting.impl.BooleanSetting;
import com.carbonclient.setting.impl.ColorSetting;
import com.carbonclient.setting.impl.ModeSetting;
import com.carbonclient.setting.impl.NumberSetting;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public final class CarbonMenuScreen extends GuiScreen {

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
    private static final int SETTING_ROW_HEIGHT = 31;
    private static final int CONTROL_WIDTH = 150;
    private static final int[] COLOR_PRESETS = {
        0xFFFFFFFF,
        0xFF6EE7FF,
        0xFF4DA6FF,
        0xFFFF4FA3,
        0xB0121824,
        0xD0506078
    };

    private final ModuleManager moduleManager;
    private Module selectedModule;
    private NumberSetting draggingSlider;

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

        if (selectedModule == null) {
            int tabsX = panelX + 190;
            drawTab("Mods", tabsX, panelY, true);
            drawTab("Settings", tabsX + 70, panelY, false);
            drawTab("Profiles", tabsX + 160, panelY, false);
            drawModuleCards(mouseX, mouseY, panelX, headerBottom, panelWidth);
        } else {
            drawOptionsView(mouseX, mouseY, panelX, panelY, headerBottom, panelWidth);
        }

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

    private void drawOptionsView(
        int mouseX,
        int mouseY,
        int panelX,
        int panelY,
        int contentTop,
        int panelWidth
    ) {
        drawBackButton(mouseX, mouseY, panelX + panelWidth - 76, panelY + 14);

        fontRendererObj.drawString(
            selectedModule.getName() + " Options",
            panelX + PADDING,
            contentTop + 12,
            PRIMARY_TEXT_COLOR
        );

        List<Setting<?>> settings = selectedModule.getSettings();
        int rowX = panelX + PADDING;
        int rowWidth = panelWidth - PADDING * 2;
        int rowY = contentTop + 32;

        if (settings.isEmpty()) {
            fontRendererObj.drawString(
                "This module has no settings.",
                rowX,
                rowY + 8,
                SECONDARY_TEXT_COLOR
            );
            return;
        }

        for (int index = 0; index < settings.size(); index++) {
            drawSettingRow(
                settings.get(index),
                mouseX,
                mouseY,
                rowX,
                rowY + index * SETTING_ROW_HEIGHT,
                rowWidth
            );
        }
    }

    private void drawBackButton(int mouseX, int mouseY, int x, int y) {
        drawButton(
            "< Back",
            x,
            y,
            60,
            mouseX,
            mouseY,
            PRIMARY_ACCENT
        );
    }

    private void drawSettingRow(
        Setting<?> setting,
        int mouseX,
        int mouseY,
        int x,
        int y,
        int width
    ) {
        Gui.drawRect(x, y, x + width, y + SETTING_ROW_HEIGHT - 4, CARD_COLOR);
        Gui.drawRect(x, y, x + 2, y + SETTING_ROW_HEIGHT - 4, SECONDARY_ACCENT);
        fontRendererObj.drawString(
            setting.getName(),
            x + 10,
            y + 11,
            PRIMARY_TEXT_COLOR
        );

        int controlX = x + width - CONTROL_WIDTH - 10;
        int controlY = y + 6;

        if (setting instanceof BooleanSetting) {
            BooleanSetting booleanSetting = (BooleanSetting) setting;
            drawButton(
                booleanSetting.isEnabled() ? "ON" : "OFF",
                controlX + 80,
                controlY,
                70,
                mouseX,
                mouseY,
                booleanSetting.isEnabled() ? CYAN_GLOW : PRIMARY_ACCENT
            );
        } else if (setting instanceof NumberSetting) {
            drawNumberSetting(
                (NumberSetting) setting,
                controlX,
                controlY,
                mouseX,
                mouseY
            );
        } else if (setting instanceof ModeSetting) {
            ModeSetting modeSetting = (ModeSetting) setting;
            drawButton(
                modeSetting.getValue(),
                controlX,
                controlY,
                CONTROL_WIDTH,
                mouseX,
                mouseY,
                SECONDARY_ACCENT
            );
        } else if (setting instanceof ColorSetting) {
            drawColorSetting(
                (ColorSetting) setting,
                controlX,
                controlY,
                mouseX,
                mouseY
            );
        }
    }

    private void drawNumberSetting(
        NumberSetting setting,
        int x,
        int y,
        int mouseX,
        int mouseY
    ) {
        int sliderY = y + 8;
        int sliderWidth = 105;
        double range = setting.getMaximum() - setting.getMinimum();
        double progress = range == 0.0D
            ? 0.0D
            : (setting.getValue() - setting.getMinimum()) / range;
        int fillWidth = (int) Math.round(sliderWidth * progress);

        Gui.drawRect(x, sliderY, x + sliderWidth, sliderY + 4, BUTTON_COLOR);
        Gui.drawRect(x, sliderY, x + fillWidth, sliderY + 4, CYAN_GLOW);
        Gui.drawRect(
            x + fillWidth - 2,
            sliderY - 3,
            x + fillWidth + 2,
            sliderY + 7,
            PRIMARY_TEXT_COLOR
        );

        String value = formatNumber(setting.getValue());
        fontRendererObj.drawString(
            value,
            x + CONTROL_WIDTH - fontRendererObj.getStringWidth(value),
            y + 5,
            PRIMARY_TEXT_COLOR
        );
    }

    private void drawColorSetting(
        ColorSetting setting,
        int x,
        int y,
        int mouseX,
        int mouseY
    ) {
        boolean hovered = isInside(mouseX, mouseY, x, y, CONTROL_WIDTH, BUTTON_HEIGHT);
        Gui.drawRect(
            x,
            y,
            x + CONTROL_WIDTH,
            y + BUTTON_HEIGHT,
            hovered ? BUTTON_HOVER_COLOR : BUTTON_COLOR
        );
        Gui.drawRect(x + 4, y + 4, x + 20, y + BUTTON_HEIGHT - 4, setting.getColor());

        String value = String.format("#%08X", setting.getColor());
        fontRendererObj.drawString(value, x + 27, y + 5, PRIMARY_TEXT_COLOR);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
        throws IOException {
        if (mouseButton == 0) {
            if (selectedModule != null && handleOptionsClick(mouseX, mouseY)) {
                return;
            }
            if (selectedModule == null && handleModuleCardClick(mouseX, mouseY)) {
                return;
            }
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
                selectedModule = module;
                return true;
            }
        }

        return false;
    }

    private boolean handleOptionsClick(int mouseX, int mouseY) {
        int panelWidth = getPanelWidth();
        int panelHeight = getPanelHeight();
        int panelX = getPanelX(panelWidth);
        int panelY = getPanelY(panelHeight);
        int contentTop = panelY + HEADER_HEIGHT;

        if (isInside(mouseX, mouseY, panelX + panelWidth - 76, panelY + 14, 60, BUTTON_HEIGHT)) {
            selectedModule = null;
            draggingSlider = null;
            return true;
        }

        List<Setting<?>> settings = selectedModule.getSettings();
        int rowX = panelX + PADDING;
        int rowWidth = panelWidth - PADDING * 2;
        int rowY = contentTop + 32;

        for (int index = 0; index < settings.size(); index++) {
            Setting<?> setting = settings.get(index);
            int settingY = rowY + index * SETTING_ROW_HEIGHT;
            int controlX = rowX + rowWidth - CONTROL_WIDTH - 10;
            int controlY = settingY + 6;

            if (setting instanceof BooleanSetting
                && isInside(mouseX, mouseY, controlX + 80, controlY, 70, BUTTON_HEIGHT)) {
                ((BooleanSetting) setting).toggle();
                return true;
            }
            if (setting instanceof NumberSetting
                && isInside(mouseX, mouseY, controlX, controlY, 105, BUTTON_HEIGHT)) {
                draggingSlider = (NumberSetting) setting;
                updateSliderValue(draggingSlider, mouseX, controlX, 105);
                return true;
            }
            if (setting instanceof ModeSetting
                && isInside(mouseX, mouseY, controlX, controlY, CONTROL_WIDTH, BUTTON_HEIGHT)) {
                cycleMode((ModeSetting) setting);
                return true;
            }
            if (setting instanceof ColorSetting
                && isInside(mouseX, mouseY, controlX, controlY, CONTROL_WIDTH, BUTTON_HEIGHT)) {
                cycleColor((ColorSetting) setting);
                return true;
            }
        }

        return false;
    }

    @Override
    protected void mouseClickMove(
        int mouseX,
        int mouseY,
        int clickedMouseButton,
        long timeSinceLastClick
    ) {
        if (clickedMouseButton == 0 && draggingSlider != null && selectedModule != null) {
            int panelWidth = getPanelWidth();
            int panelX = getPanelX(panelWidth);
            int rowWidth = panelWidth - PADDING * 2;
            int controlX = panelX + PADDING + rowWidth - CONTROL_WIDTH - 10;
            updateSliderValue(draggingSlider, mouseX, controlX, 105);
        }

        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        draggingSlider = null;
        super.mouseReleased(mouseX, mouseY, state);
    }

    private void updateSliderValue(
        NumberSetting setting,
        int mouseX,
        int sliderX,
        int sliderWidth
    ) {
        double progress = (mouseX - sliderX) / (double) sliderWidth;
        progress = Math.max(0.0D, Math.min(1.0D, progress));
        double value = setting.getMinimum()
            + (setting.getMaximum() - setting.getMinimum()) * progress;
        setting.setValue(value);
    }

    private void cycleMode(ModeSetting setting) {
        List<String> modes = setting.getModes();
        int index = modes.indexOf(setting.getValue());
        int nextIndex = (index + 1) % modes.size();
        setting.setValue(modes.get(nextIndex));
    }

    private void cycleColor(ColorSetting setting) {
        int currentColor = setting.getColor();

        for (int index = 0; index < COLOR_PRESETS.length; index++) {
            if (COLOR_PRESETS[index] == currentColor) {
                setting.setValue(COLOR_PRESETS[(index + 1) % COLOR_PRESETS.length]);
                return;
            }
        }

        setting.setValue(COLOR_PRESETS[0]);
    }

    private String formatNumber(double value) {
        return String.format("%.1f", value);
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
        if (keyCode == Keyboard.KEY_ESCAPE && selectedModule != null) {
            selectedModule = null;
            draggingSlider = null;
            return;
        }
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
