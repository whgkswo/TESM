package net.whgkswo.tesm.gui.component.bounds;

public class LinearBound extends Boundary {
    private double lengthRatio;
    private int thickness;

    public LinearBound(BoundType boundType, double xRatio, double yRatio, double lengthRatio, int thickness) {
        super(boundType, xRatio, yRatio);
        this.lengthRatio = lengthRatio;
        this.thickness = thickness;
    }

    public double getLengthRatio() {
        return lengthRatio;
    }

    public int getThickness() {
        return thickness;
    }
}
