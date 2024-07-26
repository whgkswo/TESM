package net.whgkswo.tesm.gui.screen.component;

import net.minecraft.util.Identifier;

public abstract class SingleColorTexture extends GuiComponent{
    private Identifier texture;
    private double xRatio;
    private double yRatio;
    private double widthRatio;
    private double heightRatio;

    public SingleColorTexture(Identifier texture, double xRatio, double yRatio, double widthRatio, double heightRatio) {
        this.texture = texture;
        this.xRatio = xRatio;
        this.yRatio = yRatio;
        this.widthRatio = widthRatio;
        this.heightRatio = heightRatio;
    }

    public Identifier getTexture() {
        return texture;
    }

    public double getxRatio() {
        return xRatio;
    }

    public double getyRatio() {
        return yRatio;
    }

    public double getWidthRatio() {
        return widthRatio;
    }

    public double getHeightRatio() {
        return heightRatio;
    }
}
