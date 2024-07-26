package net.whgkswo.tesm.gui.component.implementation;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.gui.component.box.Box;
import net.whgkswo.tesm.gui.component.SingleColorTexture;
import net.whgkswo.tesm.gui.component.LineDirection;
import net.whgkswo.tesm.gui.component.box.LinearBox;

public class StraightLine extends SingleColorTexture<LinearBox> {
    private LineDirection direction;
    private int offset;

    public StraightLine(LineDirection direction, Identifier texture, LinearBox renderingBox) {
        super(texture, renderingBox);
        this.direction = direction;
    }
    public StraightLine(LineDirection direction, Identifier texture, LinearBox renderingBox, int offset) {
        super(texture, renderingBox);
        this.direction = direction;
        this.offset = offset;
    }

    @Override
    public void render(DrawContext context) {
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        LinearBox box = super.getRenderingBox();

        if(direction == LineDirection.HORIZONTAL){
            context.drawTexture(super.getTexture(), (int)(screenWidth * box.getxRatio()), (int)(screenHeight * box.getyRatio()) + offset, 0,0,(int)(screenWidth * box.getLengthRatio()), box.getThickness());
        }else{
            context.drawTexture(super.getTexture(), (int)(screenWidth * box.getxRatio()) + offset, (int)(screenHeight * box.getyRatio()), 0,0,box.getThickness(), (int)(screenHeight * box.getLengthRatio()));
        }
    }
}
