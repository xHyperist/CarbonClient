package com.carbonclient.modules.render;

import com.carbonclient.event.EventListener;
import com.carbonclient.event.impl.Render2DEvent;
import com.carbonclient.module.DraggableHudModule;
import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleCategory;
import com.carbonclient.setting.impl.BooleanSetting;
import com.carbonclient.setting.impl.ColorSetting;
import com.carbonclient.setting.impl.ModeSetting;
import com.carbonclient.setting.impl.NumberSetting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

public final class ArmorHudModule extends Module implements DraggableHudModule {

    private static final int SLOT_COUNT = 5;
    private static final int ICON_SIZE = 16;
    private static final int ICON_TEXT_GAP = 3;
    private static final int ITEM_GAP = 3;

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final RenderItem itemRenderer = minecraft.getRenderItem();
    private final BooleanSetting showDurability = addSetting(
        new BooleanSetting("Show Durability", true)
    );
    private final ModeSetting durabilityMode = addSetting(
        new ModeSetting(
            "Durability Mode",
            "Percent",
            "Percent",
            "Number",
            "Both"
        )
    );
    private final ModeSetting layout = addSetting(
        new ModeSetting(
            "Layout",
            "Horizontal",
            "Horizontal",
            "Vertical"
        )
    );
    private final NumberSetting scale = addSetting(
        new NumberSetting("Scale", 1.0D, 0.5D, 2.0D, 0.1D)
    );
    private final ColorSetting textColor = addSetting(
        new ColorSetting("Text Color", 0xFFFFFFFF)
    );
    private final BooleanSetting lowDurabilityWarning = addSetting(
        new BooleanSetting("Low Durability Warning", true)
    );
    private final NumberSetting warningThreshold = addSetting(
        new NumberSetting("Warning Threshold", 25.0D, 5.0D, 75.0D, 5.0D)
    );
    private final NumberSetting positionX = addHiddenSetting(
        new NumberSetting("Position X", 75.0D, 0.0D, 10000.0D, 1.0D)
    );
    private final NumberSetting positionY = addHiddenSetting(
        new NumberSetting("Position Y", 5.0D, 0.0D, 10000.0D, 1.0D)
    );
    private final EventListener<Render2DEvent> renderListener =
        new EventListener<Render2DEvent>() {
            @Override
            public void onEvent(Render2DEvent event) {
                renderHud();
            }
        };

    public ArmorHudModule() {
        super(
            "Armor HUD",
            "Displays equipped armor and the held item with durability.",
            ModuleCategory.RENDER,
            true,
            Keyboard.KEY_NONE
        );
    }

    @Override
    protected void onEnable() {
        subscribe(Render2DEvent.class, renderListener);
    }

    @Override
    protected void onDisable() {
        unsubscribe(Render2DEvent.class, renderListener);
    }

    @Override
    public void renderHud() {
        float renderScale = scale.getValue().floatValue();
        int renderX = Math.round(getPositionX() / renderScale);
        int renderY = Math.round(getPositionY() / renderScale);

        GlStateManager.pushMatrix();
        GlStateManager.scale(renderScale, renderScale, 1.0F);

        List<ItemStack> items = getVisibleItems();
        RenderHelper.enableGUIStandardItemLighting();
        int offset = 0;
        for (ItemStack stack : items) {
            drawItem(
                stack,
                isHorizontal() ? renderX + offset : renderX,
                isHorizontal() ? renderY : renderY + offset
            );
            offset += getItemExtent(stack) + ITEM_GAP;
        }
        RenderHelper.disableStandardItemLighting();

        GlStateManager.popMatrix();
    }

    private void drawItem(ItemStack stack, int x, int y) {
        itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);

        String durability = getDurabilityText(stack);
        if (durability == null) {
            return;
        }

        int color = getDurabilityColor(stack);
        int textX = x + ICON_SIZE + ICON_TEXT_GAP;
        int textY = y
            + (ICON_SIZE - minecraft.fontRendererObj.FONT_HEIGHT) / 2;

