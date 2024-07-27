package net.whgkswo.tesm.gui.component.implementation;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.Colors;
import net.whgkswo.tesm.gui.colors.CustomColor;
import net.whgkswo.tesm.gui.component.box.Box;
import net.whgkswo.tesm.gui.component.SingleColorTexture;
import net.whgkswo.tesm.gui.component.LineDirection;
import net.whgkswo.tesm.gui.component.box.LinearBox;

import java.awt.*;

public class StraightLine extends SingleColorTexture<LinearBox> {
    private LineDirection direction;
    private int offset;

    public StraightLine(CustomColor color, LineDirection direction, LinearBox renderingBox) {
        super(color, renderingBox);
        this.direction = direction;
    }
    public StraightLine(CustomColor color, LineDirection direction, LinearBox renderingBox, int offset) {
        super(color, renderingBox);
        this.direction = direction;
        this.offset = offset;
    }

    @Override
    public void render(DrawContext context) {
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        LinearBox box = this.getRenderingBox();
        if(direction == LineDirection.HORIZONTAL){
            //context.drawTexture(this.getTexture(), (int)(screenWidth * box.getxRatio()), (int)(screenHeight * box.getyRatio()) + offset, 0,0,(int)(screenWidth * box.getLengthRatio()), box.getThickness());
            RenderingHelper.renderTextureWithColorFilter(context, Colors.BASE_TEXTURE, (int)(screenWidth * box.getxRatio()), (int)(screenHeight * box.getyRatio()) + offset, (int)(screenWidth * box.getLengthRatio()), box.getThickness(), this.getColor());
        }else{
            //context.drawTexture(this.getTexture(), (int)(screenWidth * box.getxRatio()) + offset, (int)(screenHeight * box.getyRatio()), 0,0,box.getThickness(), (int)(screenHeight * box.getLengthRatio()));
            RenderingHelper.renderTextureWithColorFilter(context, Colors.BASE_TEXTURE, (int)(screenWidth * box.getxRatio()) + offset, (int)(screenHeight * box.getyRatio()), box.getThickness(), (int)(screenHeight * box.getLengthRatio()), this.getColor());
        }
    }
}
