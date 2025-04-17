package net.whgkswo.tesm.gui.component.elements;

import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.ParentComponent;
import net.whgkswo.tesm.gui.component.bounds.RectangularBound;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;

public class FilledBox extends GuiComponent<FilledBox> {
    private RelativeBound bound;
    private TesmColor color;

    public FilledBox(ParentComponent parent, RelativeBound bound){
        this(parent, bound, TesmColor.TRANSPARENT);
    }

    public FilledBox(ParentComponent parent, RelativeBound bound, TesmColor color){
        super(parent);
        this.bound = bound;
        this.color = color;
    }

    @Override
    public void renderSelf(DrawContext context) {
        RenderingHelper.renderColoredBox(
                context,
                color,
                bound.getXMarginRatio(),
                bound.getYMarginRatio(),
                bound.getWidthRatio(),
                bound.getHeightRatio()
        );
    }

    public RelativeBound getBound(){
        return bound;
    }

    public TesmColor getColor(){
        return color;
    }
}
