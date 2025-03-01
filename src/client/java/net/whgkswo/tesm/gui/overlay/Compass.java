package net.whgkswo.tesm.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;

import static net.whgkswo.tesm.general.OnServerTicks.*;

public class Compass implements HudRenderCallback {

    private MinecraftClient client = MinecraftClient.getInstance();
    private int screenWidth = 0;
    private int screenHeight = 0;

    private static final int TEXTURE_W = 208;
    private static final int TEXTURE_H = 19;
    private static final float HUD_MAG = 0.7f;
    private final Identifier COMPASS_BLACK = Identifier.of(TESMMod.MODID, "textures/gui/compass_black.png");
    private final Identifier COMPASS_WHITE = Identifier.of(TESMMod.MODID, "textures/gui/compass_white.png");

    // TODO: 포팅
    //@Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        if(client!=null){
            screenWidth = client.getWindow().getScaledWidth();
            screenHeight = client.getWindow().getScaledHeight();
        }

        // TODO: 포팅
        //RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F,1.0F,1.0F,1.0F);
        //RenderSystem.setShaderTexture(0, INTERACT_HUD);

        RenderSystem.enableBlend(); // 반투명 텍스처의 알파값이 제대로 처리될 수 있도록 하는 메소드, 평소엔 괜찮지만 채팅창이 켜진다거나 하면 이게 꺼질 수 있으므로 반투명 텍스처 호출 전에는 이걸 써주는 게 좋다.

        drawContext.getMatrices().push(); // 현재 매트릭스 배율을 저장
        drawContext.getMatrices().scale(HUD_MAG,HUD_MAG,1);
        // TODO: 포팅
        //drawContext.drawTexture(COMPASS_BLACK, (int) ((screenWidth-TEXTURE_W*HUD_MAG)/2/HUD_MAG), (int) (screenHeight *0.03/HUD_MAG),0,0,TEXTURE_W,TEXTURE_H,TEXTURE_W,TEXTURE_H);
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

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {

    }
}
