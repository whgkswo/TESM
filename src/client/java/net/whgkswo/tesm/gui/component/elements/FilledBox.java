package net.whgkswo.tesm.gui.component.elements;

import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.ParentComponent;
import net.whgkswo.tesm.gui.component.bounds.RectangularBound;

public class FilledBox extends GuiComponent {
    private RectangularBound bound;
    private TesmColor color;

    public FilledBox(ParentComponent parent, RectangularBound bound){
        this(parent, bound, TesmColor.TRANSPARENT);
    }

    public FilledBox(ParentComponent parent, RectangularBound bound, TesmColor color){
        super(parent);
        this.bound = bound;
        this.color = color;
    }

    @Override
    public void renderSelf(DrawContext context) {
        RenderingHelper.renderColoredBox(
                context,
                color,
                bound.getxRatio(),
                bound.getyRatio(),
                bound.getWidthRatio(),
                bound.getHeightRatio()
        );
    }

    public RectangularBound getBound(){
        return bound;
    }

    public TesmColor getColor(){
        return color;
    }
}
