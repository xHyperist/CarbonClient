package com.carbonclient.modules.render;

import com.carbonclient.event.EventListener;
import com.carbonclient.event.impl.AttackEntityEvent;
import com.carbonclient.event.impl.ClientTickEvent;
import com.carbonclient.event.impl.PlayerDamageEvent;
import com.carbonclient.event.impl.Render2DEvent;
import com.carbonclient.module.DraggableHudModule;
import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleCategory;
import com.carbonclient.setting.impl.BooleanSetting;
import com.carbonclient.setting.impl.ColorSetting;
import com.carbonclient.setting.impl.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;

public final class ComboDisplayModule
    extends Module
    implements DraggableHudModule {

    private static final int PADDING = 3;
    private static final long HIT_DEBOUNCE_MS = 400L;
    private static final long PENDING_EXPIRE_MS = 250L;

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final NumberSetting resetDelay = addSetting(
        new NumberSetting("Reset Delay", 10.0D, 10.0D, 30.0D, 1.0D)
    );
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
        new NumberSetting("Position Y", 92.0D, 0.0D, 10000.0D, 1.0D)
    );
    private final EventListener<AttackEntityEvent> attackListener =
        new EventListener<AttackEntityEvent>() {
            @Override
            public void onEvent(AttackEntityEvent event) {
                recordHit(event);
            }
        };
    private final EventListener<PlayerDamageEvent> damageListener =
        new EventListener<PlayerDamageEvent>() {
            @Override
            public void onEvent(PlayerDamageEvent event) {
                resetOnDamage(event);
            }
        };
    private final EventListener<ClientTickEvent> tickListener =
        new EventListener<ClientTickEvent>() {
            @Override
            public void onEvent(ClientTickEvent event) {
                validatePendingHit();
            }
        };
    private final EventListener<Render2DEvent> renderListener =
        new EventListener<Render2DEvent>() {
            @Override
            public void onEvent(Render2DEvent event) {
                renderHud();
            }
        };

    private int combo;
    private long lastHitAt;
    private EntityLivingBase pendingTarget;
    private long pendingStartedAt;
    private int pendingInitialHurtTime;
    private int lastCountedEntityId = -1;
    private long lastCountedAtMs;

    public ComboDisplayModule() {
        super(
            "Combo Display",
            "Displays your current hit combo.",
            ModuleCategory.PVP,
            false,
            Keyboard.KEY_NONE
        );
    }

    @Override
    protected void onEnable() {
        subscribe(AttackEntityEvent.class, attackListener);
        subscribe(PlayerDamageEvent.class, damageListener);
        subscribe(ClientTickEvent.class, tickListener);
        subscribe(Render2DEvent.class, renderListener);
    }

    @Override
    protected void onDisable() {
        unsubscribe(AttackEntityEvent.class, attackListener);
        unsubscribe(PlayerDamageEvent.class, damageListener);
        unsubscribe(ClientTickEvent.class, tickListener);
        unsubscribe(Render2DEvent.class, renderListener);
        resetCombo();
    }

    @Override
    public void renderHud() {
        updateTimedReset();

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
        if (!(target instanceof EntityLivingBase)) {
            clearPendingHit();
            return;
        }

        long now = System.currentTimeMillis();
        if (isDuplicateCount(target, now)) {
            return;
        }

        pendingTarget = (EntityLivingBase) target;
        pendingStartedAt = now;
        pendingInitialHurtTime = pendingTarget.hurtTime;
    }

    private void resetOnDamage(PlayerDamageEvent event) {
        if (minecraft.theWorld == null
            || minecraft.thePlayer == null
            || event.getPlayer() == null
            || event.getPlayer() != minecraft.thePlayer) {
            return;
        }
        if (event.getAttacker() == null) {
            return;
        }

        resetCombo();
    }

    private void validatePendingHit() {
        updateTimedReset();
        if (pendingTarget == null) {
            return;
        }
        if (minecraft.theWorld == null || minecraft.thePlayer == null) {
            resetCombo();
            return;
        }

        long now = System.currentTimeMillis();
        if (now - pendingStartedAt > PENDING_EXPIRE_MS) {
            clearPendingHit();
            return;
        }
        if (pendingTarget.isDead) {
            clearPendingHit();
            return;
        }
        if (isDuplicateCount(pendingTarget, now)) {
            clearPendingHit();
            return;
        }
        if (pendingTarget.hurtTime <= pendingInitialHurtTime) {
            return;
        }

        combo++;
        lastHitAt = now;
        lastCountedEntityId = pendingTarget.getEntityId();
        lastCountedAtMs = now;
        clearPendingHit();
    }

    private void updateTimedReset() {
        if (combo <= 0 || lastHitAt <= 0L) {
            return;
        }

        long resetDelayMs = (long) (resetDelay.getValue() * 1000.0D);
        if (System.currentTimeMillis() - lastHitAt >= resetDelayMs) {
            resetCombo();
        }
    }

    private void resetCombo() {
        combo = 0;
        lastHitAt = 0L;
        lastCountedEntityId = -1;
        lastCountedAtMs = 0L;
        clearPendingHit();
    }

    private String getDisplayText() {
        return "Combo: " + combo;
    }

    private int getPadding() {
        return showBackground.isEnabled() ? PADDING : 0;
    }

    private boolean isDuplicateCount(Entity target, long now) {
        return target.getEntityId() == lastCountedEntityId
            && now - lastCountedAtMs < HIT_DEBOUNCE_MS;
    }

    private void clearPendingHit() {
        pendingTarget = null;
        pendingStartedAt = 0L;
        pendingInitialHurtTime = 0;
    }
}
