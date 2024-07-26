package net.whgkswo.tesm.gui.component;

import net.minecraft.util.Identifier;
import net.whgkswo.tesm.gui.component.box.Box;

public abstract class SingleColorTexture<T extends Box> extends GuiComponent{
    private Identifier texture;
    private T renderingBox;
    private T interactionBox;

    public SingleColorTexture(Identifier texture, T renderingBox) {
        this.texture = texture;
        this.renderingBox = renderingBox;
    }
    public SingleColorTexture(Identifier texture, T renderingBox, T interactionBox) {
        this.texture = texture;
        this.renderingBox = renderingBox;
        this.interactionBox = interactionBox;
    }

    public Identifier getTexture() {
        return texture;
    }

    public T getRenderingBox() {
        return renderingBox;
    }

    public T getInteractionBox() {
        return interactionBox;
    }
}
