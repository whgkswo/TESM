package net.whgkswo.tesm.gui.component.elements;

import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.BaseTexture;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.GuiDirection;
import net.whgkswo.tesm.gui.component.bounds.LinearBound;
import net.whgkswo.tesm.gui.component.bounds.RectangularBound;

public class StraightLine extends GuiComponent {
    private GuiDirection direction;
    private int offset;
    private TesmColor color;
    private LinearBound bound;

    public StraightLine(TesmColor color, GuiDirection direction, LinearBound bound) {
        new StraightLine(color, direction, bound, 0);
    }
    public StraightLine(TesmColor color, GuiDirection direction, LinearBound bound, int offset) {
        this.direction = direction;
        this.color = color;
        this.bound = bound;
        this.offset = offset;
    }

    @Override
    public void renderSelf(DrawContext context) {
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        if(direction == GuiDirection.HORIZONTAL){
            RenderingHelper.renderTextureWithColorFilter(context, BaseTexture.BASE_TEXTURE, (int)(screenWidth * bound.getxRatio()), (int)(screenHeight * bound.getyRatio()) + offset, (int)(screenWidth * bound.getLengthRatio()), bound.getThickness(), color);
        }else{
            RenderingHelper.renderTextureWithColorFilter(context, BaseTexture.BASE_TEXTURE, (int)(screenWidth * bound.getxRatio()) + offset, (int)(screenHeight * bound.getyRatio()), bound.getThickness(), (int)(screenHeight * bound.getLengthRatio()), color);
        }
    }
}
