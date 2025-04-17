package net.whgkswo.tesm.gui.component.bounds;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RelativeBound {
    public static final RelativeBound FULL_SCREEN = new RelativeBound(1, 1, 0, 0);

    private double widthRatio;
    private double heightRatio;
    private double xMarginRatio;
    private double yMarginRatio;

    public RelativeBound(double widthRatio, double heightRatio){
        this.widthRatio = widthRatio;
        this.heightRatio = heightRatio;
    }
}
