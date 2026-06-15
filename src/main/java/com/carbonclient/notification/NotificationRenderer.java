package com.carbonclient.notification;

import com.carbonclient.event.EventBus;
import com.carbonclient.event.EventListener;
import com.carbonclient.event.impl.Render2DEvent;
import com.carbonclient.ui.render.RenderUtils;
import com.carbonclient.ui.theme.CarbonTheme;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

public final class NotificationRenderer {

    private static final int WIDTH = 220;
    private static final int HEIGHT = 42;
    private static final int GAP = 6;
    private static final int MARGIN = 8;
    private static final int MAX_VISIBLE = 5;
    private static final long FADE_IN_MS = 180L;
    private static final long FADE_OUT_MS = 260L;

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final NotificationManager notificationManager;
    private final EventListener<Render2DEvent> renderListener =
        new EventListener<Render2DEvent>() {
            @Override
            public void onEvent(Render2DEvent event) {
                render();
            }
        };

    public NotificationRenderer(
        EventBus eventBus,
        NotificationManager notificationManager
    ) {
        if (eventBus == null || notificationManager == null) {
            throw new IllegalArgumentException(
                "EventBus and NotificationManager cannot be null."
            );
        }

        this.notificationManager = notificationManager;
        eventBus.subscribe(Render2DEvent.class, renderListener);
    }

    public void render() {
        if (minecraft.fontRendererObj == null) {
            return;
        }

        ScaledResolution resolution = new ScaledResolution(minecraft);
        render(resolution.getScaledWidth(), resolution.getScaledHeight());
    }

    public void render(int screenWidth, int screenHeight) {
        FontRenderer fontRenderer = minecraft.fontRendererObj;
        if (fontRenderer == null || screenWidth <= 0 || screenHeight <= 0) {
            return;
        }

        long now = System.currentTimeMillis();
        List<Notification> notifications =
            notificationManager.getActiveNotifications(now);
        int firstIndex = Math.max(0, notifications.size() - MAX_VISIBLE);
        int stackIndex = 0;

        for (int index = notifications.size() - 1; index >= firstIndex; index--) {
            Notification notification = notifications.get(index);
            float visibility = getVisibility(notification, now);
            if (visibility <= 0.0F) {
                continue;
            }

            int slide = Math.round((1.0F - visibility) * 22.0F);
            int x = screenWidth - MARGIN - WIDTH + slide;
            int y = screenHeight
                - MARGIN
                - HEIGHT
                - stackIndex * (HEIGHT + GAP);
            drawNotification(
                fontRenderer,
                notification,
                x,
                y,
                visibility
            );
            stackIndex++;
        }
    }

    private void drawNotification(
        FontRenderer fontRenderer,
        Notification notification,
        int x,
        int y,
        float visibility
    ) {
        int accent = withAlpha(
            notification.getType().getAccentColor(),
            visibility
        );
        RenderUtils.drawPanel(
            x,
            y,
            WIDTH,
            HEIGHT,
            withAlpha(0xEE121A2E, visibility)
        );
        RenderUtils.drawOutline(
            x,
            y,
            WIDTH,
            HEIGHT,
            withAlpha(CarbonTheme.BORDER, visibility)
        );
        RenderUtils.drawPanel(
            x,
            y,
            CarbonTheme.ACCENT_BAR_WIDTH,
            HEIGHT,
            accent
        );

        String title = fontRenderer.trimStringToWidth(
            notification.getTitle(),
            WIDTH - 20
        );
        String message = fontRenderer.trimStringToWidth(
            notification.getMessage(),
            WIDTH - 20
        );
        RenderUtils.drawText(
            fontRenderer,
            title,
            x + 10,
            y + 8,
            withAlpha(CarbonTheme.TEXT, visibility)
        );
        RenderUtils.drawText(
            fontRenderer,
            message,
            x + 10,
            y + 24,
            withAlpha(CarbonTheme.MUTED_TEXT, visibility)
        );
    }

    private float getVisibility(Notification notification, long now) {
        long age = notification.getAge(now);
        long remaining = notification.getDurationMs() - age;

        if (age < FADE_IN_MS) {
            return clamp(age / (float) FADE_IN_MS);
        }
        if (remaining < FADE_OUT_MS) {
            return clamp(remaining / (float) FADE_OUT_MS);
        }
        return 1.0F;
    }

    private int withAlpha(int color, float visibility) {
        int originalAlpha = color >>> 24;
        int alpha = Math.round(originalAlpha * clamp(visibility));
        return alpha << 24 | color & 0x00FFFFFF;
    }

    private float clamp(float value) {
        return Math.max(0.0F, Math.min(1.0F, value));
    }
}
