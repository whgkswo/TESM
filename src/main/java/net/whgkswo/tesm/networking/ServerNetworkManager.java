package net.whgkswo.tesm.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.networking.payload.data.c2s_req.ConversationNbtReq;
import net.whgkswo.tesm.networking.payload.data.c2s_req.RaycastingNbtReq;
import net.whgkswo.tesm.networking.payload.data.c2s_req.SetNbtReq;
import net.whgkswo.tesm.networking.receivers.ConversationNbtC2SReceiver;
import net.whgkswo.tesm.networking.receivers.RaycastingNbtC2SReceiver;
import net.whgkswo.tesm.networking.receivers.SetNbtReceiver;

public class ServerNetworkManager {
    // C2S 리시버 등록
    public static void registerC2SReceivers(){
        // TODO: 포팅
        /*ServerPlayNetworking.registerGlobalReceiver(EXAMPLE_ID, ExampleC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(FREEZE_ENTITY_ID, FreezeEntityC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(UNFREEZE_ENTITY_ID, UnfreezeEntityC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(GETNBT_BY_RAYCASTING, GetNbtByRaycastingC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(GETNBT_BY_CONVERSATION, GetNbtByConversationC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(TICK_FREEZE_TOGGLE_ID, TickFreezeToggleC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(USE_BLOCK_ID, UseBlockC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(CHUNK_UPDATE_ID, ChunkUpdateC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(UPDATE_NBT, UpdateNbtC2SPacket::receive);*/
        /*ServerPlayNetworking.registerGlobalReceiver(ConversationNbtReq.PACKET_ID, ((payload, context) -> {
            GlobalVariables.player.sendMessage(Text.literal("서버가 패킷을 받았습니다."), false);
        }));*/
        ServerPlayNetworking.registerGlobalReceiver(ConversationNbtReq.PACKET_ID, ConversationNbtC2SReceiver::handle);
        ServerPlayNetworking.registerGlobalReceiver(RaycastingNbtReq.PACKET_ID, RaycastingNbtC2SReceiver::handle);
        ServerPlayNetworking.registerGlobalReceiver(SetNbtReq.PACKET_ID, SetNbtReceiver::handle);
    }

    // C2S 코덱 등록
    public static void registerC2SCodecs(){
        PayloadTypeRegistry.playC2S().register(ConversationNbtReq.PACKET_ID, ConversationNbtReq.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(RaycastingNbtReq.PACKET_ID, RaycastingNbtReq.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(SetNbtReq.PACKET_ID, SetNbtReq.PACKET_CODEC);
    }
}