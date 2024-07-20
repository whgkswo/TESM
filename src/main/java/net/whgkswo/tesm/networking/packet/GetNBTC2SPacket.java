package net.whgkswo.tesm.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.whgkswo.tesm.networking.ModMessages;
import net.whgkswo.tesm.util.IEntityDataSaver;

public class GetNBTC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        // 아래 코드들은 서버에서만 실행됨!
        Entity targetEntity = player.getWorld().getEntityById(buf.readInt());

        try{
            NbtCompound nbtCompound = ((IEntityDataSaver)targetEntity).getPersistentData().getCompound("EntityData");

            boolean isInteractable = nbtCompound.getBoolean("interactable");
            String tempName = nbtCompound.getString("TempName");
            String name = nbtCompound.getString("Name");

            // 클라이언트에 응답 송신
            PacketByteBuf responseBuf = PacketByteBufs.create();
            responseBuf.writeBoolean(isInteractable);
            responseBuf.writeString(tempName);
            responseBuf.writeString(name);
            responseSender.sendPacket(ModMessages.GETNBT_RESPONSE_ID, responseBuf);

        }catch (NullPointerException e){
            // 아무것도 안함
        }

    }
}

