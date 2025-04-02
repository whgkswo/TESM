package net.whgkswo.tesm.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.whgkswo.tesm.networking.payload.data.SimpleReq;
import net.whgkswo.tesm.networking.payload.data.c2s_req.ConversationNbtReq;
import net.whgkswo.tesm.networking.payload.data.c2s_req.RaycastingNbtReq;
import net.whgkswo.tesm.networking.payload.data.c2s_req.SetNbtReq;
import net.whgkswo.tesm.networking.payload.data.c2s_req.UseBlockReq;
import net.whgkswo.tesm.networking.payload.data.s2c_req.DoorNaming;
import net.whgkswo.tesm.networking.receivers.c2s_general.GeneralC2SReqReceiver;
import net.whgkswo.tesm.networking.receivers.c2s_req.*;

public class ServerNetworkManager {
    // C2S 리시버 등록
    public static void registerC2SReceivers(){
        // TODO: 포팅
        /*
        ServerPlayNetworking.registerGlobalReceiver(CHUNK_UPDATE_ID, ChunkUpdateC2SPacket::receive);*/
        ServerPlayNetworking.registerGlobalReceiver(ConversationNbtReq.PACKET_ID, ConversationNbtC2SReceiver::handle);
        ServerPlayNetworking.registerGlobalReceiver(DoorNaming.PACKET_ID, DoorNamingC2SReceiver::handle);
        ServerPlayNetworking.registerGlobalReceiver(RaycastingNbtReq.PACKET_ID, RaycastingNbtC2SReceiver::handle);
        ServerPlayNetworking.registerGlobalReceiver(SetNbtReq.PACKET_ID, SetNbtReceiver::handle);
        ServerPlayNetworking.registerGlobalReceiver(SimpleReq.PACKET_ID, GeneralC2SReqReceiver::handle);
        ServerPlayNetworking.registerGlobalReceiver(UseBlockReq.PACKET_ID, UseBlockReceiver::handle);
    }

    // C2S 코덱 등록
    public static void registerC2SCodecs(){
        PayloadTypeRegistry.playC2S().register(ConversationNbtReq.PACKET_ID, ConversationNbtReq.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(DoorNaming.PACKET_ID, DoorNaming.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(RaycastingNbtReq.PACKET_ID, RaycastingNbtReq.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(SetNbtReq.PACKET_ID, SetNbtReq.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(SimpleReq.PACKET_ID, SimpleReq.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(UseBlockReq.PACKET_ID, UseBlockReq.PACKET_CODEC);
    }
}