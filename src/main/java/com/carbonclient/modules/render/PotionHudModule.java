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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public final class PotionHudModule extends Module implements DraggableHudModule {

    private static final ResourceLocation INVENTORY_TEXTURE =
        new ResourceLocation("textures/gui/container/inventory.png");
    private static final int ICON_SIZE = 18;
    private static final int ICON_TEXT_GAP = 3;
    private static final int EFFECT_GAP = 3;

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final BooleanSetting showIcon = addSetting(
        new BooleanSetting("Show Icon", true)
    );
    private final BooleanSetting showName = addSetting(
        new BooleanSetting("Show Name", true)
    );
    private final BooleanSetting showDuration = addSetting(
        new BooleanSetting("Show Duration", true)
    );
    private final BooleanSetting showLevel = addSetting(
        new BooleanSetting("Show Level", true)
    );
    private final ModeSetting layout = addSetting(
        new ModeSetting(
            "Layout",
            "Vertical",
            "Vertical",
            "Horizontal"
        )
    );
    private final NumberSetting scale = addSetting(
        new NumberSetting("Scale", 1.0D, 0.5D, 2.0D, 0.1D)
    );
    private final ColorSetting textColor = addSetting(
        new ColorSetting("Text Color", 0xFFFFFFFF)
    );
    private final ColorSetting warningColor = addSetting(
        new ColorSetting("Warning Color", 0xFFFF4F6D)
    );
    private final BooleanSetting lowTimeWarning = addSetting(
        new BooleanSetting("Low Time Warning", true)
    );
    private final NumberSetting warningThreshold = addSetting(
        new NumberSetting("Warning Threshold", 10.0D, 1.0D, 60.0D, 1.0D)
    );
    private final NumberSetting positionX = addHiddenSetting(
        new NumberSetting("Position X", 5.0D, 0.0D, 10000.0D, 1.0D)
    );
    private final NumberSetting positionY = addHiddenSetting(
        new NumberSetting("Position Y", 135.0D, 0.0D, 10000.0D, 1.0D)
    );
    private final EventListener<Render2DEvent> renderListener =
        new EventListener<Render2DEvent>() {
            @Override
            public void onEvent(Render2DEvent event) {
                renderHud();
            }
        };

    public PotionHudModule() {
        super(
            "Potion HUD",
            "Displays active potion effects, levels and remaining time.",
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
        List<PotionEffect> effects = getActiveEffects();
        if (effects.isEmpty()) {
            return;
        }

        float renderScale = scale.getValue().floatValue();
        int renderX = Math.round(getPositionX() / renderScale);
        int renderY = Math.round(getPositionY() / renderScale);

        GlStateManager.pushMatrix();
        GlStateManager.scale(renderScale, renderScale, 1.0F);

        int offset = 0;
        for (PotionEffect effect : effects) {
            int x = isVertical() ? renderX : renderX + offset;
            int y = isVertical() ? renderY + offset : renderY;
            drawEffect(effect, x, y);
            offset += (isVertical() ? getRowHeight() : getEffectWidth(effect))
                + EFFECT_GAP;
        }

        GlStateManager.popMatrix();
    }

    private void drawEffect(PotionEffect effect, int x, int y) {
        Potion potion = Potion.potionTypes[effect.getPotionID()];
        if (potion == null) {
            return;
        }

        int textX = x;
        if (showIcon.isEnabled() && potion.hasStatusIcon()) {
            drawPotionIcon(potion, x, y);
            textX += ICON_SIZE + ICON_TEXT_GAP;
        }

        String text = getEffectText(effect, potion);
        if (text.isEmpty()) {
            return;
        }

        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        int textY = y
            + (getRowHeight() - minecraft.fontRendererObj.FONT_HEIGHT) / 2;
        minecraft.fontRendererObj.drawString(
            text,
            textX,
            textY,
            getEffectColor(effect)
        );
    }

    private void drawPotionIcon(Potion potion, int x, int y) {
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.disableLighting();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(INVENTORY_TEXTURE);

        int iconIndex = potion.getStatusIconIndex();
        minecraft.ingameGUI.drawTexturedModalRect(
            x,
            y,
            iconIndex % 8 * ICON_SIZE,
            198 + iconIndex / 8 * ICON_SIZE,
            ICON_SIZE,
            ICON_SIZE
        );

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private String getEffectText(PotionEffect effect, Potion potion) {
        StringBuilder text = new StringBuilder();

        if (showName.isEnabled()) {
            text.append(I18n.format(potion.getName()));
        }
        if (showLevel.isEnabled() && effect.getAmplifier() > 0) {
            appendPart(text, toRoman(effect.getAmplifier() + 1));
        }
        if (showDuration.isEnabled()) {
            appendPart(text, Potion.getDurationString(effect));
        }

        return text.toString();
    }

    private void appendPart(StringBuilder text, String value) {
        if (text.length() > 0) {
            text.append(' ');
        }
        text.append(value);
    }

    private int getEffectColor(PotionEffect effect) {
        if (lowTimeWarning.isEnabled()
            && effect.getDuration() <= warningThreshold.getValue() * 20.0D) {
            return warningColor.getColor();
        }
        return textColor.getColor();
    }

    private List<PotionEffect> getActiveEffects() {
        List<PotionEffect> effects = new ArrayList<PotionEffect>();
        if (minecraft.thePlayer == null) {
            return effects;
        }

        for (Object value : minecraft.thePlayer.getActivePotionEffects()) {
            PotionEffect effect = (PotionEffect) value;
            int potionId = effect.getPotionID();
            if (potionId >= 0
                && potionId < Potion.potionTypes.length
                && Potion.potionTypes[potionId] != null) {
                effects.add(effect);
            }
        }

        Collections.sort(
            effects,
            new Comparator<PotionEffect>() {
                @Override
                public int compare(PotionEffect first, PotionEffect second) {
                    Potion firstPotion = Potion.potionTypes[first.getPotionID()];
                    Potion secondPotion = Potion.potionTypes[second.getPotionID()];
                    return I18n.format(firstPotion.getName()).compareToIgnoreCase(
                        I18n.format(secondPotion.getName())
                    );
                }
            }
        );
        return effects;
    }

    private int getEffectWidth(PotionEffect effect) {
        Potion potion = Potion.potionTypes[effect.getPotionID()];
        int width = showIcon.isEnabled() && potion.hasStatusIcon()
            ? ICON_SIZE
            : 0;
        String text = getEffectText(effect, potion);
        if (!text.isEmpty()) {
            if (width > 0) {
                width += ICON_TEXT_GAP;
            }
            width += minecraft.fontRendererObj.getStringWidth(text);
        }
        return Math.max(1, width);
    }

    private int getRowHeight() {
        return showIcon.isEnabled()
            ? ICON_SIZE
            : minecraft.fontRendererObj.FONT_HEIGHT;
    }

    private int getUnscaledWidth(List<PotionEffect> effects) {
        int contentWidth = 1;
        if (isVertical()) {
            for (PotionEffect effect : effects) {
                contentWidth = Math.max(contentWidth, getEffectWidth(effect));
            }
        } else {
            contentWidth = 0;
            for (PotionEffect effect : effects) {
                contentWidth += getEffectWidth(effect);
            }
            contentWidth += EFFECT_GAP * Math.max(0, effects.size() - 1);
        }
        return contentWidth;
    }

    private int getUnscaledHeight(List<PotionEffect> effects) {
        int contentHeight = isVertical()
            ? getRowHeight() * effects.size()
                + EFFECT_GAP * Math.max(0, effects.size() - 1)
            : getRowHeight();
        return contentHeight;
    }

    private boolean isVertical() {
        return "Vertical".equals(layout.getValue());
    }

    private String toRoman(int number) {
        String[] values = {
            "",
            "I",
            "II",
            "III",
            "IV",
            "V",
            "VI",
            "VII",
            "VIII",
            "IX",
            "X"
        };
        return number < values.length ? values[number] : Integer.toString(number);
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
        List<PotionEffect> effects = getActiveEffects();
        int width = effects.isEmpty() ? ICON_SIZE : getUnscaledWidth(effects);
        return Math.round(width * scale.getValue().floatValue());
    }

    @Override
    public int getHudHeight() {
        List<PotionEffect> effects = getActiveEffects();
        int height = effects.isEmpty() ? ICON_SIZE : getUnscaledHeight(effects);
        return Math.round(height * scale.getValue().floatValue());
    }
}
