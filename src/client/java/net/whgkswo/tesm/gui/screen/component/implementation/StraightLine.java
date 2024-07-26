package net.whgkswo.tesm.gui.screen.component.implementation;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.gui.screen.component.LineDirection;
import net.whgkswo.tesm.gui.screen.component.SingleColorTexture;

public class StraightLine extends SingleColorTexture {
    private int thickness;
    private int offset;
    private LineDirection direction;

    public StraightLine(LineDirection direction, Identifier texture, double xRatio, double yRatio,
                        double widthRatio, double heightRatio, int thickness) {
        super(texture, xRatio, yRatio, widthRatio, heightRatio);
        this.direction = direction;
        this.thickness = thickness;
    }
    public StraightLine(LineDirection direction, Identifier texture, double xRatio, double yRatio,
                        double widthRatio, double heightRatio, int thickness, int offset) {
        super(texture, xRatio, yRatio, widthRatio, heightRatio);
        this.direction = direction;
        this.thickness = thickness;
        this.offset = offset;
    }

    @Override
    public void render(DrawContext context) {
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        if(direction == LineDirection.HORIZONTAL){
            context.drawTexture(super.getTexture(), (int)(screenWidth * super.getxRatio()), (int)(screenHeight * super.getyRatio()) + offset, 0,0,(int)(screenWidth * super.getWidthRatio()), thickness);
        }else{
            context.drawTexture(super.getTexture(), (int)(screenWidth * super.getxRatio()) + offset, (int)(screenHeight * super.getyRatio()), 0,0, thickness,(int)(screenHeight * super.getHeightRatio()));
        }
    }
}
