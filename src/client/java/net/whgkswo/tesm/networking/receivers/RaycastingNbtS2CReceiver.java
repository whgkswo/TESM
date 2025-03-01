package net.whgkswo.tesm.networking.receivers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.general.GlobalVariablesClient;
import net.whgkswo.tesm.networking.payload.s2c_res.RaycastingNbtRes;
import net.whgkswo.tesm.raycast.CenterRaycast;

public class RaycastingNbtS2CReceiver {
    public static void handle(RaycastingNbtRes payload, ClientPlayNetworking.Context context){
        CenterRaycast.interactOverlayOn = payload.isInteractable();
        String tempName = payload.tempName();
        String name = payload.name();

        String interactTarget = tempName.isEmpty() ? name : tempName;
        CenterRaycast.interactTarget = interactTarget;

        //GlobalVariables.player.sendMessage(Text.of(String.format("Interactable: %b, TempName: %s, Name: %s", payload.isInteractable(), tempName, name)), false);
    }
}
