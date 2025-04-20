package net.whgkswo.tesm.gui.component.elements.builder;

import net.minecraft.text.Text;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.elements.TextLabel;
import net.whgkswo.tesm.gui.component.elements.builder.base.GuiComponentBuilder;
import net.whgkswo.tesm.gui.component.elements.style.TextLabelStyle;

// 원시타입 스타일 초기값은 여기에 명시
public class TextLabelBuilder extends GuiComponentBuilder<TextLabel, TextLabelBuilder, TextLabelStyle> {
    // TextLabel 필드
    private Text text;
    private float fontScale = 1.0f;
    private RelativeBound bound;
    private TesmColor backgroundColor;

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

        // TextLabel 필드
        textLabel.setText(this.text);
        textLabel.setFontScale(this.fontScale);
        textLabel.setBound(this.bound);
        textLabel.setBackgroundColor(this.backgroundColor);

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

    public TextLabelBuilder bound(RelativeBound bound) {
        this.bound = bound;
        return this;
    }

    public TextLabelBuilder backgroundColor(TesmColor backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }
}
