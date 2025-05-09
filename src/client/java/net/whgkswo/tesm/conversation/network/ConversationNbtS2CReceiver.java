package net.whgkswo.tesm.conversation.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.whgkswo.tesm.conversation.ConversationHelper;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.general.GlobalVariablesClient;
import net.whgkswo.tesm.networking.payload.data.s2c_res.ConversationNbtRes;

public class ConversationNbtS2CReceiver {
    public static void handle(ConversationNbtRes payload, ClientPlayNetworking.Context context){
        GlobalVariablesClient.convPartnerTempName = payload.tempName();
        GlobalVariablesClient.convPartnerName = payload.name();
        MinecraftClient client = MinecraftClient.getInstance();

        // 대사가 있는 엔티티일 경우 대화 시작
        /*if(GlobalVariablesClient.NPC_DIALOGUES.containsKey(GlobalVariablesClient.convPartnerName)){
            ConversationHelper.conversationStart(GlobalVariables.player, client);
        }*/
        ConversationHelper.conversationStart(payload, GlobalVariables.player, client);
    }
}
