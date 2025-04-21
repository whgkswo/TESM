package net.whgkswo.tesm.gui.component.bounds;

import lombok.Getter;
import net.whgkswo.tesm.gui.component.GuiAxis;

@Getter
public class LinearBound extends Boundary {
    private GuiAxis axis;
    private double lengthRatio;
    private int thickness;
    private int xOffset;
    private int yOffset;

    public LinearBound(double xOffsetRatio, double yOffsetRatio, GuiAxis axis, double lengthRatio, int thickness) {
        this(xOffsetRatio, yOffsetRatio, axis, lengthRatio, thickness, 0, 0);
    }

    public LinearBound(double xOffsetRatio, double yOffsetRatio, GuiAxis axis, double lengthRatio, int thickness, int xOffset, int yOffset) {
        super(xOffsetRatio, yOffsetRatio);
        this.axis = axis;
        this.lengthRatio = lengthRatio;
        this.thickness = thickness;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
}
