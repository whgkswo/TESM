package net.whgkswo.tesm.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.whgkswo.tesm.nbt.NBTManager;

public class UpdateNbtC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        int id = buf.readInt();
        Entity entity = player.getWorld().getEntityById(id);

        NBTManager.getCustomNbt(entity).putString("TempName", "");
    }
}
