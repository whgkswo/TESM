package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;

public class DirectionSetter {
    public static ArrayList<Direction> setSearchDirections(BlockPos startPos, JumpPoint nextJumpPoint){
        // 첫 탐색인 경우
        if(nextJumpPoint.getBlockPos().equals(startPos)){
            return new ArrayList<>(Arrays.asList(Direction.EAST, Direction.SOUTHEAST, Direction.SOUTH,
                    Direction.SOUTHWEST, Direction.WEST, Direction.NORTHWEST, Direction.NORTH,
                    Direction.NORTHEAST));
        }
        ArrayList<Direction> directions = new ArrayList<>();
        // 왼쪽이 막혔으면 왼쪽 추가 탐색
        if(nextJumpPoint.isLeftBlocked()){
            directions.add(nextJumpPoint.getPrevDirection().getLeftDirection());
        }
        // 가던 방향은 기본적으로 탐색
        directions.add(nextJumpPoint.getPrevDirection());
        // 오른쪽이 막혔으면 오른쪽 추가 탐색
        if(nextJumpPoint.isRightBlocked()){
            directions.add(nextJumpPoint.getPrevDirection().getRightDirection());
        }
        return directions;
    }
}
