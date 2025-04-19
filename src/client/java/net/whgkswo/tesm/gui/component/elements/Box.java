package net.whgkswo.tesm.gui.component.elements;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiAxis;
import net.whgkswo.tesm.gui.component.ParentComponent;
import net.whgkswo.tesm.gui.component.bounds.Boundary;
import net.whgkswo.tesm.gui.component.bounds.LinearBound;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.elements.style.BoxStyle;

import java.util.HashMap;
import java.util.Map;

@SuperBuilder
public class Box extends ParentComponent<Box, BoxStyle> {
    @Builder.Default
    private Map<LineSide, StraightLine> lines = new HashMap<>();
    @Getter
    private RelativeBound bound;
    @Getter
    private TesmColor edgeColor;
    @Getter
    private TesmColor backgroundColor;

    public Box(ParentComponent parent, RelativeBound bound, GuiAxis axis, HorizontalAlignment childrenAlignment, TesmColor edgeColor, TesmColor backgroundColor, int thickness) {
        super(parent, bound, axis, childrenAlignment);
        this.bound = bound;
        this.edgeColor = edgeColor;
        this.backgroundColor = backgroundColor;

        // 모서리 없으면 등록 x
        if(edgeColor.equals(TesmColor.TRANSPARENT) || thickness == 0) return;

        double xRatio = bound.getXMarginRatio();
        double yRatio = bound.getYMarginRatio();
        double widthRatio = bound.getWidthRatio();
        double heightRatio = bound.getHeightRatio();
        lines.put(LineSide.UP,
                new StraightLine(
                        new TesmColor(200, 160, 130),
                        GuiAxis.HORIZONTAL,
                        new LinearBound(Boundary.BoundType.FIXED, xRatio, yRatio, widthRatio, thickness)
                        ));
        lines.put(LineSide.DOWN,
                new StraightLine(
                        new TesmColor(200, 160, 130),
                        GuiAxis.HORIZONTAL,
                        new LinearBound(Boundary.BoundType.FIXED, xRatio,yRatio + heightRatio, widthRatio, thickness), -thickness
                ));
        lines.put(LineSide.LEFT,
                new StraightLine(
                        new TesmColor(200, 160, 130),
                        GuiAxis.VERTICAL,
                        new LinearBound(Boundary.BoundType.FIXED, xRatio, yRatio, heightRatio, thickness)
                ));
        lines.put(LineSide.RIGHT,
                new StraightLine(
                        new TesmColor(200, 160, 130),
                        GuiAxis.VERTICAL,
                        new LinearBound(Boundary.BoundType.FIXED, xRatio + widthRatio, yRatio, heightRatio, thickness), -thickness
                ));
    }

    @Override
    protected void renderSelf(DrawContext context) {
        RelativeBound absoluteBound = getScreenRelativeBoundWithUpdate();

        if(!backgroundColor.equals(TesmColor.TRANSPARENT)){
            RenderingHelper.fill(
                    context,
                    backgroundColor,
                    absoluteBound.getXMarginRatio(),
                    absoluteBound.getYMarginRatio(),
                    absoluteBound.getWidthRatio(),
                    absoluteBound.getHeightRatio()
            );
        }

        // 모서리 출력
        lines.forEach((lineSide, straightLine) -> {
            straightLine.renderSelf(context);
        });
    }

    @Override
    protected RelativeBound getBound(){
        return bound;
    }

    @Override
    protected Class<?> getStyleType() {
        return BoxStyle.class;
    }

    private enum LineSide{
        UP,
        LEFT,
        RIGHT,
        DOWN
    }
}
