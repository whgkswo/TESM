package net.whgkswo.tesm.gui.component.bounds;

public class Boundary {
    private BoundType boundType;
    private double xMarginRatio;
    private double yMarginRatio;

    public Boundary(BoundType boundType, double xMarginRatio, double yMarginRatio) {
        this.boundType = boundType;
        this.xMarginRatio = xMarginRatio;
        this.yMarginRatio = yMarginRatio;
    }

    public BoundType getBoundType() {
        return boundType;
    }
    public double getxMarginRatio() {
        return xMarginRatio;
    }

    public double getyMarginRatio() {
        return yMarginRatio;
    }

    public enum BoundType {
        FIXED,
        FLEXIBLE
    }
}
