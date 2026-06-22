package com.carbonclient.modules.render;

import com.carbonclient.event.EventListener;
import com.carbonclient.event.impl.AttackEntityEvent;
import com.carbonclient.event.impl.Render2DEvent;
import com.carbonclient.module.DraggableHudModule;
import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleCategory;
import com.carbonclient.setting.impl.BooleanSetting;
import com.carbonclient.setting.impl.ColorSetting;
import com.carbonclient.setting.impl.NumberSetting;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;

public final class ReachDisplayModule
    extends Module
    implements DraggableHudModule {

    private static final int PADDING = 3;

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final BooleanSetting showBackground = addSetting(
        new BooleanSetting("Show Background", true)
    );
    private final NumberSetting scale = addSetting(
        new NumberSetting("Scale", 1.0D, 0.5D, 2.0D, 0.05D)
    );
    private final ColorSetting textColor = addSetting(
        new ColorSetting("Text Color", 0xFFFFFFFF)
    );
    private final ColorSetting backgroundColor = addSetting(
        new ColorSetting("Background Color", 0x6F000000)
    );
    private final NumberSetting positionX = addHiddenSetting(
        new NumberSetting("Position X", 5.0D, 0.0D, 10000.0D, 1.0D)
    );
    private final NumberSetting positionY = addHiddenSetting(
        new NumberSetting("Position Y", 76.0D, 0.0D, 10000.0D, 1.0D)
    );
    private final EventListener<AttackEntityEvent> attackListener =
        new EventListener<AttackEntityEvent>() {
            @Override
            public void onEvent(AttackEntityEvent event) {
                recordHit(event);
            }
        };
    private final EventListener<Render2DEvent> renderListener =
        new EventListener<Render2DEvent>() {
            @Override
            public void onEvent(Render2DEvent event) {
                renderHud();
            }
        };

    private double lastReach;

    public ReachDisplayModule() {
        super(
            "Reach Display",
            "Displays the distance of your last hit.",
            ModuleCategory.PVP,
            false,
            Keyboard.KEY_NONE
        );
    }

    @Override
    protected void onEnable() {
        subscribe(AttackEntityEvent.class, attackListener);
        subscribe(Render2DEvent.class, renderListener);
    }

    @Override
    protected void onDisable() {
        unsubscribe(AttackEntityEvent.class, attackListener);
        unsubscribe(Render2DEvent.class, renderListener);
        lastReach = 0.0D;
    }

    @Override
    public void renderHud() {
        if (minecraft.theWorld == null || minecraft.thePlayer == null) {
            lastReach = 0.0D;
        }

        String text = getDisplayText();
        int padding = getPadding();
        int textWidth = minecraft.fontRendererObj.getStringWidth(text);
        int textHeight = minecraft.fontRendererObj.FONT_HEIGHT;
        float renderScale = scale.getValue().floatValue();
        int renderX = Math.round(getPositionX() / renderScale);
        int renderY = Math.round(getPositionY() / renderScale);

        GlStateManager.pushMatrix();
        GlStateManager.scale(renderScale, renderScale, 1.0F);

        if (showBackground.isEnabled()) {
            Gui.drawRect(
                renderX,
                renderY,
                renderX + textWidth + padding * 2,
                renderY + textHeight + padding * 2,
                backgroundColor.getColor()
            );
        }
        minecraft.fontRendererObj.drawString(
            text,
            renderX + padding,
            renderY + padding,
            textColor.getColor()
        );

        GlStateManager.popMatrix();
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
        int padding = getPadding();
        return Math.round(
            (minecraft.fontRendererObj.getStringWidth(getDisplayText())
                + padding * 2)
                * scale.getValue().floatValue()
        );
    }

    @Override
    public int getHudHeight() {
        int padding = getPadding();
        return Math.round(
            (minecraft.fontRendererObj.FONT_HEIGHT + padding * 2)
                * scale.getValue().floatValue()
        );
    }

    private void recordHit(AttackEntityEvent event) {
        if (minecraft.theWorld == null
            || minecraft.thePlayer == null
            || event.getPlayer() == null
            || event.getTarget() == null
            || event.getPlayer() != minecraft.thePlayer) {
            return;
        }

        Entity target = event.getTarget();
        AxisAlignedBB box = target.getEntityBoundingBox();
        if (box == null) {
            return;
        }

        lastReach = calculateEyeToBoxDistance(event.getPlayer(), box);
    }

    private String getDisplayText() {
        return String.format(Locale.US, "%.2f Blocks", lastReach);
    }

    private int getPadding() {
        return showBackground.isEnabled() ? PADDING : 0;
    }

    private double calculateEyeToBoxDistance(Entity player, AxisAlignedBB box) {
        double eyeX = player.posX;
        double eyeY = player.posY + player.getEyeHeight();
        double eyeZ = player.posZ;
        double closestX = clamp(eyeX, box.minX, box.maxX);
        double closestY = clamp(eyeY, box.minY, box.maxY);
        double closestZ = clamp(eyeZ, box.minZ, box.maxZ);
        double diffX = eyeX - closestX;
        double diffY = eyeY - closestY;
        double diffZ = eyeZ - closestZ;

        return Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
