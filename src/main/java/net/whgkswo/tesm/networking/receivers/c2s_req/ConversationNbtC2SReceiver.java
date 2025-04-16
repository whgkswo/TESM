package net.whgkswo.tesm.networking.receivers.c2s_req;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.networking.payload.data.c2s_req.ConversationNbtReq;
import net.whgkswo.tesm.networking.payload.data.s2c_res.ConversationNbtRes;
import net.whgkswo.tesm.nbt.IEntityDataSaver;

public class ConversationNbtC2SReceiver {
    public static void handle(ConversationNbtReq payload, ServerPlayNetworking.Context context){

        // 엔티티 찾기
        int id = payload.entityId();
        Entity entity = GlobalVariables.world.getEntityById(id);

        if (entity != null) {
            // NBT 데이터 가져오기
            NbtCompound nbtCompound = ((IEntityDataSaver)entity).getPersistentData().getCompound("EntityData");
            boolean isInteractable = nbtCompound.getBoolean("Interactable");
            String tempName = nbtCompound.getString("TempName");
            String name = nbtCompound.getString("Name");
            String engName = nbtCompound.getString("EngName");

            // 클라이언트로 응답 전송
            ConversationNbtRes response = new ConversationNbtRes(engName, tempName, name, id);
            ServerPlayNetworking.send(context.player(), response);
        }
    }
}
