package net.whgkswo.tesm.gui.component.components.style;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiAxis;
import net.whgkswo.tesm.gui.component.bounds.LinearBound;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SLineStyle implements GuiStyle, DefaultStyleProvider<SLineStyle> {
    // GuiComponent
    private Boolean isVisible;
    private HorizontalAlignment selfHorizontalAlignment;
    private VerticalAlignment selfVerticalAlignment;
    private Double topMarginRatio;
    private Double leftMarginRatio;
    private Double rightMarginRatio;
    private Double bottomMarginRatio;
    // SLine
    private TesmColor color;
    private LinearBound bound;

    public static final StylePreset<SLineStyle> DEFAULT = new StylePreset<>(
            "default_line",
            SLineStyle.builder()
                    .color(TesmColor.WHITE)
                    .bound(new LinearBound(0, 0, GuiAxis.HORIZONTAL, 1, 1))
                    .isVisible(true)
                    .selfHorizontalAlignment(HorizontalAlignment.NONE)
                    .selfVerticalAlignment(VerticalAlignment.NONE)
                    .build()
            );

    @Override
    public StylePreset<SLineStyle> getDefaultStyle() {
        return DEFAULT;
    }
}
