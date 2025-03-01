package net.whgkswo.tesm.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.networking.payload.s2c_res.ConversationNbtRes;
import net.whgkswo.tesm.networking.payload.s2c_res.RaycastingNbtRes;
import net.whgkswo.tesm.networking.receivers.ConversationNbtS2CReceiver;
import net.whgkswo.tesm.networking.receivers.RaycastingNbtC2SReceiver;
import net.whgkswo.tesm.networking.receivers.ConversationNbtC2SReceiver;
import net.whgkswo.tesm.networking.receivers.RaycastingNbtS2CReceiver;

public class ClientNetworkManager {

    public static final Identifier GETNBT_BY_RAYCASTING_RESPONSE = Identifier.of(TESMMod.MODID, "getnbt_raycasting_response");
    public static final Identifier GETNBT_BY_CONVERSATION_RESPONSE = Identifier.of(TESMMod.MODID, "getnbt_conversation_response");
    public static final Identifier RESET_QUESTS = Identifier.of(TESMMod.MODID, "reset_quests");

    // ID와 패킷 쌍 등록
    public static void registerC2SPackets(){

    }

    // S2C 요청 리시버 등록
    public static void registerS2CReceivers(){
        // TODO: 포팅
        /*ClientPlayNetworking.registerGlobalReceiver(GETNBT_BY_RAYCASTING_RESPONSE, (client, handler, responseBuf, responseSender) -> {
            GetNbtByRaycastingResponseHandler.handle(responseBuf);
        });
        ClientPlayNetworking.registerGlobalReceiver(GETNBT_BY_CONVERSATION_RESPONSE, (client, handler, responseBuf, responseSender) -> {
            GetNbtByConversationResponseHandler.handle(responseBuf);
        });
        ClientPlayNetworking.registerGlobalReceiver(RESET_QUESTS, ((client, handler, buf, responseSender) -> {
            ResetQuestsHandler.handle();
        }));*/
        ClientPlayNetworking.registerGlobalReceiver(ConversationNbtRes.PACKET_ID, ConversationNbtS2CReceiver::handle);
        ClientPlayNetworking.registerGlobalReceiver(RaycastingNbtRes.PACKET_ID, RaycastingNbtS2CReceiver::handle);
    }

    // S2C 코덱 등록
    public static void registerS2CCodecs(){
        PayloadTypeRegistry.playS2C().register(ConversationNbtRes.PACKET_ID, ConversationNbtRes.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(RaycastingNbtRes.PACKET_ID, RaycastingNbtRes.PACKET_CODEC);
    }
}
