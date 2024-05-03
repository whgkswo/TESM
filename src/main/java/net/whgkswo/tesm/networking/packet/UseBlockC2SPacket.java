package net.whgkswo.tesm.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.whgkswo.tesm.pathfindingv2.PathfindingManager;


public class UseBlockC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender){
        // 아래 코드들은 서버에서만 실행됨!
        BlockHitResult hitResult = buf.readBlockHitResult();
        //PathFinder.pathFindingStart(server.getOverworld().toServerWorld(), "인두리온", hitResult.getBlockPos().getX(), hitResult.getBlockPos().getY(),hitResult.getBlockPos().getZ());
        PathfindingManager.pathfindingStart(server.getOverworld().toServerWorld(), "인두리온", hitResult.getBlockPos());
    }
}
