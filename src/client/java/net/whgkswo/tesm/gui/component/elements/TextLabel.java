package net.whgkswo.tesm.gui.component.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;

@SuperBuilder
public class TextLabel extends GuiComponent<TextLabel> {
    @Getter
    private Text text;
    @Getter
    @Builder.Default
    private float fontScale = 1.0f;
    private RelativeBound bound;

    @Override
    protected void renderSelf(DrawContext context){
        RelativeBound absoluteBound = getScreenRelativeBoundWithUpdate();

        double strRef = 0;
        HorizontalAlignment textAlignment = getSelfHorizontalAlignment();
        switch (textAlignment){
            case LEFT -> strRef = absoluteBound.getXMarginRatio();
            case CENTER -> strRef = absoluteBound.getXMarginRatio() + absoluteBound.getWidthRatio() / 2;
            case RIGHT -> strRef = absoluteBound.getXMarginRatio()/* + absoluteBound.getWidthRatio() - absoluteBound.getWidthRatio()*/;
        }
        double fixedYRatio = absoluteBound.getYMarginRatio();
        RenderingHelper.renderText(textAlignment, context, fontScale, text, strRef, fixedYRatio);
    }

    @Override
    protected void initializeExtended(){
        // 텍스트 사이즈에 따른 바운드 보정
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int textWidth = textRenderer.getWidth(text);
        double textHeight = textRenderer.fontHeight * fontScale;

        Window window = MinecraftClient.getInstance().getWindow();
        int screenWidth = window.getScaledWidth();
        int screenHeight = window.getScaledHeight();

        bound = new RelativeBound(
                (double) textWidth / screenWidth,
                textHeight / screenHeight
        );
    }

    @Override
    protected RelativeBound getBound() {
        return bound;
    }
}
