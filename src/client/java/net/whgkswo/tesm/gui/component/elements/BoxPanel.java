package net.whgkswo.tesm.gui.component.elements;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;
import net.whgkswo.tesm.gui.GuiDirection;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiAxis;
import net.whgkswo.tesm.gui.component.ParentComponent;
import net.whgkswo.tesm.gui.component.bounds.LinearBound;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.elements.builder.BoxPanelBuilder;
import net.whgkswo.tesm.gui.component.elements.features.Hoverable;
import net.whgkswo.tesm.gui.component.elements.style.BoxStyle;

import java.util.HashMap;
import java.util.Map;

// 스타일 요소가 아닌 필드는 여기에 초기값 명시
public class BoxPanel extends ParentComponent<BoxPanel, BoxStyle> implements Hoverable {
    @Getter
    private final Map<GuiDirection, StraightLine> edgeLines = new HashMap<>();
    @Getter
    @Setter
    private RelativeBound bound;
    @Getter
    @Setter
    private TesmColor edgeColor;
    @Getter
    @Setter
    private int edgeThickness;
    @Setter
    private TesmColor backgroundColor;
    private boolean isHovered;

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
}
