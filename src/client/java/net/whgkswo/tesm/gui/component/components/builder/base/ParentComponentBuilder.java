package net.whgkswo.tesm.gui.component.components.builder.base;

import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.component.GuiAxis;
import net.whgkswo.tesm.gui.component.ParentComponent;
import net.whgkswo.tesm.gui.component.components.style.GuiStyle;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;

// 원시타입 스타일 초기값은 여기에 명시
public abstract class ParentComponentBuilder<C extends ParentComponent<C, S>,
                                            B extends ParentComponentBuilder<C, B, S>,
                                            S extends GuiStyle>
        extends GuiComponentBuilder<C, B, S> {

    // ParentComponent 필드
    protected GuiAxis axis = GuiAxis.VERTICAL;
    protected HorizontalAlignment childrenHorizontalAlignment = HorizontalAlignment.NONE;
    protected VerticalAlignment childrenVerticalAlignment = VerticalAlignment.NONE;
    protected double horizontalGap = 0.0;
    protected double verticalGap = 0.0;

    public B axis(GuiAxis axis) {
        this.axis = axis;
        return self();
    }

    public B childrenHorizontalAlignment(HorizontalAlignment alignment) {
        this.childrenHorizontalAlignment = alignment;
        return self();
    }

    public B childrenVerticalAlignment(VerticalAlignment alignment) {
        this.childrenVerticalAlignment = alignment;
        return self();
    }

    public B horizontalGap(double horizontalGap) {
        this.horizontalGap = horizontalGap;
        return self();
    }

    public B verticalGap(double verticalGap) {
        this.verticalGap = verticalGap;
        return self();
    }
}
