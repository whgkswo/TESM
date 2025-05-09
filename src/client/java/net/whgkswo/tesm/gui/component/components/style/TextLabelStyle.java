package net.whgkswo.tesm.gui.component.components.style;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.components.TextLabel;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;

@Builder
@AllArgsConstructor
@NoArgsConstructor
// 참조타입 초기값은 디폴트 스타일로 처리
public class TextLabelStyle implements GuiStyle, DefaultStyleProvider<TextLabelStyle>{
    // GuiComponent
    private Boolean isVisible;
    private HorizontalAlignment selfHorizontalAlignment;
    private VerticalAlignment selfVerticalAlignment;
    private Double topMarginRatio;
    private Double leftMarginRatio;
    private Double rightMarginRatio;
    private Double bottomMarginRatio;
    // TextLabel
    private Float fontScale;
    private RelativeBound bound;
    private TesmColor backgroundColor;
    private TextLabel.SizeMode sizeMode;

    public static StylePreset<TextLabelStyle> DEFAULT = new StylePreset<>(
            "default_text",
            TextLabelStyle.builder()
                    .selfHorizontalAlignment(HorizontalAlignment.NONE)
                    .selfVerticalAlignment(VerticalAlignment.NONE)
                    .backgroundColor(TesmColor.TRANSPARENT)
                    .sizeMode(TextLabel.SizeMode.RELATIVE_TO_PARENT)
                    .build()
    );

    @Override
    public StylePreset<TextLabelStyle> getDefaultStyle() {
        return DEFAULT;
    }
}
