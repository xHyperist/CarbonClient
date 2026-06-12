package com.carbonclient.ui.component;

import com.carbonclient.ui.render.RenderUtils;

public final class CardComponent {

    public void render(
        int x,
        int y,
        int width,
        int height,
        int mouseX,
        int mouseY,
        int accent
    ) {
        boolean hovered = mouseX >= x
            && mouseX < x + width
            && mouseY >= y
            && mouseY < y + height;
        RenderUtils.drawCard(x, y, width, height, hovered, accent);
    }
}
