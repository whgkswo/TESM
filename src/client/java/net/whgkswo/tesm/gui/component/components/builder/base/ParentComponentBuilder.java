package net.whgkswo.tesm.gui.component.components.builder.base;

import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.component.GuiAxis;
import net.whgkswo.tesm.gui.component.ParentComponent;
import net.whgkswo.tesm.gui.component.components.style.GuiStyle;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;

public abstract class ParentComponentBuilder<C extends ParentComponent<C, S>,
                                            B extends ParentComponentBuilder<C, B, S>,
                                            S extends GuiStyle>
        extends GuiComponentBuilder<C, B, S> {

    // ParentComponent 필드
    protected GuiAxis axis = GuiAxis.VERTICAL;
    protected HorizontalAlignment childrenHorizontalAlignment = HorizontalAlignment.NONE;
    protected VerticalAlignment childrenVerticalAlignment = VerticalAlignment.NONE;

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
}
