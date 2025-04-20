package net.whgkswo.tesm.gui.component.elements;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.elements.builder.TextLabelBuilder;
import net.whgkswo.tesm.gui.component.elements.style.TextLabelStyle;

@NoArgsConstructor
@Setter
// 스타일 요소가 아닌 필드는 여기에 초기값 명시
public class TextLabel extends GuiComponent<TextLabel, TextLabelStyle> {
    @Getter
    private Text text;
    @Getter
    private float fontScale;
    private boolean shadowed;
    private RelativeBound bound;
    private TesmColor backgroundColor;

    public static TextLabelBuilder builder(){
        return new TextLabelBuilder();
    }

    @Override
    protected void renderSelf(DrawContext context){
        RelativeBound absoluteBound = getAbsoluteBoundWithUpdate();

        RenderingHelper.fill(context, backgroundColor, absoluteBound);

        double fixedYRatio = absoluteBound.getYOffsetRatio();
        RenderingHelper.renderText(context, fontScale, text, absoluteBound.getXOffsetRatio(), fixedYRatio, shadowed);
    }

    @Override
    protected void initializeSize(){
        // 텍스트 사이즈에 따른 바운드 자동 설정
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        double textWidth = textRenderer.getWidth(text) * fontScale;
        double textHeight = textRenderer.fontHeight * fontScale;

        Window window = MinecraftClient.getInstance().getWindow();
        int screenWidth = window.getScaledWidth();
        int screenHeight = window.getScaledHeight();

        bound = new RelativeBound(
                textWidth / screenWidth,
                textHeight / screenHeight
        );
    }

    @Override
    public RelativeBound getBound() {
        return bound;
    }

    @Override
    protected Class<TextLabelStyle> getStyleType() {
        return TextLabelStyle.class;
    }


    @Override
    public double getAbsoluteWHRatio(double parentWH, double childWH){
        // 텍스트는 부모의 사이즈와 관계 없이 화면상의 크기가 일정함
        return childWH;
    }
}
