package net.whgkswo.tesm.gui.component.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.ParentComponent;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;

@SuperBuilder
public class TextLabel extends GuiComponent<TextLabel> {
    @Getter
    private Text content;
    @Getter
    @Builder.Default
    private float fontScale = 1.0f;
    @Getter
    @Builder.Default
    private HorizontalAlignment contentAlignment = HorizontalAlignment.LEFT;
    @Builder.Default
    private double textMarginRatio = 0.0;
    private Box background;

    public TextLabel(Text content, Box background, float fontScale, HorizontalAlignment selfAlignment,
                     double textMarginRatio){
        this(null, content, background, fontScale, selfAlignment, textMarginRatio);
    }

    public TextLabel(ParentComponent parent, Text content, Box background, float fontScale, HorizontalAlignment contentAlignment,
                     double textMarginRatio) {
        super(parent);
        this.content = content;
        this.fontScale = fontScale;
        this.contentAlignment = contentAlignment;
        this.textMarginRatio = textMarginRatio;
        this.background = background;
    }

    public double getTextMarginRatio() {
        return textMarginRatio;
    }

    @Override
    public void renderSelf(DrawContext context){
        RelativeBound absoluteBound = getScreenRelativeBoundWithUpdate();

        TesmColor backgroundColor = background.getBackgroundColor();
        if(!backgroundColor.equals(TesmColor.TRANSPARENT)){
            RenderingHelper.renderColoredBox(
                    context,
                    backgroundColor,
                    absoluteBound.getXMarginRatio(),
                    absoluteBound.getYMarginRatio(),
                    absoluteBound.getWidthRatio(),
                    absoluteBound.getHeightRatio()
            );
        }
        double strRef = 0;
        switch (contentAlignment){
            case LEFT -> strRef = absoluteBound.getXMarginRatio() + absoluteBound.getWidthRatio() * textMarginRatio;
            case CENTER -> strRef = absoluteBound.getXMarginRatio() + absoluteBound.getWidthRatio() / 2;
            case RIGHT -> strRef = absoluteBound.getXMarginRatio() + absoluteBound.getWidthRatio() - absoluteBound.getWidthRatio() * textMarginRatio;
        }
        double fixedYRatio = absoluteBound.getYMarginRatio() + absoluteBound.getHeightRatio() / 2 - fontScale * RenderingHelper.DEFAULT_TEXT_VERTICAL_WIDTH_RATIO;
        RenderingHelper.renderText(contentAlignment, context, fontScale, content, strRef, fixedYRatio);
    }

    @Override
    public RelativeBound getBound() {
        return background.getBound();
    }
}
