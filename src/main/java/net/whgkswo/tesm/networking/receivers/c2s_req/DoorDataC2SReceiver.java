package net.whgkswo.tesm.networking.receivers.c2s_req;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.whgkswo.tesm.blocks.blockentity.DoorBlockEntity;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.networking.payload.data.c2s_req.BlockDataReq;
import net.whgkswo.tesm.networking.payload.data.s2c_res.DoorDataRes;
import net.whgkswo.tesm.networking.payload.data.s2c_res.NullRes;
import net.whgkswo.tesm.networking.payload.id.RequestReason;
import net.whgkswo.tesm.properties.BlockEntityHelper;
import net.whgkswo.tesm.properties.data.DoorData;

public class DoorDataC2SReceiver {
    public static void handle(BlockDataReq payload, ServerPlayNetworking.Context context){

        BlockEntity blockEntity = GlobalVariables.world.getBlockEntity(payload.blockPos());

        if(blockEntity instanceof DoorBlockEntity){
            // 데이터 가져오기
            DoorData doorData = BlockEntityHelper.getDoorData(payload.blockPos());

            // 클라이언트로 응답 전송
            ServerPlayNetworking.send((ServerPlayerEntity) GlobalVariables.player, new DoorDataRes(doorData));
        }else{
            ServerPlayNetworking.send((ServerPlayerEntity) GlobalVariables.player, new NullRes(RequestReason.CENTER_RAYCAST_BLOCK));
        }

    }
}
