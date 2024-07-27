package net.whgkswo.tesm.gui.component.implementation;

import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.colors.CustomColor;
import net.whgkswo.tesm.gui.component.LineDirection;
import net.whgkswo.tesm.gui.component.SingleColorTexture;
import net.whgkswo.tesm.gui.component.box.LinearBox;
import net.whgkswo.tesm.gui.component.box.RectangularBox;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BlankedBox extends SingleColorTexture {
    private Map<LineSide, StraightLine> lines = new HashMap<>();

    public BlankedBox(CustomColor color, RectangularBox box, int thickness) {
        super(color, box);
        double xRatio = box.getxRatio();
        double yRatio = box.getyRatio();
        double widthRatio = box.getWidthRatio();
        double heightRatio = box.getHeightRatio();
        lines.put(LineSide.UP,
                new StraightLine(
                        new CustomColor(200, 160, 130),
                        LineDirection.HORIZONTAL,
                        new LinearBox(xRatio, yRatio, widthRatio, thickness)
                        ));
        lines.put(LineSide.DOWN,
                new StraightLine(
                        new CustomColor(200, 160, 130),
                        LineDirection.HORIZONTAL,
                        new LinearBox(xRatio,yRatio + heightRatio, widthRatio, thickness), -thickness
                ));
        lines.put(LineSide.LEFT,
                new StraightLine(
                        new CustomColor(200, 160, 130),
                        LineDirection.VERTICAL,
                        new LinearBox(xRatio, yRatio, heightRatio, thickness)
                ));
        lines.put(LineSide.RIGHT,
                new StraightLine(
                        new CustomColor(200, 160, 130),
                        LineDirection.VERTICAL,
                        new LinearBox(xRatio + widthRatio, yRatio, heightRatio, thickness), -thickness
                ));
    }

    @Override
    public void render(DrawContext context) {
        lines.forEach((lineSide, straightLine) -> {
            straightLine.render(context);
        });
    }
    private enum LineSide{
        UP,
        LEFT,
        RIGHT,
        DOWN
    }
}
