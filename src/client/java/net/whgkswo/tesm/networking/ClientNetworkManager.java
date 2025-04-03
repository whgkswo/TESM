package net.whgkswo.tesm.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.whgkswo.tesm.networking.payload.data.SimpleReq;
import net.whgkswo.tesm.networking.payload.data.s2c_req.DoorNaming;
import net.whgkswo.tesm.networking.payload.data.s2c_res.ConversationNbtRes;
import net.whgkswo.tesm.networking.payload.data.s2c_res.DoorDataRes;
import net.whgkswo.tesm.networking.payload.data.s2c_res.NullRes;
import net.whgkswo.tesm.networking.payload.data.s2c_res.RaycastingNbtRes;
import net.whgkswo.tesm.networking.receivers.s2c_req.*;
import net.whgkswo.tesm.networking.receivers.s2c_req.simple_tasks.GeneralS2CReqReceiver;

public class ClientNetworkManager {

    // ID와 패킷 쌍 등록
    public static void registerC2SPackets(){

    }

    // S2C 요청 리시버 등록
    public static void registerS2CReceivers(){
        // C2S에 대한 서버 응답 패킷
        ClientPlayNetworking.registerGlobalReceiver(ConversationNbtRes.PACKET_ID, ConversationNbtS2CReceiver::handle);
        ClientPlayNetworking.registerGlobalReceiver(DoorDataRes.PACKET_ID, DoorDataResReceiver::handle);
        ClientPlayNetworking.registerGlobalReceiver(NullRes.PACKET_ID, NullResReceiver::handle);
        ClientPlayNetworking.registerGlobalReceiver(RaycastingNbtRes.PACKET_ID, RaycastingNbtS2CReceiver::handle);

        // S2C 서버 요청 패킷
        ClientPlayNetworking.registerGlobalReceiver(DoorNaming.PACKET_ID, DoorNamingS2CReceiver::handle);
        ClientPlayNetworking.registerGlobalReceiver(SimpleReq.PACKET_ID, GeneralS2CReqReceiver::handle);
    }

    // S2C 코덱 등록
    public static void registerS2CCodecs(){
        // C2S에 대한 서버 응답 코덱
        PayloadTypeRegistry.playS2C().register(ConversationNbtRes.PACKET_ID, ConversationNbtRes.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(DoorDataRes.PACKET_ID, DoorDataRes.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(NullRes.PACKET_ID, NullRes.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(RaycastingNbtRes.PACKET_ID, RaycastingNbtRes.PACKET_CODEC);

        // S2C 서버 요청 코덱
        PayloadTypeRegistry.playS2C().register(DoorNaming.PACKET_ID, DoorNaming.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(SimpleReq.PACKET_ID, SimpleReq.PACKET_CODEC);
    }
}
