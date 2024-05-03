package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class BlockStateTester {
    public static boolean isSolid(ServerWorld world, BlockPos blockPos){
        return world.getBlockState(blockPos).isSolidBlock(world, blockPos);
    }
    public static boolean isSteppable(ServerWorld world, BlockPos blockPos){
        // 해당 좌표가 단단하고
        if(isSolid(world, blockPos)){
            // 그 위 두 칸이 뚫렸으면
            if(!isSolid(world, blockPos.up(1)) && !isSolid(world, blockPos.up(2))  ){
                return true;
            }
        }
        return false;
    }
    public static boolean isReachable(ServerWorld world, BlockPos refPos, Direction direction){
        int cursorX = refPos.getX();
        int cursorY = refPos.getY();
        int cursorZ = refPos.getZ();
        // 탐색 방향으로 한 칸 앞을 보기
        cursorX += direction.getX();  cursorZ += direction.getZ();
        BlockPos nextPos = new BlockPos(cursorX, cursorY, cursorZ);

        if(isSolid(world, nextPos.up(2))){
            // 장애물
            return false;
        }
        if(!isSolid(world, nextPos.up(1)) && !isSolid(world, nextPos) && !isSolid(world, nextPos.down(1))){
            // 낭떠러지
            return false;
        }
        return true;
    }
}
