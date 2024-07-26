package net.whgkswo.tesm.gui.component;

public class Box {
    private double xRatio;
    private double yRatio;
    private double widthRatio;
    private double heightRatio;

    public Box(double xRatio, double yRatio, double widthRatio, double heightRatio) {
        this.xRatio = xRatio;
        this.yRatio = yRatio;
        this.widthRatio = widthRatio;
        this.heightRatio = heightRatio;
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
