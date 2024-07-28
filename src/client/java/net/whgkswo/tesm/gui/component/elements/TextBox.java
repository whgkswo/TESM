package net.whgkswo.tesm.gui.component.elements;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.colors.CustomColor;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.bounds.Boundary;
import net.whgkswo.tesm.gui.component.bounds.RectangularBound;

import java.util.ArrayList;
import java.util.List;

public class TextBox extends GuiComponent<RectangularBound> {
    private double widthRatio;
    private double heightRatio;
    private Boundary.BoundType boundType;
    private String content;
    private float fontScale;

    public TextBox(CustomColor color, RectangularBound bound, Boundary.BoundType boundType, String content, float fontScale) {
        super(color, bound);
        this.widthRatio = widthRatio;
        this.heightRatio = heightRatio;
        this.boundType = boundType;
        this.content = content;
        this.fontScale = fontScale;
    }
    @Override
    public void render(DrawContext context) {

    }

    private void render(DrawContext context, TextRenderer textRenderer, double widthRatio) {
        List<String> contentLines = splitContent(textRenderer, content, (int)(context.getScaledWindowWidth() * widthRatio));

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

