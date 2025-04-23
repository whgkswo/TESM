package net.whgkswo.tesm.gui.component.components;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.GuiDirection;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiAxis;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.ParentComponent;
import net.whgkswo.tesm.gui.component.bounds.LinearBound;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.components.builder.BoxPanelBuilder;
import net.whgkswo.tesm.gui.component.components.features.base.HasBackground;
import net.whgkswo.tesm.gui.component.components.features.base.ScrollHandler;
import net.whgkswo.tesm.gui.component.components.features.base.Scrollable;
import net.whgkswo.tesm.gui.component.components.style.BoxStyle;
import net.whgkswo.tesm.gui.component.components.style.EdgeVisibility;
import net.whgkswo.tesm.gui.exceptions.GuiException;
import net.whgkswo.tesm.gui.screen.base.TesmScreen;

import java.util.HashMap;
import java.util.Map;

// 스타일 요소가 아닌 필드는 여기에 초기값 명시
@Getter
public class BoxPanel extends ParentComponent<BoxPanel, BoxStyle> implements HasBackground, Scrollable {
    private final Map<GuiDirection, StraightLine> edgeLines = new HashMap<>();
    private RelativeBound bound;
    @Setter
    private TesmColor edgeColor;
    @Setter
    private EdgeVisibility edgeVisibilities;
    @Setter
    private int edgeThickness;
    @Setter
    private TesmColor backgroundColor;
    @Setter
    private ScrollHandler scrollHandler;

    public BoxPanel(){
        super();
    }

    public static BoxPanelBuilder builder(ParentComponent<?, ?> parent){
        return new BoxPanelBuilder(parent);
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
    }

    protected void renderEdges(DrawContext context){
        // 모서리 없으면 생성
        generateEdgeLines();
        // 모서리 출력
        if(edgeColor.equals(TesmColor.TRANSPARENT)) return;
        edgeLines.forEach((lineSide, straightLine) -> {
            if(edgeVisibilities.isVisible(lineSide)){
                straightLine.renderSelf(context);
            }
        });
    }

    @Override
    public void render(DrawContext context){
        // 자신 렌더링
        renderSelfWithScissor(context);
        // 자식 렌더링
        for (GuiComponent<?, ?> child : getChildren()){
            child.tryRender(context);
        }
        // 자식들 위에 테두리 렌더링
        renderEdges(context);
    }

    public void generateEdgeLines(){
        if(!edgeLines.isEmpty()) return;
        // 모서리 생성
        RelativeBound bound = getAbsoluteBound();
        double xRatio = bound.getXOffsetRatio();
        double yRatio = bound.getYOffsetRatio();
        double widthRatio = bound.getWidthRatio();
        double heightRatio = bound.getHeightRatio();

        edgeLines.clear();
        addEdgeLine(GuiDirection.TOP, new LinearBound(xRatio, yRatio, GuiAxis.HORIZONTAL, widthRatio, edgeThickness));
        addEdgeLine(GuiDirection.BOTTOM, new LinearBound(xRatio, yRatio + heightRatio, GuiAxis.HORIZONTAL, widthRatio, edgeThickness, 0, -1 * edgeThickness));
        addEdgeLine(GuiDirection.LEFT, new LinearBound(xRatio, yRatio, GuiAxis.VERTICAL, heightRatio, edgeThickness));
        addEdgeLine(GuiDirection.RIGHT, new LinearBound(xRatio + widthRatio, yRatio, GuiAxis.VERTICAL, heightRatio, edgeThickness, -1 * edgeThickness, 0));
    }

    @Override
    public RelativeBound getBound(){
        return bound;
    }

    @Override
    protected Class<BoxStyle> getStyleType() {
        return BoxStyle.class;
    }

    private void addEdgeLine(GuiDirection direction, LinearBound bound){
        edgeLines.put(direction, new StraightLine(this, edgeColor, bound));
    }

    public void setBound(RelativeBound bound){
        if(TesmScreen.ROOT_ID.equals(getId()) && this.bound != null){
            new GuiException(getMotherScreen(), "루트 컴포넌트는 크기를 조정할 수 없습니다.").handle();
        }
        this.bound = bound;
    }
}
