package net.whgkswo.tesm.gui.component.bounds;

import lombok.Getter;

@Getter
public class Boundary {
    private double xOffsetRatio;
    private double yOffsetRatio;

    public Boundary(double xOffsetRatio, double yOffsetRatio) {
        this.xOffsetRatio = xOffsetRatio;
        this.yOffsetRatio = yOffsetRatio;
    }
}
