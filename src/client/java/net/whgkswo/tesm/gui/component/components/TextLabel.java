package net.whgkswo.tesm.gui.component.components;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.bounds.AbsolutePosition;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.components.builder.TextLabelBuilder;
import net.whgkswo.tesm.gui.component.components.style.TextLabelStyle;

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
    private SizeMode sizeMode;

    public static TextLabelBuilder builder(){
        return new TextLabelBuilder();
    }

    @Override
    protected void renderSelf(DrawContext context){
        RelativeBound absoluteBound = getAbsoluteBoundWithUpdate();

        RenderingHelper.fill(context, backgroundColor, absoluteBound);

        if(sizeMode.equals(SizeMode.RELATIVE_TO_PARENT)){
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            float baseFontHeight = textRenderer.fontHeight;

            // 화면 높이 구하기 (픽셀 단위)
            int screenHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();

            // 현재 바운드의 픽셀 높이 계산
            double boundHeightPixels = absoluteBound.getHeightRatio() * screenHeight;

            // 바운드 높이에 맞게 폰트 스케일 조정
            float adjustedFontScale = (float)(boundHeightPixels / baseFontHeight);

            // 조정된 폰트 스케일로 텍스트 렌더링
            double fixedYRatio = absoluteBound.getYOffsetRatio();
            RenderingHelper.renderText(context, adjustedFontScale, text, absoluteBound.getXOffsetRatio(), fixedYRatio, shadowed);
        } else {
            // 기존 폰트 스케일 사용
            double fixedYRatio = absoluteBound.getYOffsetRatio();
            RenderingHelper.renderText(context, fontScale, text, absoluteBound.getXOffsetRatio(), fixedYRatio, shadowed);
        }
    }

    @Override
    protected void initializeSize(){
        if(sizeMode.equals(SizeMode.ABSOLUTE_PIXELS)){
            initializeSizeForAbsoluteMode();
        }else { // RELATIVE_TO_PARENT
            initializeSizeForRelativeMode();
        }
    }


    protected RelativeBound initializeSizeForAbsoluteMode(){
        // 텍스트 사이즈에 따른 바운드 자동 설정
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        double textWidth = textRenderer.getWidth(text) * fontScale;
        double textHeight = textRenderer.fontHeight * fontScale;

        AbsolutePosition parentPosition = getParent().getAbsolutePosition();

        bound = new RelativeBound(
                textWidth / parentPosition.getWidth(),
                textHeight / parentPosition.getHeight()
        );
        return bound;
    }

    protected RelativeBound initializeSizeForRelativeMode(){
        // 텍스트 사이즈에 따른 바운드 자동 설정
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        Window window = MinecraftClient.getInstance().getWindow();
        double windowScale = (double) window.getScaledHeight() / RenderingHelper.DEFAULT_WINDOW_HEIGHT;

        double textWidth = textRenderer.getWidth(text) * fontScale * windowScale;
        double textHeight = textRenderer.fontHeight * fontScale * windowScale;

        AbsolutePosition parentPosition = getParent().getAbsolutePosition();

        bound = new RelativeBound(
                textWidth / parentPosition.getWidth(),
                textHeight / parentPosition.getHeight()
        );
        return bound;
    }

    @Override
    public RelativeBound getBound() {
        return bound;
    }

    @Override
    protected Class<TextLabelStyle> getStyleType() {
        return TextLabelStyle.class;
    }

    public enum SizeMode{
        ABSOLUTE_PIXELS,
        RELATIVE_TO_PARENT
    }
}
