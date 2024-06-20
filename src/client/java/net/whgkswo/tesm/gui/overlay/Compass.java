package net.whgkswo.tesm.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;

import static net.whgkswo.tesm.general.OnServerTicks.*;

public class Compass implements HudRenderCallback {

    MinecraftClient client = MinecraftClient.getInstance();
    int screenWidth = 0;
    int screenHeight = 0;

    static final int TEXTURE_W = 208;
    static final int TEXTURE_H = 19;
    private final Identifier COMPASS_BLACK = new Identifier(TESMMod.MODID, "textures/gui/compass_black.png");
    private final Identifier COMPASS_WHITE = new Identifier(TESMMod.MODID, "textures/gui/compass_white.png");
    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {

        final float HUD_MAG = 0.7f;
        //final float TEXT_MAG = 0.8f;

        if(client!=null){
            screenWidth = client.getWindow().getScaledWidth();
            //screenWidth = client.getWindow().
            //screenHeight = client.getWindow().getHeight();
            screenHeight = client.getWindow().getScaledHeight();
        }

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F,1.0F,1.0F,1.0F);
        //RenderSystem.setShaderTexture(0, INTERACT_HUD);

        RenderSystem.enableBlend(); // 반투명 텍스처의 알파값이 제대로 처리될 수 있도록 하는 메소드, 평소엔 괜찮지만 채팅창이 켜진다거나 하면 이게 꺼질 수 있으므로 반투명 텍스처 호출 전에는 이걸 써주는 게 좋다.

        drawContext.getMatrices().push(); // 현재 매트릭스 배율을 저장
        drawContext.getMatrices().scale(HUD_MAG,HUD_MAG,1);
        drawContext.drawTexture(COMPASS_BLACK, (int) ((screenWidth-TEXTURE_W*HUD_MAG)/2/HUD_MAG), (int) (screenHeight *0.03/HUD_MAG),0,0,TEXTURE_W,TEXTURE_H,TEXTURE_W,TEXTURE_H);
        //drawContext.drawTexture(COMPASS_WHITE, (int) ((screenWidth-TEXTURE_W*HUD_MAG)/2/HUD_MAG), (int) (screenHeight *0.13/HUD_MAG),0,0,TEXTURE_W,TEXTURE_H,TEXTURE_W,TEXTURE_H);
        if(northCompassOn){
            drawContext.drawText(client.textRenderer,"N",(int) ((screenWidth+northCompassPos*(TEXTURE_W*HUD_MAG/45))/2/HUD_MAG),(int) (screenHeight *0.01/HUD_MAG),0xFFFFFF,false);
        }
        if(westCompassOn){
            drawContext.drawText(client.textRenderer,"W",(int) ((screenWidth+westCompassPos*(TEXTURE_W*HUD_MAG/45))/2/HUD_MAG),(int) (screenHeight *0.01/HUD_MAG),0xFFFFFF,false);
        }
        if(southCompassOn){
            drawContext.drawText(client.textRenderer,"S",(int) ((screenWidth+southCompassPos*(TEXTURE_W*HUD_MAG/45))/2/HUD_MAG),(int) (screenHeight *0.01/HUD_MAG),0xFFFFFF,false);
        }
        if(eastCompassOn){
            drawContext.drawText(client.textRenderer,"E",(int) ((screenWidth+eastCompassPos*(TEXTURE_W*HUD_MAG/45))/2/HUD_MAG),(int) (screenHeight *0.01/HUD_MAG),0xFFFFFF,false);
        }
        drawContext.getMatrices().pop();
    }
}
