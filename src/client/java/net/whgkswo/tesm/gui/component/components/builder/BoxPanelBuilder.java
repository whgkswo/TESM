package net.whgkswo.tesm.gui.component.components.builder;

import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.components.ParentComponent;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.components.BoxPanel;
import net.whgkswo.tesm.gui.component.components.builder.base.ParentComponentBuilder;
import net.whgkswo.tesm.gui.component.components.features.base.ScrollHandler;
import net.whgkswo.tesm.gui.component.components.style.BoxStyle;
import net.whgkswo.tesm.gui.component.components.style.EdgeVisibility;

// 원시타입 스타일 초기값은 여기에 명시
public class BoxPanelBuilder extends ParentComponentBuilder<BoxPanel, BoxPanelBuilder, BoxStyle> {
    // BoxPanel 필드
    private RelativeBound bound;
    private TesmColor edgeColor;
    private int edgeThickness = 1;
    private TesmColor backgroundColor;
    private boolean isScrollable;
    private EdgeVisibility edgeVisibilities;

    public BoxPanelBuilder(ParentComponent<?, ?> parent){
        this.parent = parent;
    }

    @Override
    public BoxPanelBuilder self() {
        return this;
    }

    @Override
    public BoxPanel build() {
        BoxPanel boxPanel = new BoxPanel();
        // 스타일 프리셋 선적용
        boxPanel.setStylePreset(this.stylePreset);
        boxPanel.initializeStyle();
        // GuiComponent 필드
        boxPanel.setId(this.id);
        boxPanel.setVisibility(this.isVisible);
        boxPanel.setSelfHorizontalAlignment(this.selfHorizontalAlignment);
        boxPanel.setSelfVerticalAlignment(this.selfVerticalAlignment);
        boxPanel.setParent(this.parent);
        boxPanel.setTopMarginRatio(this.topMarginRatio);
        boxPanel.setLeftMarginRatio(this.leftMarginRatio);
        boxPanel.setRightMarginRatio(this.rightMarginRatio);
        boxPanel.setBottomMarginRatio(this.bottomMarginRatio);
        boxPanel.onClick(this.clickHandler);
        // ParentComponent 필드
        boxPanel.setAxis(this.axis);
        boxPanel.setChildrenHorizontalAlignment(this.childrenHorizontalAlignment);
        boxPanel.setChildrenVerticalAlignment(this.childrenVerticalAlignment);
        boxPanel.setHorizontalGap(this.horizontalGap);
        boxPanel.setVerticalGap(this.verticalGap);
        // BoxPanel 필드
        boxPanel.setBound(this.bound);
        boxPanel.setEdgeColor(this.edgeColor);
        boxPanel.setEdgeThickness(this.edgeThickness);
        boxPanel.setBackgroundColor(this.backgroundColor);
        boxPanel.setEdgeVisibilities(this.edgeVisibilities);

        // 순서 맨 뒤여야 하는 것들
        if(this.hoverHandler == null){
            boxPanel.setHoverHandler(this.hoverType);
        }else{
            boxPanel.setHoverHandler(this.hoverHandler);
        }

        if(isScrollable) boxPanel.setScrollHandler(new ScrollHandler(boxPanel));

        return buildExtended(boxPanel);
    }

    public BoxPanelBuilder bound(RelativeBound bound){
        this.bound = bound;
        return self();
    }

    public BoxPanelBuilder bound(double widthRatio, double heightRatio){
        this.bound = new RelativeBound(widthRatio, heightRatio);
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

    public BoxPanelBuilder isScrollable(boolean isScrollable){
        this.isScrollable = isScrollable;
        return self();
    }

    public BoxPanelBuilder edgeVisibility(EdgeVisibility edgeVisibilities){
        this.edgeVisibilities = edgeVisibilities;
        return self();
    }
}
