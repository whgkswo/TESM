package net.whgkswo.tesm.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.gui.colors.BaseTexture;
import net.whgkswo.tesm.gui.colors.CustomColor;

import java.util.Optional;

public class RenderingHelper {
    public static final TextRenderer TEXT_RENDERER = MinecraftClient.getInstance().textRenderer;
    public static final double DEFAULT_TEXT_VERTICAL_WIDTH_RATIO = (double) 1 / 50;
    public static void renderText(Alignment alignment, DrawContext context, float scale, String str,
                                  int x, int y, int color){
        context.getMatrices().push();
        context.getMatrices().scale(scale,scale,1);
        if(alignment == Alignment.LEFT){
            context.drawTextWithShadow(TEXT_RENDERER,str, x,y,color);
        } else if (alignment == Alignment.CENTER) {
            context.drawCenteredTextWithShadow(TEXT_RENDERER,str, x,y,color);
        }
        context.getMatrices().pop();
    }
    public static void renderText(Alignment alignment, DrawContext context, float scale, String str,
                                  double xRatio, double yRatio, int color){
        int xPos = getXPos(context, xRatio, scale);
        int yPos = getYPos(context, yRatio, scale);
        context.getMatrices().push();
        context.getMatrices().scale(scale,scale,1);
        switch (alignment){
            case LEFT -> {
                context.drawTextWithShadow(TEXT_RENDERER,str, xPos, yPos,color);
            }
            case CENTER -> {
                context.drawCenteredTextWithShadow(TEXT_RENDERER,str, xPos, yPos,color);
            }
            case RIGHT -> {
                int textWidth = TEXT_RENDERER.getWidth(str);
                context.drawTextWithShadow(TEXT_RENDERER, str, xPos - textWidth, yPos, color);
            }
        }
        context.getMatrices().pop();
    }
    public static void renderText(Alignment alignment, DrawContext context, float scale, Text text,
                                  double xRatio, double yRatio, int color){
        int xPos = getXPos(context, xRatio, scale);
        int yPos = getYPos(context, yRatio, scale);

        context.getMatrices().push();
        context.getMatrices().scale(scale,scale,1);

        switch (alignment){
            case LEFT -> {
                context.drawTextWithShadow(TEXT_RENDERER, text, xPos, yPos, color);
            }
            case CENTER -> {
                context.drawCenteredTextWithShadow(TEXT_RENDERER,text, xPos, yPos ,color);
            }
            case RIGHT -> {
                int textWidth = TEXT_RENDERER.getWidth(text.getString());
                context.drawTextWithShadow(TEXT_RENDERER, text, xPos - textWidth, yPos, color);
            }
        }
        context.getMatrices().pop();
    }
    public static void renderTextInBox(Alignment alignment, DrawContext context, float scale, String str,
                                  double xRatio, double yRatio, double widthRatio, int color){

        int yPos = getYPos(context, yRatio, scale);
        int offset = (int) (TEXT_RENDERER.getWidth("가") * 0.4);

        context.getMatrices().push();
        context.getMatrices().scale(scale,scale,1);

        switch (alignment){
            case LEFT -> {
                int xPos = getXPos(context, xRatio, scale) + offset;
                context.drawTextWithShadow(TEXT_RENDERER,str, xPos, yPos,color);
            }
            case CENTER -> {
                int xPos = getXPos(context, xRatio + widthRatio / 2, scale);
                context.drawCenteredTextWithShadow(TEXT_RENDERER,str, xPos, yPos,color);
            }
            case RIGHT -> {
                int textWidth = TEXT_RENDERER.getWidth(str);
                int xPos = getXPos(context, xRatio + widthRatio, scale) - offset;
                context.drawTextWithShadow(TEXT_RENDERER, str, xPos - textWidth, yPos, color);
            }
        }
        context.getMatrices().pop();
    }
    public static void renderTexture(DrawContext context, Identifier texture, double xRatio, double yRatio,
                                     double widthRatio, double heightRatio){
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        RenderSystem.enableBlend();
        context.drawTexture(texture, (int)(screenWidth * xRatio), (int)(screenHeight * yRatio),0,0,
                (int)(screenWidth * widthRatio), (int)(screenHeight * heightRatio), (int)(screenWidth * widthRatio), (int)(screenHeight * heightRatio));
    }
    public static void renderTextureWithColorFilter(DrawContext context,Identifier texture, int x, int y,
                                                    int width, int height, CustomColor color){
        if(color.getA() != 255){
            RenderSystem.enableBlend();
        }
        context.setShaderColor(color.getFloatR(), color.getFloatG() ,color.getFloatB(), color.getFloatA());
        context.drawTexture(texture, x, y, 0, 0, width, height);
        // 화면의 모든 요소 위에 그려지는 오버레이를 원한다면 아래 코드는 삭제
        context.setShaderColor(1.0f,1.0f,1.0f,1.0f);
    }
    public static void renderFilledBox(DrawContext context, CustomColor color, double xRatio, double yRatio, double widthRatio, double heightRatio){
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();

        if(color.getA() != 255){
            RenderSystem.enableBlend();
        }
        context.setShaderColor(color.getFloatR(), color.getFloatG(), color.getFloatB(), color.getFloatA());
        context.drawTexture(BaseTexture.BASE_TEXTURE, (int)(screenWidth * xRatio), (int)(screenHeight * yRatio), 0,0,(int)(screenWidth * widthRatio), (int)(screenHeight * heightRatio));
        // 화면의 모든 요소 위에 그려지는 오버레이를 원한다면 아래 코드는 삭제
        context.setShaderColor(1.0f,1.0f,1.0f,1.0f);
    }
    public static int getXPos(DrawContext context, double positionRatio, float scale){
        return (int)(context.getScaledWindowWidth() * positionRatio / scale);
    }
    public static int getYPos(DrawContext context, double positionRatio, float scale){
        return (int)(context.getScaledWindowHeight() * positionRatio / scale);
    }
}
