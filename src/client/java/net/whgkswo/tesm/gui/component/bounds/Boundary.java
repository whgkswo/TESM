package net.whgkswo.tesm.gui.component.bounds;

public class Boundary {
    private BoundType boundType;
    private double xRatio;
    private double yRatio;

    public Boundary(BoundType boundType, double xRatio, double yRatio) {
        this.boundType = boundType;
        this.xRatio = xRatio;
        this.yRatio = yRatio;
    }

    public BoundType getBoundType() {
        return boundType;
    }
    public double getxRatio() {
        return xRatio;
    }

    public double getyRatio() {
        return yRatio;
    }

    public enum BoundType {
        FIXED,
        FLEXIBLE
    }
}
