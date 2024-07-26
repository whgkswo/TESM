package net.whgkswo.tesm.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

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
    public static void renderTexture(DrawContext context, Identifier texture, double xRatio, double yRatio,
                                     double widthRatio, double heightRatio){
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        RenderSystem.enableBlend();
        context.drawTexture(texture, (int)(screenWidth * xRatio), (int)(screenHeight * yRatio),0,0,
                (int)(screenWidth * widthRatio), (int)(screenHeight * heightRatio), (int)(screenWidth * widthRatio), (int)(screenHeight * heightRatio));
    }
    public static void renderFilledBox(DrawContext context, Identifier texture, double xRatio, double yRatio, double widthRatio, double heightRatio){
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        context.drawTexture(texture, (int)(screenWidth * xRatio), (int)(screenHeight * yRatio), 0,0,(int)(screenWidth * widthRatio), (int)(screenHeight * heightRatio));
    }
    public static void renderHorizontalLine(DrawContext context, Identifier texture, double xRatio, double yRatio, double widthRatio, int thickness){
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        context.drawTexture(texture, (int)(screenWidth * xRatio), (int)(screenHeight * yRatio), 0,0,(int)(screenWidth * widthRatio), thickness);
    }
    public static void renderHorizontalLine(DrawContext context, Identifier texture, double xRatio, double yRatio, double widthRatio, int thickness, int yOffset){
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        context.drawTexture(texture, (int)(screenWidth * xRatio), (int)(screenHeight * yRatio) + yOffset, 0,0,(int)(screenWidth * widthRatio), thickness);
    }
    public static void renderVerticalLine(DrawContext context, Identifier texture, double xRatio, double yRatio, int thickness, double heightRatio){
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        context.drawTexture(texture, (int)(screenWidth * xRatio), (int)(screenHeight * yRatio), 0,0, thickness,(int)(screenHeight * heightRatio));
    }
    public static void renderVerticalLine(DrawContext context, Identifier texture, double xRatio, double yRatio, int thickness, double heightRatio, int xOffset){
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        context.drawTexture(texture, (int)(screenWidth * xRatio) + xOffset, (int)(screenHeight * yRatio), 0,0, thickness,(int)(screenHeight * heightRatio));
    }
    public static void renderBlankedBox(DrawContext context, Identifier texture, double xRatio, double yRatio, double widthRatio, double heightRatio, int thickness){
        renderHorizontalLine(context, texture, xRatio, yRatio, widthRatio, thickness);
        renderHorizontalLine(context, texture ,xRatio,yRatio + heightRatio, widthRatio, thickness, -thickness);
        renderVerticalLine(context, texture, xRatio, yRatio, thickness, heightRatio);
        renderVerticalLine(context, texture, xRatio + widthRatio, yRatio, thickness, heightRatio, -thickness);
    }
    public static int getXPos(DrawContext context, double positionRatio, float scale){
        return (int)(context.getScaledWindowWidth() * positionRatio / scale);
    }
    public static int getYPos(DrawContext context, double positionRatio, float scale){
        return (int)(context.getScaledWindowHeight() * positionRatio / scale);
    }
}
