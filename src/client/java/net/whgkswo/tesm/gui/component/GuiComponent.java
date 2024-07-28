package net.whgkswo.tesm.gui.component;

import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.colors.CustomColor;
import net.whgkswo.tesm.gui.component.bounds.Boundary;

public abstract class GuiComponent<T extends Boundary> {
    private CustomColor color;
    private T renderingBound;
    private T interactionBound;

    public GuiComponent(CustomColor color, T renderingBound) {
        this.color = color;
        this.renderingBound = renderingBound;
    }
    public GuiComponent(CustomColor color, T renderingBound, T interactionBound) {
        this.color = color;
        this.renderingBound = renderingBound;
        this.interactionBound = interactionBound;
    }
    public T getRenderingBound() {
        return renderingBound;
    }

    public T getInteractionBound() {
        return interactionBound;
    }

    public abstract void render(DrawContext context);

    public CustomColor getColor() {
        return color;
    }
}
