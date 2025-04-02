package net.whgkswo.tesm.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.gui.helpers.GuiHelper;

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

    float playerYaw;
    public float northCompassPos;
    public float westCompassPos;
    public float southCompassPos;
    public float eastCompassPos;

    public boolean northCompassOn;
    public boolean westCompassOn;
    public boolean southCompassOn;
    public boolean eastCompassOn;

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        if(client!=null){
            screenWidth = client.getWindow().getScaledWidth();
            screenHeight = client.getWindow().getScaledHeight();
            getCompassPos(GlobalVariables.player);
        }

        RenderSystem.setShaderColor(1.0F,1.0F,1.0F,1.0F);

        RenderSystem.enableBlend(); // 반투명 텍스처의 알파값이 제대로 처리될 수 있도록 하는 메소드, 평소엔 괜찮지만 채팅창이 켜진다거나 하면 이게 꺼질 수 있으므로 반투명 텍스처 호출 전에는 이걸 써주는 게 좋다.

        drawContext.getMatrices().push(); // 현재 매트릭스 배율을 저장
        drawContext.getMatrices().scale(HUD_MAG,HUD_MAG,1);

        drawContext.drawTexture(GuiHelper::getGuiTexturedLayer, COMPASS_BLACK, (int) ((screenWidth-TEXTURE_W*HUD_MAG)/2/HUD_MAG), (int) (screenHeight *0.03/HUD_MAG),0,0,TEXTURE_W,TEXTURE_H,TEXTURE_W,TEXTURE_H);
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

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 플레이어 방향과 동서남북 차이 검사 메소드 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    public void getCompassPos(PlayerEntity player){

        playerYaw = player.getYaw();

        if(playerYaw>=135){
            northCompassPos = 180-playerYaw;
            northCompassOn = true;
        } else if (playerYaw<=-135) {
            northCompassPos = (180+playerYaw)*-1;
            northCompassOn = true;
        }else{
            northCompassOn = false;
        }

        if(45<=playerYaw && playerYaw<=135){
            westCompassPos = 90-playerYaw;
            westCompassOn = true;
        }else{
            westCompassOn = false;
        }

        if(-45<=playerYaw && playerYaw<=45){
            southCompassPos = -playerYaw;
            southCompassOn = true;
        }else {
            southCompassOn = false;
        }

        if(-135<=playerYaw && playerYaw<=-45){
            eastCompassPos = -90-playerYaw;
            eastCompassOn = true;
        }else {
            eastCompassOn = false;
        }
    }
}
