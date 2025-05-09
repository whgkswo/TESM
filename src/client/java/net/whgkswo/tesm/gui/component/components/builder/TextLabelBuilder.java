package net.whgkswo.tesm.gui.component.components.builder;

import net.minecraft.text.Text;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.components.ParentComponent;
import net.whgkswo.tesm.gui.component.components.TextLabel;
import net.whgkswo.tesm.gui.component.components.builder.base.GuiComponentBuilder;
import net.whgkswo.tesm.gui.component.components.style.TextLabelStyle;

// 원시타입 스타일 초기값은 여기에 명시
public class TextLabelBuilder extends GuiComponentBuilder<TextLabel, TextLabelBuilder, TextLabelStyle> {
    // TextLabel 필드
    private Text text;
    private Float fontScale = 1.0f;
    private Boolean shadowed = false;
    private TesmColor backgroundColor;
    private TextLabel.SizeMode sizeMode;

    public TextLabelBuilder(ParentComponent<?, ?> parent){
        this.parent = parent;
    }

    @Override
    public TextLabelBuilder self() {
        return this;
    }

    @Override
    public TextLabel build() {
        TextLabel textLabel = new TextLabel();

        // GuiComponent 필드
        textLabel.setId(this.id);
        textLabel.setVisibility(this.isVisible);
        textLabel.setSelfHorizontalAlignment(this.selfHorizontalAlignment);
        textLabel.setSelfVerticalAlignment(this.selfVerticalAlignment);
        textLabel.setParentAndMotherScreen(this.parent);
        textLabel.setStylePreset(this.stylePreset);
        textLabel.setRightMarginRatio(this.rightMarginRatio);
        textLabel.setBottomMarginRatio(this.bottomMarginRatio);
        textLabel.setTopMarginRatio(this.topMarginRatio);
        textLabel.setLeftMarginRatio(this.leftMarginRatio);
        textLabel.onClick(this.clickHandler);
        // TextLabel 필드
        textLabel.setText(this.text);
        textLabel.setFontScale(this.fontScale);
        textLabel.setShadowed(this.shadowed);
        textLabel.setBackgroundColor(this.backgroundColor);
        textLabel.setSizeMode(this.sizeMode);

        // 순서 맨 뒤여야 하는 것들
        if(this.hoverHandler == null){
            textLabel.setHoverHandler(this.hoverType);
        }else{
            textLabel.setHoverHandler(this.hoverHandler);
        }
        return buildExtended(textLabel);
    }

    public TextLabelBuilder text(Text text) {
        this.text = text;
        return this;
    }

    public TextLabelBuilder text(String string) {
        this.text = Text.literal(string);
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
