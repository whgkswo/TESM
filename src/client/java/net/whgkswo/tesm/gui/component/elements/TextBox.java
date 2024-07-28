package net.whgkswo.tesm.gui.component.elements;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.general.GeneralUtil;
import net.whgkswo.tesm.gui.Alignment;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.CustomColor;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.bounds.Boundary;
import net.whgkswo.tesm.gui.component.bounds.RectangularBound;

import java.util.ArrayList;
import java.util.List;

public class TextBox extends GuiComponent<RectangularBound> {
    TextRenderer textRenderer;
    private String content;
    private float fontScale;

    public TextBox(CustomColor color, RectangularBound bound, TextRenderer textRenderer, String content, float fontScale) {
        super(color, bound);
        this.textRenderer = textRenderer;
        this.content = content;
        this.fontScale = fontScale;
    }
    @Override
    public void render(DrawContext context) {
        render(context, textRenderer);
    }

    private void render(DrawContext context, TextRenderer textRenderer) {
        RectangularBound bound = this.getRenderingBound();
        int screenWidth = context.getScaledWindowWidth();

        List<String> contentLines = splitContent(textRenderer, content, (int)(screenWidth * bound.getWidthRatio() / fontScale));

        GeneralUtil.repeatWithIndex(contentLines.size(), i -> {
            RenderingHelper.renderText(Alignment.LEFT, context, fontScale, contentLines.get(i),
                    bound.getxRatio(), bound.getyRatio() + 0.04 * i ,
                    0xffffff);
        });
    }
    private List<String> splitContent(TextRenderer textRenderer, String content, int boundWidth){
        List<String> result = new ArrayList<>();
        String line = "";
        int i = 0;
        while (i < content.length()) {
            line = "";
            while (i < content.length() && textRenderer.getWidth(line + content.charAt(i)) <= boundWidth) {
                line += content.charAt(i);
                i++;
            }
            result.add(line);
        }
        return result;
    }
}

