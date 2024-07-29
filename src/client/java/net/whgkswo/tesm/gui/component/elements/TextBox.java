package net.whgkswo.tesm.gui.component.elements;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.Alignment;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.CustomColor;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.bounds.RectangularBound;

import java.util.ArrayList;
import java.util.List;

public class TextBox extends GuiComponent<RectangularBound> {
    private static final double LINE_GAP_RATIO = 1.2;
    TextRenderer textRenderer;
    private String content;
    private float fontScale;
    private double xMarginRatio;
    private double yMarginRatio;
    private Alignment textAlignment;

    public TextBox(CustomColor color, RectangularBound bound, TextRenderer textRenderer, String content, float fontScale,
                   double xMarginRatio, double yMarginRatio, Alignment textAlignment) {
        super(color, bound);
        this.textRenderer = textRenderer;
        this.content = content;
        this.fontScale = fontScale;
        this.xMarginRatio = xMarginRatio;
        this.yMarginRatio = yMarginRatio;
        this.textAlignment = textAlignment;
    }
    @Override
    public void render(DrawContext context) {
        render(context, textRenderer);
    }

    private void render(DrawContext context, TextRenderer textRenderer) {
        RectangularBound bound = this.getRenderingBound();
        int screenWidth = context.getScaledWindowWidth();

        List<String> contentLines = splitContent(textRenderer, content, (int)(screenWidth * (1 - 2 * xMarginRatio) * bound.getWidthRatio() / fontScale));
        double lineVerticalWidth = (double) textRenderer.fontHeight * fontScale / context.getScaledWindowHeight();

        /*GeneralUtil.repeatWithIndex(contentLines.size(), i -> {
            RenderingHelper.renderTextInBox(textAlignment, context, fontScale, contentLines.get(i),
                    bound.getxRatio() + xMarginRatio * bound.getWidthRatio(), bound.getyRatio() + yMarginRatio + lineVerticalWidth * LINE_GAP_RATIO * i ,
                    (1 - 2 * xMarginRatio) * bound.getWidthRatio(), 0xffffff);
        });*/
        double yRatio = bound.getyRatio() + yMarginRatio;
        double lineGap = lineVerticalWidth * LINE_GAP_RATIO;
        int i = 0;
        while(yRatio < bound.getyRatio() + bound.getHeightRatio() - yMarginRatio/* - lineGap*/){
            RenderingHelper.renderTextInBox(textAlignment, context, fontScale, contentLines.get(i),
                    bound.getxRatio() + xMarginRatio * bound.getWidthRatio(), bound.getyRatio() + yMarginRatio + lineGap * i ,
                    (1 - 2 * xMarginRatio) * bound.getWidthRatio(), 0xffffff);
            i++;
            yRatio += lineGap;
        }
    }
    private List<String> splitContent(TextRenderer textRenderer, String content, int availableWidth) {
        List<String> result = new ArrayList<>();
        StringBuilder line = new StringBuilder();

        for (int i = 0; i < content.length(); i++) {
            char currentChar = content.charAt(i);

            if (currentChar == '\n') {
                // 개행 문자를 만나면 현재 라인을 추가
                result.add(line.toString());
                line = new StringBuilder();
                continue;
            }
            if (textRenderer.getWidth(line.toString() + currentChar) <= availableWidth) {
                line.append(currentChar);
            } else {
                result.add(line.toString());
                line = new StringBuilder().append(currentChar);
            }
        }

        // 마지막 라인 추가
        if (!line.isEmpty()) {
            result.add(line.toString());
        }

        return result;
    }
}

