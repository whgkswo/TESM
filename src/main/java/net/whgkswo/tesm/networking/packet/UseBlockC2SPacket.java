package net.whgkswo.tesm.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.exceptions.EntityNotFoundExeption;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.pathfinding.v2.PathfinderV2;
import net.whgkswo.tesm.util.BlockPosUtil;
import net.whgkswo.tesm.pathfinding.v3.PathfinderV3;
/*import net.whgkswo.tesm.pathfindingv2.PathfindingManager;*/


public class UseBlockC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender){
        // 아래 코드들은 서버에서만 실행됨!
        BlockHitResult hitResult = buf.readBlockHitResult();

        BlockPos hitResultPos = hitResult.getBlockPos();

        // 목적지 좌표의 블럭이 높이가 낮으면 기준을 한 칸 내리기
        if(BlockPosUtil.isLowBlock(hitResultPos)){
            hitResultPos = hitResultPos.down();
        }
        if(!BlockPosUtil.isSteppable(hitResultPos)){
            player.sendMessage(Text.literal(String.format("(%s) 좌표는 엔티티가 딛고 설 수 없습니다.",
                    hitResultPos.toShortString())));
            return;
        }
        try{
            /*new PathfinderV2("인두리온", hitResultPos);*/
            new PathfinderV3("인두리온", hitResultPos);
        }catch (EntityNotFoundExeption e){
            // 아무것도 안함
        }
    }
}
