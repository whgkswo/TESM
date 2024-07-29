package net.whgkswo.tesm.gui.component.elements;

import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.BaseTexture;
import net.whgkswo.tesm.gui.colors.CustomColor;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.LineDirection;
import net.whgkswo.tesm.gui.component.bounds.LinearBound;

public class StraightLine extends GuiComponent<LinearBound> {
    private LineDirection direction;
    private int offset;

    public StraightLine(CustomColor color, LineDirection direction, LinearBound renderingBox) {
        super(color, renderingBox);
        this.direction = direction;
    }
    public StraightLine(CustomColor color, LineDirection direction, LinearBound renderingBox, int offset) {
        super(color, renderingBox);
        this.direction = direction;
        this.offset = offset;
    }

    @Override
    public void render(DrawContext context) {
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        LinearBound box = this.getRenderingBound();
        if(direction == LineDirection.HORIZONTAL){
            RenderingHelper.renderTextureWithColorFilter(context, BaseTexture.BASE_TEXTURE, (int)(screenWidth * box.getxRatio()), (int)(screenHeight * box.getyRatio()) + offset, (int)(screenWidth * box.getLengthRatio()), box.getThickness(), this.getColor());
        }else{
            RenderingHelper.renderTextureWithColorFilter(context, BaseTexture.BASE_TEXTURE, (int)(screenWidth * box.getxRatio()) + offset, (int)(screenHeight * box.getyRatio()), box.getThickness(), (int)(screenHeight * box.getLengthRatio()), this.getColor());
        }
    }
}
