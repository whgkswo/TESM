package net.whgkswo.tesm.gui.component.components.builder;

import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.bounds.LinearBound;
import net.whgkswo.tesm.gui.component.components.ParentComponent;
import net.whgkswo.tesm.gui.component.components.SLine;
import net.whgkswo.tesm.gui.component.components.builder.base.GuiComponentBuilder;
import net.whgkswo.tesm.gui.component.components.style.SLineStyle;

// 원시타입 스타일 초기값은 여기에 명시
public class SLineBuilder extends GuiComponentBuilder<SLine, SLineBuilder, SLineStyle> {
    private TesmColor color;
    private LinearBound bound;

    public SLineBuilder(ParentComponent<?,?> parent){
        this.parent = parent;
    }

    @Override
    public SLineBuilder self() {
        return this;
    }

    @Override
    public SLine build() {
        SLine sLine = new SLine();
        // GuiComponent 필드
        sLine.setId(this.id);
        sLine.setVisibility(this.isVisible);
        sLine.setSelfHorizontalAlignment(this.selfHorizontalAlignment);
        sLine.setSelfVerticalAlignment(this.selfVerticalAlignment);
        sLine.setParentAndMotherScreen(this.parent);
        sLine.setStylePreset(this.stylePreset);
        sLine.setTopMarginRatio(this.topMarginRatio);
        sLine.setLeftMarginRatio(this.leftMarginRatio);
        sLine.setRightMarginRatio(this.rightMarginRatio);
        sLine.setBottomMarginRatio(this.bottomMarginRatio);
        sLine.onClick(this.clickHandler);
        // SLine 필드
        sLine.setColor(this.color);
        sLine.setBound(this.bound);

        return buildExtended(sLine);
    }

    public SLineBuilder color(TesmColor color){
        this.color = color;
        return self();
    }

    public SLineBuilder bound(LinearBound bound){
        this.bound = bound;
        return self();
    }
}
