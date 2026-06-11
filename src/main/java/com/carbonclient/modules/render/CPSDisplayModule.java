package com.carbonclient.modules.render;

import com.carbonclient.event.EventListener;
import com.carbonclient.event.impl.MouseButtonEvent;
import com.carbonclient.event.impl.Render2DEvent;
import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleCategory;
import java.util.ArrayDeque;
import java.util.Deque;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

public final class CPSDisplayModule extends Module {

    private static final long CPS_WINDOW_MILLIS = 1000L;
    private static final int TEXT_COLOR = 0xFFFFFF;
    private static final int BACKGROUND_COLOR = 0xB0121824;
    private static final int PADDING = 3;
    private static final int X = 5;
    private static final int Y = 16;

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final Deque<Long> leftClicks = new ArrayDeque<Long>();
    private final EventListener<MouseButtonEvent> mouseListener =
        new EventListener<MouseButtonEvent>() {
            @Override
            public void onEvent(MouseButtonEvent event) {
                recordClick(event.getButton());
            }
        };
    private final EventListener<Render2DEvent> renderListener =
        new EventListener<Render2DEvent>() {
            @Override
            public void onEvent(Render2DEvent event) {
                renderCps();
            }
        };

    public CPSDisplayModule() {
        super(
            "CPS Display",
            "Displays left mouse clicks per second.",
            ModuleCategory.RENDER
        );
        setKeyCode(Keyboard.KEY_C);
    }

    @Override
    protected void onEnable() {
        subscribe(MouseButtonEvent.class, mouseListener);
        subscribe(Render2DEvent.class, renderListener);
    }

    @Override
    protected void onDisable() {
        unsubscribe(MouseButtonEvent.class, mouseListener);
        unsubscribe(Render2DEvent.class, renderListener);
        leftClicks.clear();
    }

    private void recordClick(int button) {
        if (button == 0) {
            leftClicks.addLast(System.currentTimeMillis());
        }
    }

    private void renderCps() {
        long now = System.currentTimeMillis();
        removeExpiredClicks(leftClicks, now);

        String text = leftClicks.size() + " CPS";
        int width = minecraft.fontRendererObj.getStringWidth(text);
        int height = minecraft.fontRendererObj.FONT_HEIGHT;

        Gui.drawRect(
            X,
            Y,
            X + width + PADDING * 2,
            Y + height + PADDING * 2,
            BACKGROUND_COLOR
        );
        minecraft.fontRendererObj.drawString(
            text,
            X + PADDING,
            Y + PADDING,
            TEXT_COLOR
        );
    }

    private void removeExpiredClicks(Deque<Long> clicks, long now) {
        while (!clicks.isEmpty() && now - clicks.peekFirst() >= CPS_WINDOW_MILLIS) {
            clicks.removeFirst();
        }
    }
}
