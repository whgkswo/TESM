package net.whgkswo.tesm.networking.receivers;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.networking.payload.c2s_req.ConversationNbtReq;
import net.whgkswo.tesm.networking.payload.s2c_res.ConversationNbtRes;
import net.whgkswo.tesm.util.IEntityDataSaver;

public class ConversationNbtC2SReceiver {
    public static void handle(ConversationNbtReq payload, ServerPlayNetworking.Context context){

        // 엔티티 찾기
        int id = payload.entityId();
        Entity entity = GlobalVariables.world.getEntityById(id);

        if (entity != null) {
            // NBT 데이터 가져오기
            NbtCompound nbtCompound = ((IEntityDataSaver)entity).getPersistentData().getCompound("EntityData");
            boolean isInteractable = nbtCompound.getBoolean("interactable");
            String tempName = nbtCompound.getString("TempName");
            String name = nbtCompound.getString("Name");

            // 클라이언트에 응답 송신
            PacketByteBuf responseBuf = PacketByteBufs.create();
            responseBuf.writeBoolean(isInteractable);
            responseBuf.writeString(tempName);
            responseBuf.writeString(name);
            // TODO: 포팅
            //responseSender.sendPacket(ModMessages.GETNBT_BY_RAYCASTING_RESPONSE, responseBuf);

            // 클라이언트로 응답 전송
            ConversationNbtRes response = new ConversationNbtRes(tempName, name);
            ServerPlayNetworking.send(context.player(), response);
        }
    }
}
