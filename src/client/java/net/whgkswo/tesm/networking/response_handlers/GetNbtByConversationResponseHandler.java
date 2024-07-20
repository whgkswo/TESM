package net.whgkswo.tesm.networking.response_handlers;

import net.minecraft.network.PacketByteBuf;
import net.whgkswo.tesm.general.GlobalVariablesClient;

public class GetNbtByConversationResponseHandler {
    public static void handle(PacketByteBuf response){
        GlobalVariablesClient.convPartnerTempName = response.readString();
        GlobalVariablesClient.convPartnerName = response.readString();
    }
}
