package net.whgkswo.tesm.gui.component.elements.style;

import lombok.Builder;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;

@Builder
public record TextLabelStyle(
        // GuiComponent
        HorizontalAlignment selfHorizontalAlignment,
        VerticalAlignment selfVerticalAlignment,
        // TextLabel
        float fontScale,
        RelativeBound bound,
        TesmColor backgroundColor
) implements GuiStyle{
    public static StylePreset<TextLabelStyle> DEFAULT = new StylePreset<>(
            "default_text",
            TextLabelStyle.builder()
                    .selfHorizontalAlignment(HorizontalAlignment.NONE)
                    .selfVerticalAlignment(VerticalAlignment.NONE)
                    .fontScale(1.0f)
                    .backgroundColor(TesmColor.TRANSPARENT)
                    .build()
    );
}
