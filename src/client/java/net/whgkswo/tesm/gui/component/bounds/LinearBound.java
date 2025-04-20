package net.whgkswo.tesm.gui.component.bounds;

import lombok.Getter;
import net.whgkswo.tesm.gui.component.GuiAxis;

@Getter
public class LinearBound extends Boundary {
    private GuiAxis axis;
    private double lengthRatio;
    private int thickness;

    public LinearBound(double xMarginRatio, double yMarginRatio, GuiAxis axis, double lengthRatio, int thickness) {
        super(xMarginRatio, yMarginRatio);
        this.axis = axis;
        this.lengthRatio = lengthRatio;
        this.thickness = thickness;
    }
}
