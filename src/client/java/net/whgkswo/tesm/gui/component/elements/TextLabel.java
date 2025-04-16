package net.whgkswo.tesm.gui.component.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.ParentComponent;
import net.whgkswo.tesm.gui.component.bounds.RectangularBound;

public class TextLabel extends GuiComponent {
    private Text content;
    private float fontScale;
    private HorizontalAlignment contentAlignment;
    private double textMarginRatio;
    private EdgedBox background;

    public TextLabel(Text content, EdgedBox background, float fontScale, HorizontalAlignment contentAlignment,
                     double textMarginRatio){
        this(null, content, background, fontScale, contentAlignment, textMarginRatio);
    }

    public TextLabel(ParentComponent parent, Text content, EdgedBox background, float fontScale, HorizontalAlignment contentAlignment,
                     double textMarginRatio) {
        super(parent);
        this.content = content;
        this.fontScale = fontScale;
        this.contentAlignment = contentAlignment;
        this.textMarginRatio = textMarginRatio;
        this.background = background;
    }

    public Text getContent() {
        return content;
    }

    public float getFontScale() {
        return fontScale;
    }

    public HorizontalAlignment getContentAlignment() {
        return contentAlignment;
    }

    public double getTextMarginRatio() {
        return textMarginRatio;
    }

    @Override
    public void renderSelf(DrawContext context){
        ParentComponent parent = getParent();
        RectangularBound parentBound = null;
        if(parent != null) parentBound = (RectangularBound) parent.getBound();

        RectangularBound absoluteBound = RenderingHelper.getAbsoluteBound(parentBound, background.getBound());

        TesmColor backgroundColor = background.getBackgroundColor();
        if(!backgroundColor.equals(TesmColor.TRANSPARENT)){
            RenderingHelper.renderColoredBox(
                    context,
                    backgroundColor,
                    absoluteBound.getxRatio(),
                    absoluteBound.getyRatio(),
                    absoluteBound.getWidthRatio(),
                    absoluteBound.getHeightRatio()
            );
        }
        double strRef = 0;
        switch (contentAlignment){
            case LEFT -> strRef = absoluteBound.getxRatio() + absoluteBound.getWidthRatio() * textMarginRatio;
            case CENTER -> strRef = absoluteBound.getxRatio() + absoluteBound.getWidthRatio() / 2;
            case RIGHT -> strRef = absoluteBound.getxRatio() + absoluteBound.getWidthRatio() - absoluteBound.getWidthRatio() * textMarginRatio;
        }
        double fixedYRatio = absoluteBound.getyRatio() + absoluteBound.getHeightRatio() / 2 - fontScale * RenderingHelper.DEFAULT_TEXT_VERTICAL_WIDTH_RATIO;
        RenderingHelper.renderText(contentAlignment, context, fontScale, content, strRef, fixedYRatio);
    }
}
