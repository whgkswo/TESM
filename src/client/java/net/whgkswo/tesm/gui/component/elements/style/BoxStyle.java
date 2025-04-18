package net.whgkswo.tesm.gui.component.elements.style;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiAxis;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoxStyle implements GuiStyle, DefaultStyleProvider<BoxStyle> {
    // GuiComponent
    private HorizontalAlignment selfHorizontalAlignment;
    private VerticalAlignment selfVerticalAlignment;
    // ParentComponent
    private GuiAxis axis;
    private HorizontalAlignment childrenHorizontalAlignment;
    private VerticalAlignment childrenVerticalAlignment;
    // Box
    private RelativeBound bound;
    private TesmColor edgeColor;
    private TesmColor backgroundColor;

    public static final StylePreset<BoxStyle> DEFAULT = new StylePreset<>(
            "default_box",
            BoxStyle.builder()
                    .selfHorizontalAlignment(HorizontalAlignment.NONE)
                    .selfVerticalAlignment(VerticalAlignment.NONE)
                    .axis(GuiAxis.VERTICAL)
                    .childrenHorizontalAlignment(HorizontalAlignment.NONE)
                    .childrenVerticalAlignment(VerticalAlignment.NONE)
                    .bound(RelativeBound.FULL_SCREEN)
                    .edgeColor(TesmColor.TRANSPARENT)
                    .backgroundColor(TesmColor.WHITE)
                    .build()
    );

    public static final StylePreset<BoxStyle> TEST = new StylePreset<>(
            "test",
            BoxStyle.builder()
                    .backgroundColor(TesmColor.BLACK)
                    .bound(new RelativeBound(0.3, 0.3))
                    .build()
    );

    @Override
    public StylePreset<BoxStyle> getDefaultStyle() {
        return DEFAULT;
    }
}
