package net.whgkswo.tesm.gui.component.components;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.GuiDirection;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiAxis;
import net.whgkswo.tesm.gui.component.ParentComponent;
import net.whgkswo.tesm.gui.component.bounds.LinearBound;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.components.builder.BoxPanelBuilder;
import net.whgkswo.tesm.gui.component.components.features.BackgroundHoverHandler;
import net.whgkswo.tesm.gui.component.components.features.base.BackgroundComponent;
import net.whgkswo.tesm.gui.component.components.features.base.HoverHandler;
import net.whgkswo.tesm.gui.component.components.style.BoxStyle;

import java.util.HashMap;
import java.util.Map;

// 스타일 요소가 아닌 필드는 여기에 초기값 명시
@Getter
public class BoxPanel extends ParentComponent<BoxPanel, BoxStyle> implements BackgroundComponent {
    private final Map<GuiDirection, StraightLine> edgeLines = new HashMap<>();
    @Setter
    private RelativeBound bound;
    @Setter
    private TesmColor edgeColor;
    @Setter
    private int edgeThickness;
    @Setter
    private TesmColor backgroundColor;

    public BoxPanel(){
        super();
    }

    public static BoxPanelBuilder builder(){
        return new BoxPanelBuilder();
    }

    @Override
    protected void renderSelf(DrawContext context) {
        RelativeBound absoluteBound = getAbsoluteBoundWithUpdate();

        if(!backgroundColor.equals(TesmColor.TRANSPARENT)){
            RenderingHelper.fill(
                    context,
                    backgroundColor,
                    absoluteBound.getXOffsetRatio(),
                    absoluteBound.getYOffsetRatio(),
                    absoluteBound.getWidthRatio(),
                    absoluteBound.getHeightRatio()
            );
        }

        // 모서리 출력
        edgeLines.forEach((lineSide, straightLine) -> {
            straightLine.renderSelf(context);
        });
    }

    @Override
    protected void initializeStyleExtended(){
        // 모서리 생성
        double xRatio = bound.getXOffsetRatio();
        double yRatio = bound.getYOffsetRatio();
        double widthRatio = bound.getWidthRatio();
        double heightRatio = bound.getHeightRatio();
        edgeLines.put(GuiDirection.TOP,
                new StraightLine(
                        edgeColor,
                        new LinearBound(xRatio, yRatio, GuiAxis.HORIZONTAL, widthRatio, edgeThickness)
                ));
        edgeLines.put(GuiDirection.BOTTOM,
                new StraightLine(
                        edgeColor,
                        new LinearBound(xRatio,yRatio, GuiAxis.HORIZONTAL, widthRatio, edgeThickness)
                ));
        edgeLines.put(GuiDirection.LEFT,
                new StraightLine(
                        edgeColor,
                        new LinearBound(xRatio, yRatio, GuiAxis.VERTICAL, heightRatio, edgeThickness)
                ));
        edgeLines.put(GuiDirection.RIGHT,
                new StraightLine(
                        edgeColor,
                        new LinearBound(xRatio, yRatio, GuiAxis.VERTICAL, heightRatio, edgeThickness)
                ));
    }

    @Override
    public RelativeBound getBound(){
        return bound;
    }

    @Override
    protected Class<BoxStyle> getStyleType() {
        return BoxStyle.class;
    }
}
