package net.whgkswo.tesm.gui.component.box;

public class RectangularBox extends Box{
    private double widthRatio;
    private double heightRatio;

    public RectangularBox(double xRatio, double yRatio, double widthRatio, double heightRatio) {
        super(xRatio, yRatio);
        this.widthRatio = widthRatio;
        this.heightRatio = heightRatio;
    }

    public double getWidthRatio() {
        return widthRatio;
    }

    public double getHeightRatio() {
        return heightRatio;
    }
}
