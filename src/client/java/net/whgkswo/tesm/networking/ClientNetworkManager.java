package net.whgkswo.tesm.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.networking.payload.data.GeneralReq;
import net.whgkswo.tesm.networking.payload.data.s2c_res.ConversationNbtRes;
import net.whgkswo.tesm.networking.payload.data.s2c_res.RaycastingNbtRes;
import net.whgkswo.tesm.networking.receivers.ConversationNbtS2CReceiver;
import net.whgkswo.tesm.networking.receivers.RaycastingNbtS2CReceiver;
import net.whgkswo.tesm.networking.receivers.s2c_req.GeneralReqReceiver;

public class ClientNetworkManager {

    // ID와 패킷 쌍 등록
    public static void registerC2SPackets(){

    }

    // S2C 요청 리시버 등록
    public static void registerS2CReceivers(){
        // C2S에 대한 서버 응답 패킷
        ClientPlayNetworking.registerGlobalReceiver(ConversationNbtRes.PACKET_ID, ConversationNbtS2CReceiver::handle);
        ClientPlayNetworking.registerGlobalReceiver(RaycastingNbtRes.PACKET_ID, RaycastingNbtS2CReceiver::handle);

        // S2C 서버 요청 패킷
        ClientPlayNetworking.registerGlobalReceiver(GeneralReq.PACKET_ID, GeneralReqReceiver::handle);
    }

    // S2C 코덱 등록
    public static void registerS2CCodecs(){
        // C2S에 대한 서버 응답 코덱
        PayloadTypeRegistry.playS2C().register(ConversationNbtRes.PACKET_ID, ConversationNbtRes.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(RaycastingNbtRes.PACKET_ID, RaycastingNbtRes.PACKET_CODEC);

        // S2C 서버 요청 코덱
        PayloadTypeRegistry.playS2C().register(GeneralReq.PACKET_ID, GeneralReq.PACKET_CODEC);
    }
}
