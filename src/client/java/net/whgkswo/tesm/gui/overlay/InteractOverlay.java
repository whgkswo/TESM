package net.whgkswo.tesm.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.raycast.CenterRaycast;

import static net.whgkswo.tesm.conversation.ConversationStart.convOn;
import static net.whgkswo.tesm.raycast.CenterRaycast.interactTarget;
import static net.whgkswo.tesm.raycast.CenterRaycast.interactType;

public class InteractOverlay implements HudRenderCallback{
    MinecraftClient client = MinecraftClient.getInstance();
    int screenWidth = 0;
    int screenHeight = 0;

    static final int HUD_W = 138;
    static final int HUD_H = 33;
    private final Identifier INTERACT_HUD = new Identifier(TESMMod.MODID, "textures/gui/interact_hud.png");

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        if(client!=null){
            screenWidth = client.getWindow().getScaledWidth();
            screenHeight = client.getWindow().getScaledHeight();
        }

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F,1.0F,1.0F,1.0F);
        //RenderSystem.setShaderTexture(0, INTERACT_HUD);

        if(CenterRaycast.interactOverlayOn&&!convOn){
            final float HUD_MAG = 0.6f;
            final float TEXT_MAG = 0.8f;

            RenderSystem.enableBlend(); // 반투명 텍스처의 알파값이 제대로 처리될 수 있도록 하는 메소드, 평소엔 괜찮지만 채팅창이 켜진다거나 하면 이게 꺼질 수 있으므로 반투명 텍스처 호출 전에는 이걸 써주는 게 좋다.

            drawContext.getMatrices().push(); // 현재 매트릭스 배율을 저장
            drawContext.getMatrices().scale(HUD_MAG,HUD_MAG,1);
            drawContext.drawTexture(INTERACT_HUD, (int) (screenWidth *0.6/HUD_MAG), (int) (screenHeight *0.4/HUD_MAG),0,0,HUD_W,HUD_H,HUD_W,HUD_H);
            drawContext.getMatrices().pop();

            drawContext.getMatrices().push();
            drawContext.getMatrices().scale(TEXT_MAG, TEXT_MAG, 1); // 매트릭스 x,y,z축 방향 배율 조정. 마크 HUD는 2d 작업이므로 z값은 의미 없음
            drawContext.drawText(client.textRenderer,interactType, (int)(screenWidth *0.65/ TEXT_MAG), (int)(screenHeight *0.403/ TEXT_MAG),0xAAA685,true);
            drawContext.drawText(client.textRenderer,interactTarget, (int)(screenWidth *0.65/ TEXT_MAG), (int)(screenHeight *0.445/ TEXT_MAG),0xFFFFFF,false);
            drawContext.getMatrices().pop(); // 원래의 매트릭스 배율로 복귀. 이로서 다른 요소들은 정상적인 크기로 렌더링됨
        }

    }
}