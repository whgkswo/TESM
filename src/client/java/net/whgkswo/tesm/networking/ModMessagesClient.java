package net.whgkswo.tesm.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.networking.packet.*;
import net.whgkswo.tesm.raycast.CenterRaycast;

public class ModMessagesClient {

    public static final Identifier GETNBT_RESPONSE_ID = new Identifier(TESMMod.MODID, "getnbt_response");

    // ID와 패킷 쌍 등록
    public static void registerC2SPackets(){

    }

    // 결과값 수신 및 처리
    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(GETNBT_RESPONSE_ID, (client2, handler, responseBuf, responseSender) -> {

            CenterRaycast.interactOverlayOn = responseBuf.readBoolean();
        });

    }
}
