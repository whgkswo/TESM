package net.whgkswo.tesm.gui.component.components;

import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.bounds.LinearBound;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.components.style.BoxStyle;

//@SuperBuilder
public class StraightLine extends GuiComponent<StraightLine, BoxStyle> {
    private int offset;
    private TesmColor color;
    private LinearBound bound;

    public StraightLine(TesmColor color, LinearBound bound) {
        this(color, bound, 0);
    }
    public StraightLine(TesmColor color, LinearBound bound, int offset) {
        this.color = color;
        this.bound = bound;
        this.offset = offset;
    }

    @Override
    public void renderSelf(DrawContext context) {
        /*RelativeBound parentBound = getParent().getAbsoluteBound();
        RenderingHelper.drawLine(context, color, absoluteBound);*/
    }

    @Override
    public RelativeBound getBound() {
        return null;
    }

    @Override
    protected Class<BoxStyle> getStyleType() {
        return BoxStyle.class;
    }
}
