package net.whgkswo.tesm.networking;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.networking.response_handlers.GetNbtByConversationResponseHandler;
import net.whgkswo.tesm.networking.response_handlers.GetNbtByRaycastingResponseHandler;
import net.whgkswo.tesm.networking.response_handlers.ResetQuestsHandler;

public class ModMessagesClient {

    public static final Identifier GETNBT_BY_RAYCASTING_RESPONSE = new Identifier(TESMMod.MODID, "getnbt_raycasting_response");
    public static final Identifier GETNBT_BY_CONVERSATION_RESPONSE = new Identifier(TESMMod.MODID, "getnbt_conversation_response");
    public static final Identifier RESET_QUESTS = new Identifier(TESMMod.MODID, "reset_quests");

    // ID와 패킷 쌍 등록
    public static void registerC2SPackets(){

    }

    // 결과값 수신 및 처리
    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(GETNBT_BY_RAYCASTING_RESPONSE, (client, handler, responseBuf, responseSender) -> {
            GetNbtByRaycastingResponseHandler.handle(responseBuf);
        });
        ClientPlayNetworking.registerGlobalReceiver(GETNBT_BY_CONVERSATION_RESPONSE, (client, handler, responseBuf, responseSender) -> {
            GetNbtByConversationResponseHandler.handle(responseBuf);
        });
        ClientPlayNetworking.registerGlobalReceiver(RESET_QUESTS, ((client, handler, buf, responseSender) -> {
            ResetQuestsHandler.handle(buf);
        }));
    }
}
