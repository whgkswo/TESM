package net.whgkswo.tesm.gui.component.components.builder;

import net.minecraft.text.Text;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.components.TextLabel;
import net.whgkswo.tesm.gui.component.components.builder.base.GuiComponentBuilder;
import net.whgkswo.tesm.gui.component.components.style.TextLabelStyle;

// 원시타입 스타일 초기값은 여기에 명시
public class TextLabelBuilder extends GuiComponentBuilder<TextLabel, TextLabelBuilder, TextLabelStyle> {
    // TextLabel 필드
    private Text text;
    private float fontScale = 1.0f;
    private boolean shadowed = false;
    private TesmColor backgroundColor;
    private TextLabel.SizeMode sizeMode;

    @Override
    public TextLabelBuilder self() {
        return this;
    }

    @Override
    public TextLabel build() {
        TextLabel textLabel = new TextLabel();

        // GuiComponent 필드
        textLabel.setId(this.id);
        textLabel.setShouldHide(this.shouldHide);
        textLabel.setSelfHorizontalAlignment(this.selfHorizontalAlignment);
        textLabel.setSelfVerticalAlignment(this.selfVerticalAlignment);
        textLabel.setParent(this.parent);
        textLabel.setStylePreset(this.stylePreset);
        textLabel.setRightMarginRatio(this.rightMarginRatio);
        textLabel.setBottomMarginRatio(this.bottomMarginRatio);
        textLabel.setTopMarginRatio(this.topMarginRatio);
        textLabel.setLeftMarginRatio(this.leftMarginRatio);
        // TextLabel 필드
        textLabel.setText(this.text);
        textLabel.setFontScale(this.fontScale);
        textLabel.setShadowed(this.shadowed);
        textLabel.setBackgroundColor(this.backgroundColor);
        textLabel.setSizeMode(this.sizeMode);

        return register(textLabel);
    }

    public TextLabelBuilder text(Text text) {
        this.text = text;
        return this;
    }

    public TextLabelBuilder fontScale(float fontScale) {
        this.fontScale = fontScale;
        return this;
    }

    public TextLabelBuilder shadowed(boolean shadowed) {
        this.shadowed = shadowed;
        return this;
    }

    public TextLabelBuilder backgroundColor(TesmColor backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public TextLabelBuilder sizeMode(TextLabel.SizeMode sizeMode){
        this.sizeMode = sizeMode;
        return this;
    }
}
