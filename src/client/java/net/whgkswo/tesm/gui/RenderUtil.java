package net.whgkswo.tesm.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.general.GlobalVariablesClient;

public class RenderUtil {
    public static final TextRenderer TEXT_RENDERER = MinecraftClient.getInstance().textRenderer;
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
        int xPos = getXPos(xRatio, scale);
        int yPos = getYPos(yRatio, scale);
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
    public static void renderTexture(DrawContext context, Identifier id, int x, int y, int width, int height){
        RenderSystem.enableBlend();
        context.drawTexture(id, x, y,0,0, width, height, width, height);
    }
    public static int getXPos(double positionRatio, float scale){
        return (int)(GlobalVariablesClient.screenWidth * positionRatio / scale);
    }
    public static int getYPos(double positionRatio, float scale){
        return (int)(GlobalVariablesClient.screenHeight * positionRatio / scale);
    }
    public enum Alignment{
        LEFT,
        CENTER,
        RIGHT
    }
}
