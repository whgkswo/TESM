package net.whgkswo.tesm.gui.screen.component;

import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.gui.Alignment;

import java.util.HashMap;
import java.util.Map;

public class GuiComponent {
    String name;
    private Identifier background;
    private String content;
    private float textScale;
    private Alignment contentAlignment;
    private double xRatio;
    private double yRatio;
    private double widthRatio;
    private double heightRatio;
    private double xMarginRatio;

    public GuiComponent(String name, Identifier background, String content, float textScale, Alignment contentAlignment, double xRatio, double yRatio,
                        double widthRatio, double heightRatio, double xMarginRatio) {
        this.name = name;
        this.background = background;
        this.content = content;
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
}
