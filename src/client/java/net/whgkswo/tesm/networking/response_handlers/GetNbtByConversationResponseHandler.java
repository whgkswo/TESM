package net.whgkswo.tesm.networking.response_handlers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.whgkswo.tesm.conversation.ConversationScreen;
import net.whgkswo.tesm.conversation.ConversationStart;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.general.GlobalVariablesClient;

public class GetNbtByConversationResponseHandler {
    public static void handle(PacketByteBuf response){
        GlobalVariablesClient.convPartnerTempName = response.readString();
        GlobalVariablesClient.convPartnerName = response.readString();
        MinecraftClient client = MinecraftClient.getInstance();
        client.execute(()->{
            // 대사가 있는 엔티티일 경우 대화 시작
            if(GlobalVariablesClient.NPC_DIALOGUES.containsKey(GlobalVariablesClient.convPartnerName)){
                ConversationStart.conversationStart(GlobalVariables.player, client);
            }
        });
    }
}
