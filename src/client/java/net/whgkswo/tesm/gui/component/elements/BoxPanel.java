package net.whgkswo.tesm.gui.component.elements;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
import net.whgkswo.tesm.gui.component.elements.builder.BoxPanelBuilder;
import net.whgkswo.tesm.gui.component.elements.features.Hoverable;
import net.whgkswo.tesm.gui.component.elements.style.BoxStyle;

import java.util.HashMap;
import java.util.Map;

// 스타일 요소가 아닌 필드는 여기에 초기값 명시
public class BoxPanel extends ParentComponent<BoxPanel, BoxStyle> implements Hoverable {
    private Map<LineSide, StraightLine> lines = new HashMap<>();
    @Getter
    @Setter
    private RelativeBound bound;
    @Setter
    @Getter
    private TesmColor edgeColor;
    @Setter
    @Getter
    private TesmColor backgroundColor;
    private boolean isHovered;

    public BoxPanel(){
        super();
    }

    public BoxPanel(ParentComponent parent, RelativeBound bound, GuiAxis axis, HorizontalAlignment childrenAlignment, TesmColor edgeColor, TesmColor backgroundColor, int thickness) {
        super(parent, bound, axis, childrenAlignment);
        this.bound = bound;
        this.edgeColor = edgeColor;
        this.backgroundColor = backgroundColor;

        // 모서리 없으면 등록 x
        if(edgeColor.equals(TesmColor.TRANSPARENT) || thickness == 0) return;

        double xRatio = bound.getXOffsetRatio();
        double yRatio = bound.getYOffsetRatio();
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
        lines.forEach((lineSide, straightLine) -> {
            straightLine.renderSelf(context);
        });
    }

    @Override
    public RelativeBound getBound(){
        return bound;
    }

    @Override
    protected Class<BoxStyle> getStyleType() {
        return BoxStyle.class;
    }

    @Override
    public void handleHover() {
        if(backgroundColor.equals(TesmColor.TRANSPARENT)) return;
        TesmColor backgroundColorBackup = backgroundColor;
        onHover(backgroundColorBackup);
        isHovered = true;
    }

    @Override
    public void handleHoverExit(){
        if(backgroundColor.equals(TesmColor.TRANSPARENT)) return;
        onHoverExit(backgroundColor);
        isHovered = false;
    }

    @Override
    public boolean isHovered(){
        return isHovered;
    }

    public void onHover(TesmColor originalColor) {
        this.backgroundColor = originalColor.withAlpha(100);
        //MessageHelper.sendMessage("호버: " + getId());
    }

    public void onHoverExit(TesmColor originalColor) {
        // 원래 색상으로 복원
        this.backgroundColor = originalColor.withAlpha(255);
        //MessageHelper.sendMessage("호버 아웃: " + getId());
    }

    private enum LineSide{
        UP,
        LEFT,
        RIGHT,
        DOWN
    }
}
