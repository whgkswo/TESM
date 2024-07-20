package net.whgkswo.tesm.networking.response_handlers;

import net.minecraft.network.PacketByteBuf;
import net.whgkswo.tesm.raycast.CenterRaycast;

public class GetNbtResponseHandler {
    public static void handle(PacketByteBuf response){
        CenterRaycast.interactOverlayOn = response.readBoolean();
        String tempName = response.readString();
        String name = response.readString();
        CenterRaycast.interactTarget = tempName.isEmpty() ? name : tempName;
    }
}
