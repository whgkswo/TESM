package net.whgkswo.tesm.gui.component;

import net.whgkswo.tesm.gui.colors.CustomColor;
import net.whgkswo.tesm.gui.component.box.Box;

public abstract class SingleColorTexture<T extends Box> extends GuiComponent{
    private T renderingBox;
    private T interactionBox;

    public SingleColorTexture(CustomColor color, T renderingBox) {
        super(color);
        this.renderingBox = renderingBox;
    }
    public SingleColorTexture(CustomColor color, T renderingBox, T interactionBox) {
        super(color);
        this.renderingBox = renderingBox;
        this.interactionBox = interactionBox;
    }

    public T getRenderingBox() {
        return renderingBox;
    }

    public T getInteractionBox() {
        return interactionBox;
    }
}