        minecraft.fontRendererObj.drawString(durability, textX, textY, color);
    }

    private List<ItemStack> getVisibleItems() {
        List<ItemStack> visibleItems = new ArrayList<ItemStack>();
        for (ItemStack stack : getDisplayedItems()) {
            if (stack != null) {
                visibleItems.add(stack);
            }
        }
        return visibleItems;
    }

    private ItemStack[] getDisplayedItems() {
        ItemStack[] items = new ItemStack[SLOT_COUNT];
        if (minecraft.thePlayer == null) {
            return items;
        }

        items[0] = minecraft.thePlayer.inventory.armorInventory[3];
        items[1] = minecraft.thePlayer.inventory.armorInventory[2];
        items[2] = minecraft.thePlayer.inventory.armorInventory[1];
        items[3] = minecraft.thePlayer.inventory.armorInventory[0];
        items[4] = minecraft.thePlayer.getHeldItem();
        return items;
    }

    private String getDurabilityText(ItemStack stack) {
        if (!showDurability.isEnabled() || !stack.isItemStackDamageable()) {
            return null;
        }

        int maximum = stack.getMaxDamage();
        int remaining = Math.max(0, maximum - stack.getItemDamage());
        int percent = maximum <= 0
            ? 100
            : Math.round(remaining * 100.0F / maximum);

        if ("Number".equals(durabilityMode.getValue())) {
            return Integer.toString(remaining);
        }
        if ("Both".equals(durabilityMode.getValue())) {
            return percent + "% " + remaining;
        }
        return percent + "%";
    }

    private int getDurabilityColor(ItemStack stack) {
        if (!lowDurabilityWarning.isEnabled() || !stack.isItemStackDamageable()) {
            return textColor.getColor();
        }

        int maximum = stack.getMaxDamage();
        float ratio = maximum <= 0
            ? 1.0F
            : Math.max(
                0.0F,
                Math.min(
                    1.0F,
                    (maximum - stack.getItemDamage()) / (float) maximum
                )
            );
        float threshold = warningThreshold.getValue().floatValue() / 100.0F;
        if (ratio > threshold) {
            return textColor.getColor();
        }

        int red = 255;
        int green = Math.round(255.0F * ratio / Math.max(0.01F, threshold));
        return 0xFF000000 | red << 16 | green << 8;
    }

    private boolean isHorizontal() {
        return "Horizontal".equals(layout.getValue());
    }

    private int getItemWidth(ItemStack stack) {
        String durability = getDurabilityText(stack);
        return durability == null
            ? ICON_SIZE
            : ICON_SIZE
                + ICON_TEXT_GAP
                + minecraft.fontRendererObj.getStringWidth(durability);
    }

    private int getItemExtent(ItemStack stack) {
        return isHorizontal() ? getItemWidth(stack) : ICON_SIZE;
    }

    private int getUnscaledWidth() {
        List<ItemStack> items = getVisibleItems();
        if (items.isEmpty()) {
            return ICON_SIZE;
        }

        if (isHorizontal()) {
            int width = 0;
            for (ItemStack stack : items) {
                width += getItemWidth(stack);
            }
            return width + ITEM_GAP * (items.size() - 1);
        }

        int width = ICON_SIZE;
        for (ItemStack stack : items) {
            width = Math.max(width, getItemWidth(stack));
        }
        return width;
    }

    private int getUnscaledHeight() {
        int itemCount = getVisibleItems().size();
        if (itemCount == 0 || isHorizontal()) {
            return ICON_SIZE;
        }
        return ICON_SIZE * itemCount + ITEM_GAP * (itemCount - 1);
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
        return Math.round(
            getUnscaledWidth() * scale.getValue().floatValue()
        );
    }

    @Override
    public int getHudHeight() {
        return Math.round(
            getUnscaledHeight() * scale.getValue().floatValue()
        );
    }
}
