package net.whgkswo.tesm.gui.component.bounds;

public class RectangularBound extends Boundary {
    public static final RectangularBound FULL_SCREEN = new RectangularBound(BoundType.FIXED, 0, 0, 1, 1);

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

    public int getRenderingPosX(int screenWidth){
        return (int)(screenWidth * this.getxMarginRatio());
    }
    public int getRenderingPosY(int screenHeight){
        return (int)(screenHeight * this.getyMarginRatio());
    }
}
