package net.whgkswo.tesm.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.whgkswo.tesm.pathfindingv2.Pathfinder;
/*import net.whgkswo.tesm.pathfindingv2.PathfindingManager;*/


public class UseBlockC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender){
        // 아래 코드들은 서버에서만 실행됨!
        BlockHitResult hitResult = buf.readBlockHitResult();
        ServerWorld world = server.getOverworld().toServerWorld();

        new Pathfinder(world, "인두리온", hitResult.getBlockPos());
    }
}
