package net.whgkswo.tesm.networking.receivers.s2c_req;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.whgkswo.tesm.networking.payload.data.s2c_res.NullRes;
import net.whgkswo.tesm.networking.payload.id.RequestReason;

import static net.whgkswo.tesm.gui.overlay.raycast.HUDRaycastHelper.*;

public class NullResReceiver {
    public static void handle(NullRes payload, ClientPlayNetworking.Context context){
        RequestReason requestReason = payload.requestReason();

        switch (requestReason){
            case CENTER_RAYCAST_BLOCK -> onCenterRaycastBlock();
        }
    }

    private static void onCenterRaycastBlock(){
        if(interactOverlayOn){
            interactOverlayOn = false;
            interactTarget = "";
            interactType = "";
        }
    }
}
