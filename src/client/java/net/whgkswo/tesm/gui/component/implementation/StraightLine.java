package net.whgkswo.tesm.gui.component.implementation;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.gui.component.Box;
import net.whgkswo.tesm.gui.component.SingleColorTexture;
import net.whgkswo.tesm.gui.component.LineDirection;

public class StraightLine extends SingleColorTexture {
    private int thickness;
    private int offset;
    private LineDirection direction;

    public StraightLine(LineDirection direction, Identifier texture, Box renderingBox, int thickness) {
        super(texture, renderingBox);
        this.direction = direction;
        this.thickness = thickness;
    }
    public StraightLine(LineDirection direction, Identifier texture, Box renderingBox, int thickness, int offset) {
        super(texture, renderingBox);
        this.direction = direction;
        this.thickness = thickness;
        this.offset = offset;
    }

    @Override
    public void render(DrawContext context) {
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        Box box = super.getRenderingBox();
        if(direction == LineDirection.HORIZONTAL){
            context.drawTexture(super.getTexture(), (int)(screenWidth * box.getxRatio()), (int)(screenHeight * box.getyRatio()) + offset, 0,0,(int)(screenWidth * box.getWidthRatio()), thickness);
        }else{
            context.drawTexture(super.getTexture(), (int)(screenWidth * box.getxRatio()) + offset, (int)(screenHeight * box.getyRatio()), 0,0, thickness,(int)(screenHeight * box.getHeightRatio()));
        }
    }
}
