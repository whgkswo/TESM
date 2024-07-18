package net.whgkswo.tesm.conversation.v2;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.whgkswo.tesm.general.GlobalVariablesClient;
import net.whgkswo.tesm.networking.ModMessages;

import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ConversationScreen extends Screen {
    private Entity partner;
    public ConversationScreen(Entity partner){
        super(Text.literal("대화 시스템 GUI"));
        this.partner = partner;
    }
    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta){
    }
    @Override
    public boolean shouldPause(){
        return false;
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta){
        super.render(context, mouseX, mouseY, delta);
        // NPC 이름 출력
        final float nameScale = 1.5f;
        String partnerName = partner.getCustomName().getString();
        scaledRender(context,nameScale,partnerName,(int)(width/2/nameScale),(int)(height*0.55/nameScale),0xffffff);
        // 대사 출력
        final float lineScale = 0.8f;
        NpcDialogues partnerDL = GlobalVariablesClient.NPC_DIALOGUES.get(partnerName);
        NormalStage dialogue = partnerDL.getNormalLines().get("1");
        DecisionStage decisions = partnerDL.getDecisions().get("1");
        scaledRender(context,lineScale,dialogue.getContents().get(0),
                (int) (width/2/lineScale),(int) (height*0.7/lineScale),0xffffff);
    }
    private void scaledRender(DrawContext context ,float scale, String str,
                              int x, int y, int color){
        context.getMatrices().push();
        context.getMatrices().scale(scale,scale,1);
        context.drawCenteredTextWithShadow(textRenderer,str, x,y,color);
        context.getMatrices().pop();
    }
    @Override
    public void close(){
        // 대화 상대 움직임 제한 해제 (서버에 패킷 전송)
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(partner.getId());
        ClientPlayNetworking.send(ModMessages.UNFREEZE_ENTITY_ID,buf);
        super.close();
    }
}



