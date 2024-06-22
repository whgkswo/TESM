package net.whgkswo.tesm.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.whgkswo.tesm.exceptions.EntityNotFoundExeption;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.pathfindingv2.Pathfinder;

import static net.whgkswo.tesm.general.GlobalVariables.world;
/*import net.whgkswo.tesm.pathfindingv2.PathfindingManager;*/


public class UseBlockC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender){
        // 아래 코드들은 서버에서만 실행됨!
        BlockHitResult hitResult = buf.readBlockHitResult();

        BlockPos hitResultPos = hitResult.getBlockPos();

        if(!world.getBlockState(hitResultPos).isSolidBlock(world, hitResultPos)){
            hitResultPos = hitResultPos.down(1);
        }
        try{
            new Pathfinder("인두리온", hitResultPos);
        }catch (EntityNotFoundExeption e){

        }
    }
}
