package com.carbonclient.gui;

import com.carbonclient.common.Reference;
import com.carbonclient.config.ConfigManager;
import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleCategory;
import com.carbonclient.module.ModuleManager;
import com.carbonclient.modules.render.CrosshairModule;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public final class CarbonMenuScreen extends GuiScreen {

    private static final int PANEL_WIDTH = CarbonTheme.MENU_WIDTH;
    private static final int PANEL_HEIGHT = CarbonTheme.MENU_HEIGHT;
    private static final int HEADER_HEIGHT = CarbonTheme.HEADER_HEIGHT;
    private static final int FOOTER_HEIGHT = CarbonTheme.FOOTER_HEIGHT;
    private static final int PADDING = CarbonTheme.CONTENT_PADDING;
    private static final int GRID_COLUMNS = CarbonTheme.GRID_COLUMNS;
    private static final int CARD_GAP = CarbonTheme.CARD_GAP;
    private static final int CARD_HEIGHT = CarbonTheme.CARD_HEIGHT;
    private static final int BUTTON_HEIGHT = CarbonTheme.BUTTON_HEIGHT;
    private static final int SETTING_ROW_HEIGHT =
        CarbonTheme.SETTING_ROW_HEIGHT;
    private static final int CONTROL_WIDTH = CarbonTheme.CONTROL_WIDTH;
    private static final int TAB_START_OFFSET = 142;
    private static final int TAB_GAP = CarbonTheme.SPACE_4;
    private static final int SEARCH_HEIGHT = 24;
    private static final int MODULE_SCROLL_STEP = 36;
    private static final int MAX_SEARCH_LENGTH = 64;
    private static final String[] TAB_LABELS = {
        "Mods",
        "Settings",
        "Profiles",
        "HUD Editor"
    };
    private final ModuleManager moduleManager;
    private final ConfigManager configManager;
    private final ButtonComponent buttonComponent = new ButtonComponent();
    private final ToggleComponent toggleComponent = new ToggleComponent();
    private final CardComponent cardComponent = new CardComponent();
    private final ColorPickerComponent colorPickerComponent =
        new ColorPickerComponent();
    private final SliderComponent sliderComponent = new SliderComponent();
    private Module selectedModule;
    private Module listeningModuleKeybind;
    private NumberSetting draggingSlider;
    private KeybindSetting listeningKeybind;
    private ColorSetting openColorSetting;
    private String colorHexInput = "";
    private boolean colorHexFocused;
    private int colorPickerDrag;
    private int optionsScrollIndex;
    private int moduleScrollOffset;
    private int keybindScrollIndex;
    private String moduleSearchQuery = "";
    private boolean moduleSearchFocused;
    private ModuleFilter moduleFilter = ModuleFilter.ALL;
    private boolean settingsTab;
    private boolean resetAllConfirmation;
    private String crosshairPreviewBackground = "Dark";

    private enum ModuleFilter {
        ALL,
        HUD,
        RENDER,
        MOVEMENT,
        PVP,
        UTILITY
    }

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
            CarbonTheme.ACCENT_BAR_WIDTH,
            HEADER_HEIGHT / 2,
            CarbonTheme.PRIMARY
        );
        RenderUtils.drawPanel(
            panelX,
            panelY + HEADER_HEIGHT / 2,
            CarbonTheme.ACCENT_BAR_WIDTH,
            HEADER_HEIGHT - HEADER_HEIGHT / 2,
            CarbonTheme.SECONDARY
        );
        RenderUtils.drawDivider(panelX, headerBottom, panelWidth);
        RenderUtils.drawDivider(panelX, footerTop, panelWidth);

        RenderUtils.drawText(
            fontRendererObj,
            "CARBON CLIENT",
            panelX + PADDING,
            panelY + 12,
            CarbonTheme.TEXT
        );
        RenderUtils.drawText(
            fontRendererObj,
            "PVP CLIENT",
            panelX + PADDING,
            panelY + 27,
            CarbonTheme.MUTED_TEXT
        );

        if (selectedModule == null) {
            drawTab(0, panelX, panelY, !settingsTab, true, mouseX, mouseY);
            drawTab(1, panelX, panelY, settingsTab, true, mouseX, mouseY);
            drawTab(2, panelX, panelY, false, false, mouseX, mouseY);
            drawTab(3, panelX, panelY, false, true, mouseX, mouseY);
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
            "Carbon Client v" + Reference.VERSION,
            panelX + PADDING,
            footerTop + 10,
            CarbonTheme.MUTED_TEXT
        );
        String platform = "Minecraft 1.8.9 Forge";
        RenderUtils.drawText(
            fontRendererObj,
            platform,
            panelX + panelWidth - PADDING
                - fontRendererObj.getStringWidth(platform),
            footerTop + 10,
            CarbonTheme.MUTED_TEXT
        );

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawTab(
        int index,
        int panelX,
        int panelY,
        boolean selected,
        boolean enabled,
        int mouseX,
        int mouseY
    ) {
        String label = TAB_LABELS[index];
        int x = getTabX(panelX, index);
        int y = panelY + CarbonTheme.SPACE_8;
        int width = getTabWidth(label);
        int height = HEADER_HEIGHT - CarbonTheme.SPACE_16;
        boolean hovered = enabled
            && isInside(mouseX, mouseY, x, y, width, height);

        if (selected || hovered) {
            RenderUtils.drawPanel(
                x,
                y,
                width,
                height,
                selected ? CarbonTheme.ROW : CarbonTheme.BUTTON_HOVER
            );
            RenderUtils.drawOutline(
                x,
                y,
                width,
                height,
                selected ? CarbonTheme.ACCENT : CarbonTheme.BORDER_HOVER
            );
        }

        RenderUtils.drawCenteredText(
            fontRendererObj,
            label,
            x,
            y,
            width,
            height,
            selected
                ? CarbonTheme.TEXT
                : enabled
                    ? hovered ? CarbonTheme.ACCENT : CarbonTheme.MUTED_TEXT
                    : CarbonTheme.BORDER_HOVER
        );

        if (selected) {
            RenderUtils.drawPanel(
                x + CarbonTheme.SPACE_6,
                panelY + HEADER_HEIGHT - CarbonTheme.SPACE_4,
                width - CarbonTheme.SPACE_12,
                CarbonTheme.SPACE_2,
                CarbonTheme.ACCENT
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
        List<Module> modules = getFilteredModules();
        int gridWidth = panelWidth - PADDING * 2;
        int cardWidth = (gridWidth - CARD_GAP * (GRID_COLUMNS - 1)) / GRID_COLUMNS;
        int panelY = contentTop - HEADER_HEIGHT;
        int searchX = panelX + PADDING;
        int searchY = contentTop + PADDING;
        int searchWidth = panelWidth - PADDING * 2;
        int gridTop = searchY + SEARCH_HEIGHT + CARD_GAP;
        int gridBottom = getModuleGridBottom(panelY);
        int viewportHeight = Math.max(0, gridBottom - gridTop);
        int maximumScroll = getMaximumModuleScroll(
            modules.size(),
            viewportHeight
        );
        moduleScrollOffset = Math.max(
            0,
            Math.min(moduleScrollOffset, maximumScroll)
        );

        drawModuleSearchBar(
            mouseX,
            mouseY,
            searchX,
            searchY,
            searchWidth
        );

        if (modules.isEmpty()) {
            RenderUtils.drawCenteredText(
                fontRendererObj,
                "No mods found",
                searchX,
                gridTop,
                searchWidth,
                viewportHeight,
                CarbonTheme.MUTED_TEXT
            );
            return;
        }

        boolean mouseInViewport = isInside(
            mouseX,
            mouseY,
            searchX,
            gridTop,
            searchWidth,
            viewportHeight
        );
        int cardMouseX = mouseInViewport ? mouseX : Integer.MIN_VALUE;
        int cardMouseY = mouseInViewport ? mouseY : Integer.MIN_VALUE;

        RenderUtils.beginScissor(
            searchX - CarbonTheme.SPACE_2,
            gridTop,
            searchWidth + CarbonTheme.SPACE_4,
            viewportHeight
        );
        for (int index = 0; index < modules.size(); index++) {
            Module module = modules.get(index);
            int column = index % GRID_COLUMNS;
            int row = index / GRID_COLUMNS;
            int cardX = panelX + PADDING + column * (cardWidth + CARD_GAP);
            int cardY = gridTop
                + row * (CARD_HEIGHT + CARD_GAP)
                - moduleScrollOffset;

            if (cardY + CARD_HEIGHT > gridTop && cardY < gridBottom) {
                drawModuleCard(
                    module,
                    cardMouseX,
                    cardMouseY,
                    cardX,
                    cardY,
                    cardWidth
                );
            }
        }
        RenderUtils.endScissor();

        drawModuleScrollbar(
            panelX + panelWidth - CarbonTheme.SPACE_6,
            gridTop,
            viewportHeight,
            moduleScrollOffset,
            maximumScroll
        );
    }

    private void drawModuleSearchBar(
        int mouseX,
        int mouseY,
        int x,
        int y,
        int width
    ) {
        boolean hovered = isInside(mouseX, mouseY, x, y, width, SEARCH_HEIGHT);
        int borderColor = moduleSearchFocused
            ? CarbonTheme.ACCENT
            : hovered
                ? CarbonTheme.BORDER_HOVER
                : CarbonTheme.BORDER;

        RenderUtils.drawPanel(x, y, width, SEARCH_HEIGHT, CarbonTheme.TRACK);
        RenderUtils.drawOutline(x, y, width, SEARCH_HEIGHT, borderColor);
        RenderUtils.drawPanel(
            x,
            y,
            CarbonTheme.ACCENT_BAR_WIDTH,
            SEARCH_HEIGHT,
            moduleSearchFocused ? CarbonTheme.PRIMARY : CarbonTheme.SECONDARY
        );

        String text = moduleSearchQuery.isEmpty()
            ? "Search mods..."
            : moduleSearchQuery + (moduleSearchFocused ? "_" : "");
        int filterWidth = fontRendererObj.getStringWidth(moduleFilter.name())
            + CarbonTheme.SPACE_12;
        int textWidth = width
            - filterWidth
            - CarbonTheme.SPACE_24
            - CarbonTheme.SPACE_6;
        String visibleText = moduleSearchQuery.isEmpty()
            ? text
            : fontRendererObj.trimStringToWidth(text, textWidth, true);
        RenderUtils.drawText(
            fontRendererObj,
            visibleText,
            x + CarbonTheme.SPACE_12,
            y + 8,
            moduleSearchQuery.isEmpty()
                ? CarbonTheme.MUTED_TEXT
                : CarbonTheme.TEXT
        );

        String filterLabel = moduleFilter.name();
        RenderUtils.drawPanel(
            x + width - filterWidth - CarbonTheme.SPACE_6,
            y + CarbonTheme.SPACE_4,
            filterWidth,
            SEARCH_HEIGHT - CarbonTheme.SPACE_8,
            CarbonTheme.ROW
        );
        RenderUtils.drawCenteredText(
            fontRendererObj,
            filterLabel,
            x + width - filterWidth - CarbonTheme.SPACE_6,
            y + CarbonTheme.SPACE_4,
            filterWidth,
            SEARCH_HEIGHT - CarbonTheme.SPACE_8,
            CarbonTheme.ACCENT
        );
    }

    private void drawModuleScrollbar(
        int x,
        int y,
        int height,
        int scrollOffset,
        int maximumScroll
    ) {
        if (maximumScroll <= 0 || height <= 0) {
            return;
        }

        int thumbHeight = Math.max(24, height * height / (height + maximumScroll));
        int travel = Math.max(1, height - thumbHeight);
        int thumbY = y + (int) Math.round(
            travel * (scrollOffset / (double) maximumScroll)
        );

        RenderUtils.drawPanel(x, y, 2, height, CarbonTheme.TRACK);
        RenderUtils.drawPanel(
            x,
            thumbY,
            2,
            thumbHeight,
            CarbonTheme.ACCENT
        );
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
            x + CarbonTheme.SPACE_12,
            y + CarbonTheme.SPACE_12,
            CarbonTheme.TEXT
        );
        RenderUtils.drawText(
            fontRendererObj,
            module.getCategory().name(),
            x + CarbonTheme.SPACE_12,
            y + 30,
            CarbonTheme.MUTED_TEXT
        );

        String status = module.isEnabled() ? "ENABLED" : "DISABLED";
        int statusWidth = fontRendererObj.getStringWidth(status)
            + CarbonTheme.SPACE_12;
        RenderUtils.drawPanel(
            x + CarbonTheme.SPACE_12,
            y + 46,
            statusWidth,
            16,
            CarbonTheme.TRACK
        );
        RenderUtils.drawOutline(
            x + CarbonTheme.SPACE_12,
            y + 46,
            statusWidth,
            16,
            accent
        );
        RenderUtils.drawCenteredText(
            fontRendererObj,
            status,
            x + CarbonTheme.SPACE_12,
            y + 46,
            statusWidth,
            16,
            accent
        );
        RenderUtils.drawText(
            fontRendererObj,
            fontRendererObj.trimStringToWidth(
                module.getDescription(),
                width - CarbonTheme.SPACE_24
            ),
            x + CarbonTheme.SPACE_12,
            y + 70,
            CarbonTheme.MUTED_TEXT
        );

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
            panelX + PADDING + CarbonTheme.SPACE_8,
            contentTop + 12,
            CarbonTheme.TEXT
        );
        RenderUtils.drawPanel(
            panelX + PADDING,
            contentTop + CarbonTheme.SPACE_8,
            CarbonTheme.ACCENT_BAR_WIDTH,
            18,
            CarbonTheme.ACCENT
        );
        drawButton(
            "Reset to Defaults",
            panelX + panelWidth - PADDING - 130,
            contentTop + 7,
            130,
            mouseX,
            mouseY,
            CarbonTheme.DANGER
        );

        List<Setting<?>> settings = getVisibleSettings();
        int rowX = panelX + PADDING;
        int rowWidth = panelWidth - PADDING * 2;
        int rowY = getOptionsRowY(contentTop);
        if (isCrosshairOptions()) {
            drawCrosshairPreview(
                mouseX,
                mouseY,
                rowX,
                contentTop + 32,
                rowWidth
            );
        }
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

    private void drawCrosshairPreview(
        int mouseX,
        int mouseY,
        int x,
        int y,
        int width
    ) {
        int height = 96;
        drawPreviewBackground(x, y, width, height);
        RenderUtils.drawOutline(x, y, width, height, CarbonTheme.BORDER);
        RenderUtils.drawText(
            fontRendererObj,
            "Live Preview",
            x + 8,
            y + 7,
            CarbonTheme.MUTED_TEXT
        );

        drawButton(
            crosshairPreviewBackground,
            x + width - 98,
            y + 5,
            90,
            mouseX,
            mouseY,
            CarbonTheme.SECONDARY
        );

        ((CrosshairModule) selectedModule).renderCrosshairAt(
            x + width / 2,
            y + height / 2 + 8,
            true
        );
    }

    private void drawPreviewBackground(int x, int y, int width, int height) {
        if ("Light".equals(crosshairPreviewBackground)) {
            RenderUtils.drawPanel(x, y, width, height, 0xFFE4E8F0);
            return;
        }
        if ("Transparent/Grid".equals(crosshairPreviewBackground)) {
            int cell = 10;
            for (int row = 0; row * cell < height; row++) {
                for (int column = 0; column * cell < width; column++) {
                    RenderUtils.drawPanel(
                        x + column * cell,
                        y + row * cell,
                        Math.min(cell, width - column * cell),
                        Math.min(cell, height - row * cell),
                        (row + column) % 2 == 0
                            ? 0xFF30394A
                            : 0xFF596579
                    );
                }
            }
            return;
        }
        if ("Game-like".equals(crosshairPreviewBackground)) {
            RenderUtils.drawPanel(x, y, width, height / 2, 0xFF72A7D8);
            RenderUtils.drawPanel(
                x,
                y + height / 2,
                width,
                height - height / 2,
                0xFF547A3F
            );
            return;
        }
        RenderUtils.drawPanel(x, y, width, height, CarbonTheme.PANEL);
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
            CarbonTheme.DANGER
        );

        List<Module> modules = getKeybindModules();
        int rowX = panelX + PADDING;
        int rowWidth = getPanelWidth() - PADDING * 2;
        int rowY = contentTop + 92;
        int panelY = contentTop - HEADER_HEIGHT;
        int visibleCount = getVisibleKeybindCount(panelY, rowY);
        int endIndex = Math.min(
            modules.size(),
            keybindScrollIndex + visibleCount
        );

        RenderUtils.drawText(
            fontRendererObj,
            "Keybinds",
            rowX,
            rowY - 16,
            CarbonTheme.TEXT
        );

        for (int index = keybindScrollIndex; index < endIndex; index++) {
            drawKeybindRow(
                modules.get(index),
                mouseX,
                mouseY,
                rowX,
                rowY + (index - keybindScrollIndex) * SETTING_ROW_HEIGHT,
                rowWidth
            );
        }

        if (modules.size() > visibleCount) {
            RenderUtils.drawText(
                fontRendererObj,
                "Mouse wheel: more keybinds",
                rowX,
                panelY + getPanelHeight() - FOOTER_HEIGHT - 13,
                CarbonTheme.MUTED_TEXT
            );
        }
    }

    private void drawKeybindRow(
        Module module,
        int mouseX,
        int mouseY,
        int x,
        int y,
        int width
    ) {
        boolean conflict = hasKeybindConflict(module);
        int accent = conflict ? CarbonTheme.DANGER : CarbonTheme.SECONDARY;
        int rowHeight = SETTING_ROW_HEIGHT - 4;
        boolean hovered = isInside(mouseX, mouseY, x, y, width, rowHeight);

        RenderUtils.drawRow(x, y, width, rowHeight, hovered, accent);
        RenderUtils.drawText(
            fontRendererObj,
            module.getName(),
            x + 10,
            y + 10,
            CarbonTheme.TEXT
        );

        String keyName = listeningModuleKeybind == module
            ? "Press a key..."
            : getKeyName(module.getKeyCode());
        int keyX = x + width - 210;
        int keyWidth = 72;
        RenderUtils.drawPanel(
            keyX,
            y + 5,
            keyWidth,
            BUTTON_HEIGHT,
            CarbonTheme.TRACK
        );
        RenderUtils.drawOutline(
            keyX,
            y + 5,
            keyWidth,
            BUTTON_HEIGHT,
            conflict ? CarbonTheme.DANGER : CarbonTheme.BORDER
        );
        RenderUtils.drawCenteredText(
            fontRendererObj,
            keyName,
            keyX,
            y + 5,
            keyWidth,
            BUTTON_HEIGHT,
            conflict ? CarbonTheme.DANGER : CarbonTheme.MUTED_TEXT
        );
        drawButton(
            "Change",
            x + width - 132,
            y + 5,
            68,
            mouseX,
            mouseY,
            accent
        );
        drawButton(
            "Reset",
            x + width - 58,
            y + 5,
            52,
            mouseX,
            mouseY,
            CarbonTheme.SECONDARY
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
            CarbonTheme.SECONDARY
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
        int rowHeight = SETTING_ROW_HEIGHT - CarbonTheme.SPACE_4;
        RenderUtils.drawRow(
            x,
            y,
            width,
            rowHeight,
            isInside(mouseX, mouseY, x, y, width, rowHeight),
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
                && !settingsTab
                && handleModuleSearchClick(mouseX, mouseY)) {
                return;
            }
            if (selectedModule == null
                && settingsTab
                && handleResetAllClick(mouseX, mouseY)) {
                return;
            }
            if (selectedModule == null
                && settingsTab
                && handleKeybindPanelClick(mouseX, mouseY)) {
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

        if (isInsideTab(mouseX, mouseY, panelX, panelY, 0)) {
            settingsTab = false;
            moduleSearchFocused = false;
            resetAllConfirmation = false;
            return true;
        }
        if (isInsideTab(mouseX, mouseY, panelX, panelY, 1)) {
            settingsTab = true;
            moduleSearchFocused = false;
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
        moduleScrollOffset = 0;
        keybindScrollIndex = 0;
        listeningModuleKeybind = null;
        return true;
    }

    private boolean handleKeybindPanelClick(int mouseX, int mouseY) {
        int panelWidth = getPanelWidth();
        int panelHeight = getPanelHeight();
        int panelX = getPanelX(panelWidth);
        int panelY = getPanelY(panelHeight);
        int rowX = panelX + PADDING;
        int rowWidth = panelWidth - PADDING * 2;
        int rowY = panelY + HEADER_HEIGHT + 92;
        List<Module> modules = getKeybindModules();
        int visibleCount = getVisibleKeybindCount(panelY, rowY);
        int endIndex = Math.min(
            modules.size(),
            keybindScrollIndex + visibleCount
        );

        for (int index = keybindScrollIndex; index < endIndex; index++) {
            Module module = modules.get(index);
            int currentY = rowY
                + (index - keybindScrollIndex) * SETTING_ROW_HEIGHT;

            if (isInside(
                mouseX,
                mouseY,
                rowX + rowWidth - 132,
                currentY + 5,
                68,
                BUTTON_HEIGHT
            )) {
                listeningModuleKeybind = module;
                return true;
            }
            if (isInside(
                mouseX,
                mouseY,
                rowX + rowWidth - 58,
                currentY + 5,
                52,
                BUTTON_HEIGHT
            )) {
                module.setKeyCode(module.getDefaultKeyCode());
                configManager.save();
                listeningModuleKeybind = null;
                return true;
            }
        }

        return false;
    }

    private boolean handleHudEditorTabClick(int mouseX, int mouseY) {
        int panelWidth = getPanelWidth();
        int panelHeight = getPanelHeight();
        int panelX = getPanelX(panelWidth);
        int panelY = getPanelY(panelHeight);

        if (isInsideTab(mouseX, mouseY, panelX, panelY, 3)) {
            mc.displayGuiScreen(
                new HudLayoutEditorScreen(moduleManager, configManager)
            );
            return true;
        }

        return false;
    }

    private boolean handleModuleSearchClick(int mouseX, int mouseY) {
        int panelWidth = getPanelWidth();
        int panelHeight = getPanelHeight();
        int panelX = getPanelX(panelWidth);
        int panelY = getPanelY(panelHeight);
        int searchX = panelX + PADDING;
        int searchY = panelY + HEADER_HEIGHT + PADDING;
        int searchWidth = panelWidth - PADDING * 2;

        if (isInside(
            mouseX,
            mouseY,
            searchX,
            searchY,
            searchWidth,
            SEARCH_HEIGHT
        )) {
            moduleSearchFocused = true;
            return true;
        }

        moduleSearchFocused = false;
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
        int gridTop = contentTop
            + PADDING
            + SEARCH_HEIGHT
            + CARD_GAP;
        int gridBottom = getModuleGridBottom(panelY);
        List<Module> modules = getFilteredModules();

        if (!isInside(
            mouseX,
            mouseY,
            panelX + PADDING,
            gridTop,
            gridWidth,
            Math.max(0, gridBottom - gridTop)
        )) {
            return false;
        }

        for (int index = 0; index < modules.size(); index++) {
            Module module = modules.get(index);
            int column = index % GRID_COLUMNS;
            int row = index / GRID_COLUMNS;
            int cardX = panelX + PADDING + column * (cardWidth + CARD_GAP);
            int cardY = gridTop
                + row * (CARD_HEIGHT + CARD_GAP)
                - moduleScrollOffset;
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
                moduleSearchFocused = false;
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
            if (isCrosshairOptions()) {
                crosshairPreviewBackground = "Dark";
            }
            return true;
        }
        if (isCrosshairOptions()
            && isInside(
                mouseX,
                mouseY,
                panelX + panelWidth - PADDING - 98,
                contentTop + 37,
                90,
                BUTTON_HEIGHT
            )) {
            cycleCrosshairPreviewBackground();
            return true;
        }

        List<Setting<?>> settings = getVisibleSettings();
        int rowX = panelX + PADDING;
        int rowWidth = panelWidth - PADDING * 2;
        int rowY = getOptionsRowY(contentTop);
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
        if (openColorSetting != null) {
            return;
        }

        if (selectedModule == null && !settingsTab) {
            List<Module> modules = getFilteredModules();
            int panelY = getPanelY(getPanelHeight());
            int contentTop = panelY + HEADER_HEIGHT;
            int gridTop = contentTop
                + PADDING
                + SEARCH_HEIGHT
                + CARD_GAP;
            int viewportHeight = Math.max(
                0,
                getModuleGridBottom(panelY) - gridTop
            );
            int maximumScroll = getMaximumModuleScroll(
                modules.size(),
                viewportHeight
            );

            if (wheel < 0) {
                moduleScrollOffset = Math.min(
                    maximumScroll,
                    moduleScrollOffset + MODULE_SCROLL_STEP
                );
            } else {
                moduleScrollOffset = Math.max(
                    0,
                    moduleScrollOffset - MODULE_SCROLL_STEP
                );
            }
            return;
        }
        if (selectedModule == null && settingsTab) {
            List<Module> modules = getKeybindModules();
            int panelY = getPanelY(getPanelHeight());
            int rowY = panelY + HEADER_HEIGHT + 92;
            int visibleCount = getVisibleKeybindCount(panelY, rowY);
            int maximumIndex = Math.max(0, modules.size() - visibleCount);

            if (wheel < 0) {
                keybindScrollIndex = Math.min(
                    maximumIndex,
                    keybindScrollIndex + 1
                );
            } else {
                keybindScrollIndex = Math.max(0, keybindScrollIndex - 1);
            }
            return;
        }
        if (selectedModule == null) {
            return;
        }

        List<Setting<?>> settings = getVisibleSettings();
        int panelY = getPanelY(getPanelHeight());
        int rowY = getOptionsRowY(panelY + HEADER_HEIGHT);
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

        Collections.sort(
            visibleSettings,
            new Comparator<Setting<?>>() {
                @Override
                public int compare(Setting<?> first, Setting<?> second) {
                    return Integer.compare(
                        getSettingOrder(first),
                        getSettingOrder(second)
                    );
                }
            }
        );
        return visibleSettings;
    }

    private int getSettingOrder(Setting<?> setting) {
        String name = setting.getName();
        if ("Enabled".equalsIgnoreCase(name)
            || "Show HUD".equalsIgnoreCase(name)
            || name.endsWith(" Enabled")) {
            return 0;
        }
        if (name.startsWith("Show ")
            || setting instanceof ModeSetting
            || name.contains("Layout")
            || name.contains("Format")
            || name.contains("Precision")) {
            return 1;
        }
        if (setting instanceof ColorSetting) {
            return 2;
        }
        if ("Scale".equalsIgnoreCase(name)) {
            return 3;
        }
        return 4;
    }

    private int getVisibleSettingCount(int panelY, int rowY) {
        int footerTop = panelY + getPanelHeight() - FOOTER_HEIGHT;
        return Math.max(1, (footerTop - rowY - 16) / SETTING_ROW_HEIGHT);
    }

    private int getOptionsRowY(int contentTop) {
        return contentTop + (isCrosshairOptions() ? 136 : 32);
    }

    private boolean isCrosshairOptions() {
        return selectedModule instanceof CrosshairModule;
    }

    private void cycleCrosshairPreviewBackground() {
        if ("Dark".equals(crosshairPreviewBackground)) {
            crosshairPreviewBackground = "Light";
        } else if ("Light".equals(crosshairPreviewBackground)) {
            crosshairPreviewBackground = "Transparent/Grid";
        } else if ("Transparent/Grid".equals(crosshairPreviewBackground)) {
            crosshairPreviewBackground = "Game-like";
        } else {
            crosshairPreviewBackground = "Dark";
        }
    }

    private int getVisibleKeybindCount(int panelY, int rowY) {
        int footerTop = panelY + getPanelHeight() - FOOTER_HEIGHT;
        return Math.max(1, (footerTop - rowY - 16) / SETTING_ROW_HEIGHT);
    }

    private int getModuleGridBottom(int panelY) {
        return panelY + getPanelHeight() - FOOTER_HEIGHT - PADDING;
    }

    private int getModuleRowCount(int moduleCount) {
        return (moduleCount + GRID_COLUMNS - 1) / GRID_COLUMNS;
    }

    private int getMaximumModuleScroll(int moduleCount, int viewportHeight) {
        int rows = getModuleRowCount(moduleCount);
        int contentHeight = rows == 0
            ? 0
            : rows * CARD_HEIGHT + (rows - 1) * CARD_GAP;
        return Math.max(0, contentHeight - viewportHeight);
    }

    private String formatNumber(double value) {
        return String.format("%.1f", value);
    }

    private List<Module> getFilteredModules() {
        List<Module> filteredModules = new ArrayList<Module>();
        String query = moduleSearchQuery.trim().toLowerCase(Locale.ROOT);

        for (Module module : moduleManager.getModules()) {
            if (!isMenuModule(module) || !matchesModuleFilter(module)) {
                continue;
            }

            if (query.isEmpty()) {
                filteredModules.add(module);
                continue;
            }

            String searchableText = (
                module.getName()
                    + " "
                    + module.getDescription()
                    + " "
                    + module.getCategory().name()
            ).toLowerCase(Locale.ROOT);
            if (searchableText.contains(query)) {
                filteredModules.add(module);
            }
        }

        return filteredModules;
    }

    private boolean isMenuModule(Module module) {
        return module.getCategory() == ModuleCategory.RENDER
            || module.getCategory() == ModuleCategory.HUD
            || module.getCategory() == ModuleCategory.MOVEMENT;
    }

    private boolean matchesModuleFilter(Module module) {
        switch (moduleFilter) {
            case HUD:
                return module.getCategory() == ModuleCategory.HUD;
            case RENDER:
                return module.getCategory() == ModuleCategory.RENDER;
            case MOVEMENT:
                return module.getCategory() == ModuleCategory.MOVEMENT;
            case PVP:
            case UTILITY:
                return false;
            case ALL:
            default:
                return true;
        }
    }

    private List<Module> getKeybindModules() {
        List<Module> modules = new ArrayList<Module>();

        for (Module module : moduleManager.getModules()) {
            if (!"Example Module".equalsIgnoreCase(module.getName())) {
                modules.add(module);
            }
        }

        return modules;
    }

    private boolean hasKeybindConflict(Module target) {
        int keyCode = target.getKeyCode();
        if (keyCode == Keyboard.KEY_NONE) {
            return false;
        }

        int matches = 0;
        for (Module module : getKeybindModules()) {
            if (module.getKeyCode() == keyCode && ++matches >= 2) {
                return true;
            }
        }

        return false;
    }

    private String getKeyName(int keyCode) {
        if (keyCode <= Keyboard.KEY_NONE || keyCode >= Keyboard.KEYBOARD_SIZE) {
            return "NONE";
        }

        String name = Keyboard.getKeyName(keyCode);
        return name == null ? "UNKNOWN" : name;
    }

    private int getTabX(int panelX, int index) {
        int x = panelX + TAB_START_OFFSET;
        for (int current = 0; current < index; current++) {
            x += getTabWidth(TAB_LABELS[current]) + TAB_GAP;
        }
        return x;
    }

    private int getTabWidth(String label) {
        return fontRendererObj.getStringWidth(label) + CarbonTheme.SPACE_16;
    }

    private boolean isInsideTab(
        int mouseX,
        int mouseY,
        int panelX,
        int panelY,
        int index
    ) {
        return isInside(
            mouseX,
            mouseY,
            getTabX(panelX, index),
            panelY + CarbonTheme.SPACE_8,
            getTabWidth(TAB_LABELS[index]),
            HEADER_HEIGHT - CarbonTheme.SPACE_16
        );
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

        if (moduleSearchFocused
            && selectedModule == null
            && !settingsTab) {
            if (keyCode == Keyboard.KEY_ESCAPE
                || keyCode == Keyboard.KEY_RETURN
                || keyCode == Keyboard.KEY_NUMPADENTER) {
                moduleSearchFocused = false;
                return;
            }
            if (keyCode == Keyboard.KEY_BACK) {
                if (!moduleSearchQuery.isEmpty()) {
                    moduleSearchQuery = moduleSearchQuery.substring(
                        0,
                        moduleSearchQuery.length() - 1
                    );
                    moduleScrollOffset = 0;
                }
                return;
            }
            if (typedChar >= 32
                && typedChar != 127
                && moduleSearchQuery.length() < MAX_SEARCH_LENGTH) {
                moduleSearchQuery += typedChar;
                moduleScrollOffset = 0;
            }
            return;
        }

        if (listeningModuleKeybind != null) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                listeningModuleKeybind = null;
                return;
            }

            listeningModuleKeybind.setKeyCode(
                keyCode == Keyboard.KEY_DELETE || keyCode == Keyboard.KEY_BACK
                    ? Keyboard.KEY_NONE
                    : keyCode
            );
            configManager.save();
            listeningModuleKeybind = null;
            return;
        }

        if (listeningKeybind != null) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                listeningKeybind = null;
                return;
            }

            listeningKeybind.setValue(
                keyCode == Keyboard.KEY_DELETE || keyCode == Keyboard.KEY_BACK
                    ? Keyboard.KEY_NONE
                    : keyCode
            );
            configManager.save();
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
