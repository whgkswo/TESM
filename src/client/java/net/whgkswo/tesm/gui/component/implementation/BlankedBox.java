package net.whgkswo.tesm.gui.component.implementation;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.gui.colors.Colors;
import net.whgkswo.tesm.gui.component.LineDirection;
import net.whgkswo.tesm.gui.component.SingleColorTexture;

import java.util.HashMap;
import java.util.Map;

public class BlankedBox extends SingleColorTexture {
    private Map<LineSide, StraightLine> lines = new HashMap<>();

    public BlankedBox(Identifier texture, double xRatio, double yRatio, double widthRatio, double heightRatio, int thickness) {
        super(texture, xRatio, yRatio, widthRatio, heightRatio);
        lines.put(LineSide.UP,
                new StraightLine(
                        LineDirection.HORIZONTAL,
                        Colors.COLORED_TEXTURES.get("aaa685"),
                        xRatio, yRatio, widthRatio, heightRatio, thickness
                        ));
        lines.put(LineSide.DOWN,
                new StraightLine(
                        LineDirection.HORIZONTAL,
                        Colors.COLORED_TEXTURES.get("aaa685"),
                        xRatio, yRatio + heightRatio, widthRatio, heightRatio, thickness, -thickness
                ));
        lines.put(LineSide.LEFT,
                new StraightLine(
                        LineDirection.VERTICAL,
                        Colors.COLORED_TEXTURES.get("aaa685"),
                        xRatio, yRatio, widthRatio, heightRatio, thickness
                ));
        lines.put(LineSide.RIGHT,
                new StraightLine(
                        LineDirection.VERTICAL,
                        Colors.COLORED_TEXTURES.get("aaa685"),
                        xRatio + widthRatio, yRatio, widthRatio, heightRatio, thickness, -thickness
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
