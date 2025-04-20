package net.whgkswo.tesm.gui.component.elements;

import lombok.experimental.SuperBuilder;
import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.BaseTexture;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.GuiAxis;
import net.whgkswo.tesm.gui.component.bounds.LinearBound;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.elements.style.BoxStyle;

//@SuperBuilder
public class StraightLine extends GuiComponent<StraightLine, BoxStyle> {
    private GuiAxis direction;
    private int offset;
    private TesmColor color;
    private LinearBound bound;

    public StraightLine(TesmColor color, GuiAxis direction, LinearBound bound) {
        this(color, direction, bound, 0);
    }
    public StraightLine(TesmColor color, GuiAxis direction, LinearBound bound, int offset) {
        this.direction = direction;
        this.color = color;
        this.bound = bound;
        this.offset = offset;
    }

    @Override
    public void renderSelf(DrawContext context) {
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        if(direction == GuiAxis.HORIZONTAL){
            RenderingHelper.renderTextureWithColorFilter(context, BaseTexture.BASE_TEXTURE, (int)(screenWidth * bound.getxMarginRatio()), (int)(screenHeight * bound.getyMarginRatio()) + offset, (int)(screenWidth * bound.getLengthRatio()), bound.getThickness(), color);
        }else{
            RenderingHelper.renderTextureWithColorFilter(context, BaseTexture.BASE_TEXTURE, (int)(screenWidth * bound.getxMarginRatio()) + offset, (int)(screenHeight * bound.getyMarginRatio()), bound.getThickness(), (int)(screenHeight * bound.getLengthRatio()), color);
        }
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
