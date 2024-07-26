package net.whgkswo.tesm.gui.component;

import net.minecraft.util.Identifier;

public abstract class SingleColorTexture extends GuiComponent{
    private Identifier texture;
    private Box renderingBox;
    private Box interactionBox;

    public SingleColorTexture(Identifier texture, Box renderingBox) {
        this.texture = texture;
        this.renderingBox = renderingBox;
    }
    public SingleColorTexture(Identifier texture, Box renderingBox, Box interactionBox) {
        this.texture = texture;
        this.renderingBox = renderingBox;
        this.interactionBox = interactionBox;
    }

    public Identifier getTexture() {
        return texture;
    }

    public Box getRenderingBox() {
        return renderingBox;
    }

    public Box getInteractionBox() {
        return interactionBox;
    }
}
