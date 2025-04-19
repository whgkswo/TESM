package net.whgkswo.tesm.gui.component.elements.style;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TextLabelStyle implements GuiStyle, DefaultStyleProvider<TextLabelStyle>{
    // GuiComponent
    private HorizontalAlignment selfHorizontalAlignment;
    private VerticalAlignment selfVerticalAlignment;
    // TextLabel
    private float fontScale;
    private RelativeBound bound;
    private TesmColor backgroundColor;

    public static StylePreset<TextLabelStyle> DEFAULT = new StylePreset<>(
            "default_text",
            TextLabelStyle.builder()
                    .selfHorizontalAlignment(HorizontalAlignment.NONE)
                    .selfVerticalAlignment(VerticalAlignment.NONE)
                    .fontScale(1.0f)
                    .backgroundColor(TesmColor.TRANSPARENT)
                    .build()
    );

    @Override
    public StylePreset<TextLabelStyle> getDefaultStyle() {
        return DEFAULT;
    }
}
