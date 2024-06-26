package net.whgkswo.tesm.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.whgkswo.tesm.pathfinding.v2.Direction;
import net.whgkswo.tesm.tags.BlockTags;

import static net.whgkswo.tesm.general.GlobalVariables.world;

public class BlockPosUtil {
    public static double getRoughDistance(BlockPos pos1, BlockPos pos2){
        int farGap = Math.max(Math.abs(pos1.getX()- pos2.getX()),Math.abs(pos1.getZ()- pos2.getZ()));
        int closeGap = Math.min(Math.abs(pos1.getX()- pos2.getX()),Math.abs(pos1.getZ()- pos2.getZ()));
        return 1.4*(closeGap) + farGap-closeGap;
    }
    public static BlockPos getCopyPos(BlockPos targetPos){
        return new BlockPos(targetPos.getX(), targetPos.getY(), targetPos.getZ());
    }
    public static BlockPos getNextBlock(BlockPos refPos, Direction direction) {
        // 탐색 방향으로 한 칸 가기
        int cursorX = refPos.getX() + direction.getX();
        int cursorY = refPos.getY();
        int cursorZ = refPos.getZ() + direction.getZ();

        BlockPos nextPos = new BlockPos(cursorX, cursorY, cursorZ);
        if (BlockPosUtil.isSolid(nextPos.up(1))) {
            // 올라가기
            cursorY++;
        } else if (!BlockPosUtil.isSolid(nextPos)) {
            // 내려가기
            cursorY--;
        }
        return new BlockPos(cursorX, cursorY, cursorZ);
    }
    public static boolean isSolid(BlockPos blockPos){
        VoxelShape shape = world.getBlockState(blockPos).getCollisionShape(world,blockPos);
        return !shape.isEmpty();
    }
    public static boolean isSteppable(BlockPos blockPos){
        // 해당 좌표가 단단하고
        if(isSolid(blockPos)){
            // 그 위 두 칸이 뚫렸으면
            if(!isObstacle(blockPos.up(1)) && !isObstacle(blockPos.up(2))  ){
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

        if(isTrapBlock(nextPos.up()) || isObstacle(nextPos.up(2))){
            // 장애물
            return false;
        }
        if(!isSolid(nextPos.up(1)) && !isSolid(nextPos) && !isSolid(nextPos.down(1))){
            // 낭떠러지
            return false;
        }
        if(isFluid(nextPos) || isFluid(nextPos.up())){
            // 액체
            return false;
        }
        // 전방에 장애물이 없었고 대각선 방향일 경우 추가 검사
        if(direction.isDiagonal()){
            // 한 칸 앞의 y좌표를 검사
            BlockPos nextTempPos = getNextBlock(refPos, direction);
            int dy = nextTempPos.getY() - nextPos.getY();
            // 올라가는 칸의 경우 검사 기준 좌표를 한 칸 올려 줘야 함
            int offset = (dy == 1) ? 2:1;

            BlockPos xRefPos = new BlockPos(refPos.getX() + direction.getX(), refPos.getY(), refPos.getZ()).up(offset);
            BlockPos zRefPos = new BlockPos(refPos.getX(), refPos.getY(), refPos.getZ() + direction.getZ()).up(offset);
            boolean xBlocked = BlockPosUtil.isObstacle(xRefPos) || BlockPosUtil.isObstacle(xRefPos.up(1));
            boolean zBlocked = BlockPosUtil.isObstacle(zRefPos) || BlockPosUtil.isObstacle(zRefPos.up(1));
            return !xBlocked && !zBlocked;
        }
        return true;
    }
    public static boolean isObstacle(BlockPos blockPos){
        return isSolid(blockPos) || isTrapBlock(blockPos);
    }
    public static double getBlockHeight(BlockPos blockPos){
        return world.getBlockState(blockPos).getCollisionShape(world, blockPos).getMax(net.minecraft.util.math.Direction.Axis.Y);
    }
    public static boolean isTrapBlock(BlockPos blockPos){
        return world.getBlockState(blockPos).isIn(BlockTags.TRAP_BLOCK);
    }
    public static boolean isFluid(BlockPos blockPos){
        return !world.getBlockState(blockPos).getFluidState().isEmpty();
    }
}