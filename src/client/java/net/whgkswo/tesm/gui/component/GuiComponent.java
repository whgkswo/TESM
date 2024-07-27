package net.whgkswo.tesm.gui.component;

import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.colors.CustomColor;

public abstract class GuiComponent {
    private CustomColor color;

    public GuiComponent(CustomColor color) {
        this.color = color;
    }

    public abstract void render(DrawContext context);

    public CustomColor getColor() {
        return color;
    }
}
