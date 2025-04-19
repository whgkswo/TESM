package net.whgkswo.tesm.gui.component.elements.style;

import lombok.Builder;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiAxis;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;

@Builder
public record BoxStyle(
        // GuiComponent
        HorizontalAlignment selfHorizontalAlignment,
        VerticalAlignment selfVerticalAlignment,
        // ParentComponent
        GuiAxis axis,
        HorizontalAlignment childrenHorizontalAlignment,
        VerticalAlignment childrenVerticalAlignment,
        // Box
        RelativeBound bound,
        TesmColor edgeColor,
        TesmColor backgroundColor
) implements GuiStyle{
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
}
