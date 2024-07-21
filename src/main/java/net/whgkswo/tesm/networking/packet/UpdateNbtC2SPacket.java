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

public class UpdateNbtC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        int id = buf.readInt();
        Entity entity = player.getWorld().getEntityById(id);

        ((IEntityDataSaver)entity).getPersistentData().getCompound("EntityData").putString("TempName", "");
    }
}
