package net.whgkswo.tesm.gui.component.elements.style;

import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;

import java.util.HashMap;
import java.util.Map;

public record StylePreset<T extends GuiStyle>(String className, T style) {

    public static final Map<Class<?>, StylePreset<?>> DEFAULT_STYLES = new HashMap<>();

    // 스타일 타입이 늘어날 때마다 추가해줘야 함
    static {
        DEFAULT_STYLES.put(BoxStyle.class, BoxStyle.DEFAULT);
        DEFAULT_STYLES.put(TextLabelStyle.class, TextLabelStyle.DEFAULT);
    }

    public static StylePreset<BoxStyle> TEST_BOX_PRESET =
            new StylePreset<>(
                    "test",
                    BoxStyle.builder()
                            .selfHorizontalAlignment(HorizontalAlignment.CENTER)
                            .selfVerticalAlignment(VerticalAlignment.CENTER)
                            .backgroundColor(TesmColor.BLACK)
                            .build()
            );
}
