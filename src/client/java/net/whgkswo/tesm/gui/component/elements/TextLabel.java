package net.whgkswo.tesm.gui.component.elements;

import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.Alignment;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.CustomColor;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.bounds.RectangularBound;

public class TextLabel extends GuiComponent<RectangularBound> {
    private String content;
    private int textColor;
    private float textScale;
    private Alignment contentAlignment;
    private double textMarginRatio;

    public TextLabel(CustomColor color, String content, int textColor, float textScale, Alignment contentAlignment,
                     RectangularBound bound, double textMarginRatio) {
        super(color, bound);
        this.content = content;
        this.textColor = textColor;
        this.textScale = textScale;
        this.contentAlignment = contentAlignment;
        this.textMarginRatio = textMarginRatio;
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

    public double getTextMarginRatio() {
        return textMarginRatio;
    }
    @Override
    public void render(DrawContext context){
        RectangularBound bound = this.getRenderingBound();
        RenderingHelper.renderFilledBox(
                context,
                new CustomColor(170,166,133),
                bound.getxRatio(),
                bound.getyRatio(),
                bound.getWidthRatio(),
                bound.getHeightRatio()
        );
        double strRef = 0;
        switch (contentAlignment){
            case LEFT -> strRef = bound.getxRatio() + bound.getWidthRatio() * textMarginRatio;
            case CENTER -> strRef = bound.getxRatio() + bound.getWidthRatio() / 2;
            case RIGHT -> strRef = bound.getxRatio() + bound.getWidthRatio() - bound.getWidthRatio() * textMarginRatio;
        }
        double fixedYRatio = bound.getyRatio() + bound.getHeightRatio() / 2 - textScale * RenderingHelper.DEFAULT_TEXT_VERTICAL_WIDTH_RATIO;
        RenderingHelper.renderText(contentAlignment, context, textScale, content, strRef, fixedYRatio, textColor);
    }
}
