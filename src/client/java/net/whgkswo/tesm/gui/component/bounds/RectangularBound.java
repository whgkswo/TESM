package net.whgkswo.tesm.gui.component.bounds;

public class RectangularBound extends Boundary {
    private double widthRatio;
    private double heightRatio;

    public RectangularBound(BoundType boundType, double xRatio, double yRatio, double widthRatio, double heightRatio) {
        super(boundType, xRatio, yRatio);
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
