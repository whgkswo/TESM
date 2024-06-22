package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import static net.whgkswo.tesm.general.GlobalVariables.world;
import static net.whgkswo.tesm.pathfindingv2.LinearSearcher.moveOneBlock;


public class BlockStateTester {
    public static boolean isSolid(BlockPos blockPos){
        return world.getBlockState(blockPos).isSolidBlock(world, blockPos);
    }
    public static boolean isSteppable(ServerWorld world, BlockPos blockPos){
        // 해당 좌표가 단단하고
        if(isSolid(blockPos)){
            // 그 위 두 칸이 뚫렸으면
            if(!isSolid(blockPos.up(1)) && !isSolid(blockPos.up(2))  ){
                return true;
            }
        }
        return false;
    }
    public static boolean isReachable(BlockPos refPos, Direction direction){
        int cursorX = refPos.getX();
        int cursorY = refPos.getY();
        int cursorZ = refPos.getZ();
        // 탐색 방향으로 한 칸 앞을 보기
        cursorX += direction.getX();  cursorZ += direction.getZ();
        BlockPos nextPos = new BlockPos(cursorX, cursorY, cursorZ);

        if(isSolid(nextPos.up(2))){
            // 장애물
            return false;
        }
        if(!isSolid(nextPos.up(1)) && !isSolid(nextPos) && !isSolid(nextPos.down(1))){
            // 낭떠러지
            return false;
        }
        if(!world.getBlockState(nextPos).getFluidState().isEmpty()){
            // 액체
            return false;
        }
        // 전방에 장애물이 없었고 대각선 방향일 경우 추가 검사
        if(direction.isDiagonal()){
            // 한 칸 앞의 y좌표를 검사
            BlockPos nextTempPos = moveOneBlock(refPos, direction);
            int dy = nextTempPos.getY() - nextPos.getY();
            // 올라가는 칸의 경우 검사 기준 좌표를 한 칸 올려 줘야 함
            int offset = (dy == 1) ? 2:1;

            BlockPos xRefPos = new BlockPos(refPos.getX() + direction.getX(), refPos.getY(), refPos.getZ()).up(offset);
            BlockPos zRefPos = new BlockPos(refPos.getX(), refPos.getY(), refPos.getZ() + direction.getZ()).up(offset);
            boolean xBlocked = BlockStateTester.isSolid(xRefPos) || BlockStateTester.isSolid(xRefPos.up(1));
            boolean zBlocked = BlockStateTester.isSolid(zRefPos) || BlockStateTester.isSolid(zRefPos.up(1));
            return !xBlocked && !zBlocked;
        }
        return true;
    }
}
