package net.whgkswo.tesm.gui.component.components.style;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.whgkswo.tesm.gui.GuiDirection;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiAxis;
import net.whgkswo.tesm.gui.component.bounds.PositionType;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;

import java.util.HashMap;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
// 참조타입 초기값은 디폴트 스타일로 처리
public class BoxStyle implements GuiStyle, DefaultStyleProvider<BoxStyle> {
    // GuiComponent
    private Boolean isVisible;
    private HorizontalAlignment selfHorizontalAlignment;
    private VerticalAlignment selfVerticalAlignment;
    private Double topMarginRatio;
    private Double leftMarginRatio;
    private Double rightMarginRatio;
    private Double bottomMarginRatio;
    // ParentComponent
    private GuiAxis axis;
    private HorizontalAlignment childrenHorizontalAlignment;
    private VerticalAlignment childrenVerticalAlignment;
    private Double horizontalGap;
    private Double verticalGap;
    // Box
    private RelativeBound bound;
    private TesmColor edgeColor;
    private TesmColor backgroundColor;
    private EdgeVisibility edgeVisibilities;
    private Integer edgeThickness;

    public static final StylePreset<BoxStyle> DEFAULT = new StylePreset<>(
            "default_box",
            BoxStyle.builder()
                    .selfHorizontalAlignment(HorizontalAlignment.NONE)
                    .selfVerticalAlignment(VerticalAlignment.NONE)
                    .axis(GuiAxis.VERTICAL)
                    .childrenHorizontalAlignment(HorizontalAlignment.NONE)
                    .childrenVerticalAlignment(VerticalAlignment.NONE)
                    .bound(RelativeBound.FULL_SCREEN)
                    .edgeColor(TesmColor.WHITE)
                    .backgroundColor(TesmColor.WHITE)
                    .edgeVisibilities(EdgeVisibility.NO_EDGES)
                    .build()
    );

    public static final StylePreset<BoxStyle> TEST = new StylePreset<>(
            "test",
            BoxStyle.builder()
                    .backgroundColor(TesmColor.BLACK)
                    .bound(new RelativeBound(0.3, 0.3))
                    .build()
    );

    public static final StylePreset<BoxStyle> ROOT_MODAL = new StylePreset<>(
            "root_modal",
            BoxStyle.builder()
                    .backgroundColor(TesmColor.BLACK.withAlpha(200))
                    .bound(new RelativeBound(0.45, 0.8))
                    .selfHorizontalAlignment(HorizontalAlignment.CENTER)
                    .selfVerticalAlignment(VerticalAlignment.CENTER)
                    .childrenHorizontalAlignment(HorizontalAlignment.CENTER)
                    .childrenVerticalAlignment(VerticalAlignment.CENTER)
                    .edgeColor(TesmColor.WHITE)
                    .edgeVisibilities(EdgeVisibility.FULL_EDGES)
                    .edgeThickness(2)
                    .build()
    );

    @Override
    public StylePreset<BoxStyle> getDefaultStyle() {
        return DEFAULT;
    }
}
