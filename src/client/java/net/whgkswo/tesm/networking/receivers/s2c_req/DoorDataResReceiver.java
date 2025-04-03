package net.whgkswo.tesm.networking.receivers.s2c_req;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.whgkswo.tesm.direction.DirectionHelper;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.gui.overlay.raycast.HUDRaycastHelper;
import net.whgkswo.tesm.networking.payload.data.s2c_res.DoorDataRes;
import net.whgkswo.tesm.properties.data.DoorData;

import static net.whgkswo.tesm.gui.overlay.raycast.HUDRaycastHelper.interactOverlayOn;

public class DoorDataResReceiver {
    public static void handle(DoorDataRes payload, ClientPlayNetworking.Context context){
        DoorData doorData = payload.doorData();

        PlayerEntity player = GlobalVariables.player;
        float refYaw = player.getYaw();
        // 바깥으로 열리는 문이면 플레이어와 문의 facing이 반대
        if(doorData.pushToOutside()){
            refYaw = DirectionHelper.getOppositeYaw(refYaw);
        }

        interactOverlayOn = true;
        if(DirectionHelper.isInSameHalfPlane(doorData.facing(), refYaw)){
            HUDRaycastHelper.interactTarget = payload.doorData().insideName();
            if(payload.doorData().insideName().isEmpty()) interactOverlayOn = false;
        }else{
            HUDRaycastHelper.interactTarget = payload.doorData().outsideName();
            if(payload.doorData().outsideName().isEmpty()) interactOverlayOn = false;
        }

        HUDRaycastHelper.interactType = "열기";
    }
}
