package net.whgkswo.tesm.gui.component.elements;

import lombok.experimental.SuperBuilder;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.ParentComponent;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.elements.style.BoxStyle;

import java.util.ArrayList;
import java.util.List;

//@SuperBuilder
public class TextBox extends GuiComponent<TextBox, BoxStyle> {
    private static final double LINE_GAP_RATIO = 1.2;
    private TextRenderer textRenderer;
    private Text content;
    private float fontScale;
    private double xMarginRatio;
    private double yMarginRatio;
    private HorizontalAlignment textAlignment;
    private BoxPanel boxPanel;

    public TextBox(ParentComponent parent, BoxPanel boxPanel, TextRenderer textRenderer, Text content, float fontScale,
                   double xMarginRatio, double yMarginRatio, HorizontalAlignment textAlignment) {
        super(parent);
        this.textRenderer = textRenderer;
        this.content = content;
        this.fontScale = fontScale;
        this.xMarginRatio = xMarginRatio;
        this.yMarginRatio = yMarginRatio;
        this.textAlignment = textAlignment;
    }

    public float getFontScale() {
        return fontScale;
    }

    public Text getContent() {
        return content;
    }

    @Override
    public void renderSelf(DrawContext context) {
        render(context, textRenderer);
    }

    @Override
    public RelativeBound getBound() {
        return null;
    }

    @Override
    protected Class<BoxStyle> getStyleType() {
        return BoxStyle.class;
    }

    private void render(DrawContext context, TextRenderer textRenderer) {
        RelativeBound bound = boxPanel.getBound();
        int screenWidth = context.getScaledWindowWidth();

        List<String> contentLines = splitContent(textRenderer, content.getString(), (int)(screenWidth * (1 - 2 * xMarginRatio) * bound.getWidthRatio() / fontScale));
        double lineVerticalWidth = (double) textRenderer.fontHeight * fontScale / context.getScaledWindowHeight();

        double yRatio = bound.getYOffsetRatio() + yMarginRatio;
        double lineGap = lineVerticalWidth * LINE_GAP_RATIO;
        int i = 0;
        while(i < contentLines.size() && yRatio < bound.getYOffsetRatio() + bound.getHeightRatio() - yMarginRatio/* - lineGap*/){
            RenderingHelper.renderTextInBox(textAlignment, context, fontScale, contentLines.get(i),
                    bound.getXOffsetRatio() + xMarginRatio * bound.getWidthRatio(), bound.getYOffsetRatio() + yMarginRatio + lineGap * i ,
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

