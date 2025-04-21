package net.whgkswo.tesm.gui.component.components.builder;

import lombok.NoArgsConstructor;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.components.BoxPanel;
import net.whgkswo.tesm.gui.component.components.builder.base.ParentComponentBuilder;
import net.whgkswo.tesm.gui.component.components.style.BoxStyle;

@NoArgsConstructor
// 원시타입 스타일 초기값은 여기에 명시
public class BoxPanelBuilder extends ParentComponentBuilder<BoxPanel, BoxPanelBuilder, BoxStyle> {
    // BoxPanel 필드
    private RelativeBound bound;
    private TesmColor edgeColor;
    private int edgeThickness = 1;
    private TesmColor backgroundColor;

    @Override
    public BoxPanelBuilder self() {
        return this;
    }

    @Override
    public BoxPanel build() {
        BoxPanel boxPanel = new BoxPanel();

        // GuiComponent 필드
        boxPanel.setId(this.id);
        boxPanel.setShouldHide(this.shouldHide);
        boxPanel.setSelfHorizontalAlignment(this.selfHorizontalAlignment);
        boxPanel.setSelfVerticalAlignment(this.selfVerticalAlignment);
        boxPanel.setParent(this.parent);
        boxPanel.setStylePreset(this.stylePreset);
        boxPanel.setTopMarginRatio(this.topMarginRatio);
        boxPanel.setLeftMarginRatio(this.leftMarginRatio);
        boxPanel.setRightMarginRatio(this.rightMarginRatio);
        boxPanel.setBottomMarginRatio(this.bottomMarginRatio);
        // ParentComponent 필드
        boxPanel.setAxis(this.axis);
        boxPanel.setChildrenHorizontalAlignment(this.childrenHorizontalAlignment);
        boxPanel.setChildrenVerticalAlignment(this.childrenVerticalAlignment);
        // BoxPanel 필드
        boxPanel.setBound(this.bound);
        boxPanel.setEdgeColor(this.edgeColor);
        boxPanel.setEdgeThickness(this.edgeThickness);
        boxPanel.setBackgroundColor(this.backgroundColor);

        return register(boxPanel);
    }

    public BoxPanelBuilder bound(RelativeBound bound){
        this.bound = bound;
        return self();
    }

    public BoxPanelBuilder edgeColor(TesmColor edgeColor){
        this.edgeColor = edgeColor;
        return self();
    }

    public BoxPanelBuilder edgeThickness(int edgeThickness){
        this.edgeThickness = edgeThickness;
        return self();
    }

    public BoxPanelBuilder backgroundColor(TesmColor backgroundColor){
        this.backgroundColor = backgroundColor;
        return self();
    }
}
