package net.whgkswo.tesm.networking.receivers.s2c_req;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.whgkswo.tesm.networking.payload.data.s2c_res.RaycastingNbtRes;
import net.whgkswo.tesm.gui.overlay.raycast.HUDRaycastHelper;

public class RaycastingNbtS2CReceiver {
    public static void handle(RaycastingNbtRes payload, ClientPlayNetworking.Context context){
        HUDRaycastHelper.interactOverlayOn = payload.isInteractable();
        String tempName = payload.tempName();
        String name = payload.name();

        String interactTarget = tempName.isEmpty() ? name : tempName;
        HUDRaycastHelper.interactTarget = interactTarget;

        //GlobalVariables.player.sendMessage(Text.of(String.format("Interactable: %b, TempName: %s, Name: %s", payload.isInteractable(), tempName, name)), false);
    }
}
