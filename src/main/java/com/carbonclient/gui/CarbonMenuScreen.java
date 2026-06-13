package com.carbonclient.gui;

import com.carbonclient.common.Reference;
import com.carbonclient.config.ConfigManager;
import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleCategory;
import com.carbonclient.module.ModuleManager;
import com.carbonclient.setting.Setting;
import com.carbonclient.setting.impl.BooleanSetting;
import com.carbonclient.setting.impl.ColorSetting;
import com.carbonclient.setting.impl.KeybindSetting;
import com.carbonclient.setting.impl.ModeSetting;
import com.carbonclient.setting.impl.NumberSetting;
import com.carbonclient.ui.component.ButtonComponent;
import com.carbonclient.ui.component.CardComponent;
import com.carbonclient.ui.component.ColorPickerComponent;
import com.carbonclient.ui.component.SliderComponent;
import com.carbonclient.ui.component.ToggleComponent;
import com.carbonclient.ui.render.RenderUtils;
import com.carbonclient.ui.theme.CarbonTheme;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public final class CarbonMenuScreen extends GuiScreen {

    private static final int PANEL_WIDTH = 500;
    private static final int PANEL_HEIGHT = 410;
    private static final int HEADER_HEIGHT = 46;
    private static final int FOOTER_HEIGHT = 28;
    private static final int PADDING = 16;
    private static final int GRID_COLUMNS = 3;
    private static final int CARD_GAP = 10;
    private static final int CARD_HEIGHT = 126;
    private static final int BUTTON_HEIGHT = 18;
    private static final int SETTING_ROW_HEIGHT = 31;
    private static final int CONTROL_WIDTH = 150;
    private final ModuleManager moduleManager;
    private final ConfigManager configManager;
    private final ButtonComponent buttonComponent = new ButtonComponent();
    private final ToggleComponent toggleComponent = new ToggleComponent();
    private final CardComponent cardComponent = new CardComponent();
    private final ColorPickerComponent colorPickerComponent =
        new ColorPickerComponent();
    private final SliderComponent sliderComponent = new SliderComponent();
    private Module selectedModule;
    private NumberSetting draggingSlider;
    private KeybindSetting listeningKeybind;
    private ColorSetting openColorSetting;
    private String colorHexInput = "";
    private boolean colorHexFocused;
    private int colorPickerDrag;
    private int optionsScrollIndex;
    private int moduleScrollRow;
    private boolean settingsTab;
    private boolean resetAllConfirmation;

    public CarbonMenuScreen(
        ModuleManager moduleManager,
        ConfigManager configManager
    ) {
        if (moduleManager == null || configManager == null) {
            throw new IllegalArgumentException(
                "ModuleManager and ConfigManager cannot be null."
            );
        }

        this.moduleManager = moduleManager;
        this.configManager = configManager;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtils.drawPanel(0, 0, width, height, CarbonTheme.OVERLAY);

        int panelWidth = getPanelWidth();
        int panelHeight = getPanelHeight();
        int panelX = getPanelX(panelWidth);
        int panelY = getPanelY(panelHeight);
        int panelRight = panelX + panelWidth;
        int panelBottom = panelY + panelHeight;
        int headerBottom = panelY + HEADER_HEIGHT;
        int footerTop = panelBottom - FOOTER_HEIGHT;

        RenderUtils.drawOutline(
            panelX,
            panelY,
            panelWidth,
            panelHeight,
            CarbonTheme.BORDER
        );
        RenderUtils.drawPanel(
            panelX,
            panelY,
            panelWidth,
            HEADER_HEIGHT,
            CarbonTheme.PANEL
        );
        RenderUtils.drawPanel(
            panelX,
            headerBottom,
            panelWidth,
            footerTop - headerBottom,
            CarbonTheme.BACKGROUND
        );
        RenderUtils.drawPanel(
            panelX,
            footerTop,
            panelWidth,
            FOOTER_HEIGHT,
            CarbonTheme.PANEL
        );
        RenderUtils.drawPanel(
            panelX,
            panelY,
            3,
            HEADER_HEIGHT,
            CarbonTheme.PRIMARY
        );

        RenderUtils.drawText(
            fontRendererObj,
            "CARBON CLIENT",
            panelX + PADDING,
            panelY + 18,
            CarbonTheme.TEXT
        );

        if (selectedModule == null) {
            int tabsX = panelX + 150;
            drawTab("Mods", tabsX, panelY, !settingsTab);
            drawTab("Settings", tabsX + 55, panelY, settingsTab);
            drawTab("Profiles", tabsX + 130, panelY, false);
            drawTab("HUD Editor", tabsX + 205, panelY, false);
            if (settingsTab) {
                drawSettingsView(
                    mouseX,
                    mouseY,
                    panelX,
                    headerBottom
                );
            } else {
                drawModuleCards(
                    mouseX,
                    mouseY,
                    panelX,
                    headerBottom,
                    panelWidth
                );
            }
        } else {
            drawOptionsView(mouseX, mouseY, panelX, panelY, headerBottom, panelWidth);
        }

        if (openColorSetting != null) {
            colorPickerComponent.render(
                fontRendererObj,
                openColorSetting,
                colorHexInput,
                colorHexFocused,
                getColorPickerX(panelX, panelWidth),
                getColorPickerY(panelY),
                mouseX,
                mouseY
            );
        }

        RenderUtils.drawText(
            fontRendererObj,
            "Version: " + Reference.VERSION,
            panelX + PADDING,
            footerTop + 10,
            CarbonTheme.MUTED_TEXT
        );

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawTab(String label, int x, int panelY, boolean selected) {
        int color = selected ? CarbonTheme.TEXT : CarbonTheme.MUTED_TEXT;
        int textY = panelY + 18;

        RenderUtils.drawText(fontRendererObj, label, x, textY, color);

        if (selected) {
            int underlineY = panelY + HEADER_HEIGHT - 3;
            RenderUtils.drawPanel(
                x,
                underlineY,
                fontRendererObj.getStringWidth(label),
                2,
                CarbonTheme.PRIMARY
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
        int panelY = contentTop - HEADER_HEIGHT;
        int visibleRows = getVisibleModuleRowCount(panelY, contentTop);
        int firstIndex = moduleScrollRow * GRID_COLUMNS;
        int endIndex = Math.min(
            modules.size(),
            firstIndex + visibleRows * GRID_COLUMNS
        );

        for (int index = firstIndex; index < endIndex; index++) {
            Module module = modules.get(index);
            int column = index % GRID_COLUMNS;
            int row = index / GRID_COLUMNS - moduleScrollRow;
            int cardX = panelX + PADDING + column * (cardWidth + CARD_GAP);
            int cardY = contentTop + PADDING + row * (CARD_HEIGHT + CARD_GAP);

            drawModuleCard(module, mouseX, mouseY, cardX, cardY, cardWidth);
        }

        int totalRows = getModuleRowCount(modules.size());
        if (totalRows > visibleRows) {
            String scrollText = "Mouse wheel: "
                + (moduleScrollRow + 1)
                + "/"
                + (totalRows - visibleRows + 1);
            int footerTop = panelY + getPanelHeight() - FOOTER_HEIGHT;
            RenderUtils.drawText(
                fontRendererObj,
                scrollText,
                panelX + panelWidth - PADDING
                    - fontRendererObj.getStringWidth(scrollText),
                footerTop - 13,
                CarbonTheme.MUTED_TEXT
            );
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
        int accent = module.isEnabled()
            ? CarbonTheme.ACCENT
            : CarbonTheme.PRIMARY;
        cardComponent.render(
            x,
            y,
            width,
            CARD_HEIGHT,
            mouseX,
            mouseY,
            accent
        );

        RenderUtils.drawText(
            fontRendererObj,
            module.getName(),
            x + 10,
            y + 12,
            CarbonTheme.TEXT
        );
        RenderUtils.drawText(
            fontRendererObj,
            module.getCategory().name(),
            x + 10,
            y + 30,
            CarbonTheme.MUTED_TEXT
        );

        String status = module.isEnabled() ? "ENABLED" : "DISABLED";
        RenderUtils.drawText(fontRendererObj, status, x + 10, y + 50, accent);

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
            CarbonTheme.SECONDARY
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
        buttonComponent.render(
            fontRendererObj,
            label,
            x,
            y,
            width,
            BUTTON_HEIGHT,
            mouseX,
            mouseY,
            accent
        );
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

        RenderUtils.drawText(
            fontRendererObj,
            selectedModule.getName() + " Options",
            panelX + PADDING,
            contentTop + 12,
            CarbonTheme.TEXT
        );
        drawButton(
            "Reset to Defaults",
            panelX + panelWidth - PADDING - 130,
            contentTop + 7,
            130,
            mouseX,
            mouseY,
            CarbonTheme.PRIMARY
        );

        List<Setting<?>> settings = getVisibleSettings();
        int rowX = panelX + PADDING;
        int rowWidth = panelWidth - PADDING * 2;
        int rowY = contentTop + 32;
        int visibleCount = getVisibleSettingCount(panelY, rowY);
        int endIndex = Math.min(
            settings.size(),
            optionsScrollIndex + visibleCount
        );

        if (settings.isEmpty()) {
            RenderUtils.drawText(
                fontRendererObj,
                "This module has no settings.",
                rowX,
                rowY + 8,
                CarbonTheme.MUTED_TEXT
            );
            return;
        }

        for (int index = optionsScrollIndex; index < endIndex; index++) {
            drawSettingRow(
                settings.get(index),
                mouseX,
                mouseY,
                rowX,
                rowY + (index - optionsScrollIndex) * SETTING_ROW_HEIGHT,
                rowWidth
            );
        }

        if (settings.size() > visibleCount) {
            RenderUtils.drawText(
                fontRendererObj,
                "Mouse wheel: more options",
                rowX,
                panelY + getPanelHeight() - FOOTER_HEIGHT - 13,
                CarbonTheme.MUTED_TEXT
            );
        }
    }

    private void drawSettingsView(
        int mouseX,
        int mouseY,
        int panelX,
        int contentTop
    ) {
        RenderUtils.drawText(
            fontRendererObj,
            "Client Settings",
            panelX + PADDING,
            contentTop + 18,
            CarbonTheme.TEXT
        );
        RenderUtils.drawText(
            fontRendererObj,
            "Restore every module, keybind and HUD position.",
            panelX + PADDING,
            contentTop + 38,
            CarbonTheme.MUTED_TEXT
        );

        drawButton(
            resetAllConfirmation
                ? "Click again to confirm"
                : "Reset All Settings",
            panelX + PADDING,
            contentTop + 58,
            180,
            mouseX,
            mouseY,
            CarbonTheme.PRIMARY
        );
    }

    private void drawBackButton(int mouseX, int mouseY, int x, int y) {
        drawButton(
            "< Back",
            x,
            y,
            60,
            mouseX,
            mouseY,
            CarbonTheme.PRIMARY
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
        RenderUtils.drawPanel(
            x,
            y,
            width,
            SETTING_ROW_HEIGHT - 4,
            CarbonTheme.CARD
        );
        RenderUtils.drawPanel(
            x,
            y,
            2,
            SETTING_ROW_HEIGHT - 4,
            CarbonTheme.SECONDARY
        );
        RenderUtils.drawText(
            fontRendererObj,
            setting.getName(),
            x + 10,
            y + 11,
            CarbonTheme.TEXT
        );

        int controlX = x + width - CONTROL_WIDTH - 10;
        int controlY = y + 6;

        if (setting instanceof BooleanSetting) {
            BooleanSetting booleanSetting = (BooleanSetting) setting;
            toggleComponent.render(
                fontRendererObj,
                booleanSetting.isEnabled(),
                controlX + 80,
                controlY,
                70,
                BUTTON_HEIGHT,
                mouseX,
                mouseY
            );
        } else if (setting instanceof KeybindSetting) {
            KeybindSetting keybindSetting = (KeybindSetting) setting;
            drawButton(
                listeningKeybind == keybindSetting
                    ? "Press a key..."
                    : keybindSetting.getKeyName(),
                controlX,
                controlY,
                CONTROL_WIDTH,
                mouseX,
                mouseY,
                listeningKeybind == keybindSetting
                    ? CarbonTheme.PRIMARY
                    : CarbonTheme.SECONDARY
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
                CarbonTheme.SECONDARY
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
        int sliderWidth = 105;
        double range = setting.getMaximum() - setting.getMinimum();
        double progress = range == 0.0D
            ? 0.0D
            : (setting.getValue() - setting.getMinimum()) / range;

        String value = formatNumber(setting.getValue());
        sliderComponent.render(
            fontRendererObj,
            value,
            progress,
            x,
            y,
            sliderWidth,
            CONTROL_WIDTH
        );
    }

    private void drawColorSetting(
        ColorSetting setting,
        int x,
        int y,
        int mouseX,
        int mouseY
    ) {
        RenderUtils.drawButton(
            x,
            y,
            CONTROL_WIDTH,
            BUTTON_HEIGHT,
            isInside(mouseX, mouseY, x, y, CONTROL_WIDTH, BUTTON_HEIGHT),
            CarbonTheme.SECONDARY
        );
        RenderUtils.drawPanel(
            x + 4,
            y + 4,
            16,
            BUTTON_HEIGHT - 8,
            setting.getColor()
        );

        String value = setting.getHexColor();
        RenderUtils.drawText(
            fontRendererObj,
            value,
            x + 27,
            y + 5,
            CarbonTheme.TEXT
        );
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
        throws IOException {
        if (mouseButton == 0) {
            if (openColorSetting != null) {
                handleColorPickerClick(mouseX, mouseY);
                return;
            }
            if (selectedModule == null && handleMainTabClick(mouseX, mouseY)) {
                return;
            }
            if (selectedModule == null && handleHudEditorTabClick(mouseX, mouseY)) {
                return;
            }
            if (selectedModule == null
                && settingsTab
                && handleResetAllClick(mouseX, mouseY)) {
                return;
            }
            if (selectedModule != null && handleOptionsClick(mouseX, mouseY)) {
                return;
            }
            if (selectedModule == null
                && !settingsTab
                && handleModuleCardClick(mouseX, mouseY)) {
                return;
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private boolean handleMainTabClick(int mouseX, int mouseY) {
        int panelWidth = getPanelWidth();
        int panelHeight = getPanelHeight();
        int panelX = getPanelX(panelWidth);
        int panelY = getPanelY(panelHeight);
        int tabsX = panelX + 150;

        if (isInside(
            mouseX,
            mouseY,
            tabsX,
            panelY + 12,
            fontRendererObj.getStringWidth("Mods"),
            22
        )) {
            settingsTab = false;
            resetAllConfirmation = false;
            return true;
        }
        if (isInside(
            mouseX,
            mouseY,
            tabsX + 55,
            panelY + 12,
            fontRendererObj.getStringWidth("Settings"),
            22
        )) {
            settingsTab = true;
            resetAllConfirmation = false;
            return true;
        }

        return false;
    }

    private boolean handleResetAllClick(int mouseX, int mouseY) {
        int panelWidth = getPanelWidth();
        int panelHeight = getPanelHeight();
        int panelX = getPanelX(panelWidth);
        int contentTop = getPanelY(panelHeight) + HEADER_HEIGHT;

        if (!isInside(
            mouseX,
            mouseY,
            panelX + PADDING,
            contentTop + 58,
            180,
            BUTTON_HEIGHT
        )) {
            return false;
        }

        if (!resetAllConfirmation) {
            resetAllConfirmation = true;
            return true;
        }

        moduleManager.resetAllToDefaults();
        configManager.save();
        resetAllConfirmation = false;
        moduleScrollRow = 0;
        return true;
    }

    private boolean handleHudEditorTabClick(int mouseX, int mouseY) {
        int panelWidth = getPanelWidth();
        int panelHeight = getPanelHeight();
        int panelX = getPanelX(panelWidth);
        int panelY = getPanelY(panelHeight);
        int tabsX = panelX + 150;
        int hudEditorX = tabsX + 205;

        if (isInside(
            mouseX,
            mouseY,
            hudEditorX,
            panelY + 12,
            fontRendererObj.getStringWidth("HUD Editor"),
            22
        )) {
            mc.displayGuiScreen(
                new HudLayoutEditorScreen(moduleManager, configManager)
            );
            return true;
        }

        return false;
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
        int visibleRows = getVisibleModuleRowCount(panelY, contentTop);
        int firstIndex = moduleScrollRow * GRID_COLUMNS;
        int endIndex = Math.min(
            modules.size(),
            firstIndex + visibleRows * GRID_COLUMNS
        );

        for (int index = firstIndex; index < endIndex; index++) {
            Module module = modules.get(index);
            int column = index % GRID_COLUMNS;
            int row = index / GRID_COLUMNS - moduleScrollRow;
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
                optionsScrollIndex = 0;
                closeColorPicker();
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
            listeningKeybind = null;
            closeColorPicker();
            optionsScrollIndex = 0;
            return true;
        }
        if (isInside(
            mouseX,
            mouseY,
            panelX + panelWidth - PADDING - 130,
            contentTop + 7,
            130,
            BUTTON_HEIGHT
        )) {
            selectedModule.resetToDefaults();
            configManager.save();
            draggingSlider = null;
            listeningKeybind = null;
            closeColorPicker();
            optionsScrollIndex = 0;
            return true;
        }

        List<Setting<?>> settings = getVisibleSettings();
        int rowX = panelX + PADDING;
        int rowWidth = panelWidth - PADDING * 2;
        int rowY = contentTop + 32;
        int visibleCount = getVisibleSettingCount(panelY, rowY);
        int endIndex = Math.min(
            settings.size(),
            optionsScrollIndex + visibleCount
        );

        for (int index = optionsScrollIndex; index < endIndex; index++) {
            Setting<?> setting = settings.get(index);
            int settingY = rowY
                + (index - optionsScrollIndex) * SETTING_ROW_HEIGHT;
            int controlX = rowX + rowWidth - CONTROL_WIDTH - 10;
            int controlY = settingY + 6;

            if (setting instanceof BooleanSetting
                && isInside(mouseX, mouseY, controlX + 80, controlY, 70, BUTTON_HEIGHT)) {
                ((BooleanSetting) setting).toggle();
                return true;
            }
            if (setting instanceof KeybindSetting
                && isInside(
                    mouseX,
                    mouseY,
                    controlX,
                    controlY,
                    CONTROL_WIDTH,
                    BUTTON_HEIGHT
                )) {
                listeningKeybind = (KeybindSetting) setting;
                draggingSlider = null;
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
                openColorPicker((ColorSetting) setting);
                return true;
            }
        }

        return false;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int wheel = Mouse.getEventDWheel();
        if (wheel == 0) {
            return;
        }

        if (selectedModule == null && !settingsTab) {
            List<Module> modules = getVisibleModules();
            int panelY = getPanelY(getPanelHeight());
            int contentTop = panelY + HEADER_HEIGHT;
            int visibleRows = getVisibleModuleRowCount(panelY, contentTop);
            int maximumRow = Math.max(
                0,
                getModuleRowCount(modules.size()) - visibleRows
            );

            if (wheel < 0) {
                moduleScrollRow = Math.min(maximumRow, moduleScrollRow + 1);
            } else {
                moduleScrollRow = Math.max(0, moduleScrollRow - 1);
            }
            return;
        }
        if (selectedModule == null) {
            return;
        }

        List<Setting<?>> settings = getVisibleSettings();
        int panelY = getPanelY(getPanelHeight());
        int rowY = panelY + HEADER_HEIGHT + 32;
        int visibleCount = getVisibleSettingCount(panelY, rowY);
        int maximumIndex = Math.max(0, settings.size() - visibleCount);

        if (wheel < 0) {
            optionsScrollIndex = Math.min(maximumIndex, optionsScrollIndex + 1);
        } else {
            optionsScrollIndex = Math.max(0, optionsScrollIndex - 1);
        }
    }

    @Override
    protected void mouseClickMove(
        int mouseX,
        int mouseY,
        int clickedMouseButton,
        long timeSinceLastClick
    ) {
        if (clickedMouseButton == 0
            && openColorSetting != null
            && colorPickerDrag != 0) {
            int panelWidth = getPanelWidth();
            int pickerX = getColorPickerX(getPanelX(panelWidth), panelWidth);
            int pickerY = getColorPickerY(getPanelY(getPanelHeight()));

            if (colorPickerDrag == 1) {
                colorPickerComponent.updateSaturationBrightness(
                    openColorSetting,
                    mouseX,
                    mouseY,
                    pickerX,
                    pickerY
                );
                updateColorHexDisplay();
            } else if (colorPickerDrag == 2) {
                colorPickerComponent.updateHue(openColorSetting, mouseY, pickerY);
                updateColorHexDisplay();
            } else if (colorPickerDrag == 3) {
                colorPickerComponent.updateAlpha(openColorSetting, mouseY, pickerY);
            } else if (colorPickerDrag == 4) {
                colorPickerComponent.updateSpeed(
                    openColorSetting,
                    mouseX,
                    pickerX
                );
            }
            return;
        }

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
        colorPickerDrag = 0;
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

    private void openColorPicker(ColorSetting setting) {
        openColorSetting = setting;
        colorHexInput = setting.getHexColor();
        colorHexFocused = false;
        draggingSlider = null;
        listeningKeybind = null;
    }

    private void closeColorPicker() {
        openColorSetting = null;
        colorHexInput = "";
        colorHexFocused = false;
        colorPickerDrag = 0;
    }

    private void handleColorPickerClick(int mouseX, int mouseY) {
        int panelWidth = getPanelWidth();
        int panelX = getPanelX(panelWidth);
        int panelY = getPanelY(getPanelHeight());
        int pickerX = getColorPickerX(panelX, panelWidth);
        int pickerY = getColorPickerY(panelY);

        if (colorPickerComponent.isSaturationBrightness(
            mouseX,
            mouseY,
            pickerX,
            pickerY
        )) {
            colorPickerDrag = 1;
            colorPickerComponent.updateSaturationBrightness(
                openColorSetting,
                mouseX,
                mouseY,
                pickerX,
                pickerY
            );
            updateColorHexDisplay();
            return;
        }
        if (colorPickerComponent.isHueSlider(
            mouseX,
            mouseY,
            pickerX,
            pickerY
        )) {
            colorPickerDrag = 2;
            colorPickerComponent.updateHue(openColorSetting, mouseY, pickerY);
            updateColorHexDisplay();
            return;
        }
        if (colorPickerComponent.isAlphaSlider(
            mouseX,
            mouseY,
            pickerX,
            pickerY
        )) {
            colorPickerDrag = 3;
            colorPickerComponent.updateAlpha(openColorSetting, mouseY, pickerY);
            return;
        }
        if (colorPickerComponent.isSpeedSlider(
            mouseX,
            mouseY,
            pickerX,
            pickerY
        )) {
            colorPickerDrag = 4;
            colorPickerComponent.updateSpeed(openColorSetting, mouseX, pickerX);
            return;
        }

        int preset = colorPickerComponent.getPresetAt(
            mouseX,
            mouseY,
            pickerX,
            pickerY
        );

        if (preset != -1) {
            openColorSetting.setBaseColor(preset);
            updateColorHexDisplay();
            colorHexFocused = false;
            return;
        }

        if (colorPickerComponent.isHexInputHovered(
            mouseX,
            mouseY,
            pickerX,
            pickerY
        )) {
            colorHexFocused = true;
            colorHexInput = "#";
            return;
        }

        if (colorPickerComponent.isChromaHovered(
            mouseX,
            mouseY,
            pickerX,
            pickerY
        )) {
            openColorSetting.setChroma(!openColorSetting.isChroma());
            colorHexFocused = false;
            return;
        }

        if (colorPickerComponent.isTypeHovered(
            mouseX,
            mouseY,
            pickerX,
            pickerY
        )) {
            openColorSetting.cycleType();
            colorHexFocused = false;
            return;
        }

        if (!colorPickerComponent.isInsidePanel(
            mouseX,
            mouseY,
            pickerX,
            pickerY
        )) {
            closeColorPicker();
        }
    }

    private int getColorPickerX(int panelX, int panelWidth) {
        return panelX + panelWidth - ColorPickerComponent.WIDTH - PADDING;
    }

    private int getColorPickerY(int panelY) {
        return panelY + HEADER_HEIGHT + 20;
    }

    private List<Setting<?>> getVisibleSettings() {
        List<Setting<?>> visibleSettings = new ArrayList<Setting<?>>();

        for (Setting<?> setting : selectedModule.getSettings()) {
            if (setting.isVisibleInOptions()) {
                visibleSettings.add(setting);
            }
        }

        return visibleSettings;
    }

    private int getVisibleSettingCount(int panelY, int rowY) {
        int footerTop = panelY + getPanelHeight() - FOOTER_HEIGHT;
        return Math.max(1, (footerTop - rowY - 16) / SETTING_ROW_HEIGHT);
    }

    private int getVisibleModuleRowCount(int panelY, int contentTop) {
        int footerTop = panelY + getPanelHeight() - FOOTER_HEIGHT;
        int availableHeight = footerTop - contentTop - PADDING * 2;
        return Math.max(
            1,
            (availableHeight + CARD_GAP) / (CARD_HEIGHT + CARD_GAP)
        );
    }

    private int getModuleRowCount(int moduleCount) {
        return (moduleCount + GRID_COLUMNS - 1) / GRID_COLUMNS;
    }

    private String formatNumber(double value) {
        return String.format("%.1f", value);
    }

    private List<Module> getVisibleModules() {
        List<Module> visibleModules = new ArrayList<Module>();

        for (Module module : moduleManager.getModules()) {
            if (module.getCategory() == ModuleCategory.RENDER
                || module.getCategory() == ModuleCategory.HUD
                || module.getCategory() == ModuleCategory.MOVEMENT) {
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
        if (openColorSetting != null) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                closeColorPicker();
                return;
            }
            if (colorHexFocused) {
                if (keyCode == Keyboard.KEY_RETURN
                    || keyCode == Keyboard.KEY_NUMPADENTER) {
                    if (openColorSetting.setHexColor(colorHexInput)) {
                        colorHexInput = openColorSetting.getHexColor();
                    }
                    colorHexFocused = false;
                    return;
                }
                if (keyCode == Keyboard.KEY_BACK) {
                    if (!colorHexInput.isEmpty()) {
                        colorHexInput = colorHexInput.substring(
                            0,
                            colorHexInput.length() - 1
                        );
                    }
                    applyHexInputIfValid();
                    return;
                }
                if (isHexCharacter(typedChar) && colorHexInput.length() < 7) {
                    colorHexInput += Character.toUpperCase(typedChar);
                    applyHexInputIfValid();
                }
                return;
            }
        }

        if (listeningKeybind != null) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                listeningKeybind = null;
                return;
            }

            listeningKeybind.setValue(
                keyCode == Keyboard.KEY_DELETE
                    ? Keyboard.KEY_NONE
                    : keyCode
            );
            listeningKeybind = null;
            return;
        }

        if (keyCode == Keyboard.KEY_ESCAPE && selectedModule != null) {
            selectedModule = null;
            draggingSlider = null;
            listeningKeybind = null;
            closeColorPicker();
            optionsScrollIndex = 0;
            return;
        }
        if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_RSHIFT) {
            mc.displayGuiScreen(null);
            return;
        }

        super.keyTyped(typedChar, keyCode);
    }

    private void applyHexInputIfValid() {
        if (openColorSetting.setHexColor(colorHexInput)) {
            updateColorHexDisplay();
        }
    }

    private void updateColorHexDisplay() {
        colorHexInput = openColorSetting.getHexColor();
    }

    private boolean isHexCharacter(char character) {
        return character == '#'
            || character >= '0' && character <= '9'
            || character >= 'a' && character <= 'f'
            || character >= 'A' && character <= 'F';
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
