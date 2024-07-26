package net.whgkswo.tesm.gui.screen.component.implementation;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.gui.Alignment;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.screen.component.GuiComponent;

public class TextBoxWIthBackground extends GuiComponent {
    private Identifier background;
    private String content;
    private int textColor;
    private float textScale;
    private Alignment contentAlignment;
    private double xRatio;
    private double yRatio;
    private double widthRatio;
    private double heightRatio;
    private double xMarginRatio;

    public TextBoxWIthBackground(Identifier background, String content, int textColor, float textScale, Alignment contentAlignment,
                                 double xRatio, double yRatio, double widthRatio, double heightRatio, double xMarginRatio) {
        this.background = background;
        this.content = content;
        this.textColor = textColor;
        this.textScale = textScale;
        this.contentAlignment = contentAlignment;
        this.xRatio = xRatio;
        this.yRatio = yRatio;
        this.widthRatio = widthRatio;
        this.heightRatio = heightRatio;
        this.xMarginRatio = xMarginRatio;
    }

    public Identifier getBackground() {
        return background;
    }

    public String getContent() {
        return content;
    }

    public int getTextColor() {
        return textColor;
    }

    public float getTextScale() {
        return textScale;
    }

    public Alignment getContentAlignment() {
        return contentAlignment;
    }

    public double getxRatio() {
        return xRatio;
    }

    public double getyRatio() {
        return yRatio;
    }

    public double getWidthRatio() {
        return widthRatio;
    }

    public double getHeightRatio() {
        return heightRatio;
    }

    public double getxMarginRatio() {
        return xMarginRatio;
    }
    @Override
    public void render(DrawContext context){
        RenderingHelper.renderFilledBox(
                context,
                background,
                xRatio,
                yRatio,
                widthRatio,
                heightRatio
        );
        double strRef = 0;
        switch (contentAlignment){
            case LEFT -> strRef = xRatio + widthRatio * xMarginRatio;
            case CENTER -> strRef = xRatio + widthRatio / 2;
            case RIGHT -> strRef = xRatio + widthRatio - widthRatio * xMarginRatio;
        }
        double fixedYRatio = yRatio + heightRatio / 2 - textScale * RenderingHelper.DEFAULT_TEXT_VERTICAL_WIDTH_RATIO;
        RenderingHelper.renderText(contentAlignment, context, textScale, content, strRef, fixedYRatio, textColor);
    }
}
