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

public class GetNbtByConversationC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        int id = buf.readInt();
        Entity entity = player.getWorld().getEntityById(id);
        NbtCompound nbtCompound = ((IEntityDataSaver)entity).getPersistentData().getCompound("EntityData");

        String tempName = nbtCompound.getString("TempName");
        String name = nbtCompound.getString("Name");

        // 클라이언트에 응답 송신
        PacketByteBuf responseBuf = PacketByteBufs.create();
        responseBuf.writeInt(id);
        responseBuf.writeString(tempName);
        responseBuf.writeString(name);
        responseSender.sendPacket(ModMessages.GETNBT_BY_CONVERSATION_RESPONSE, responseBuf);
    }
}
