package net.whgkswo.tesm.gui.component.box;

public class LinearBox extends Box{
    private double lengthRatio;
    private int thickness;

    public LinearBox(double xRatio, double yRatio, double lengthRatio, int thickness) {
        super(xRatio, yRatio);
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
