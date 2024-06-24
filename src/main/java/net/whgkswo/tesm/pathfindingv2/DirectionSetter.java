package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;

public class DirectionSetter {
    public static ArrayList<Direction> setSearchDirections(BlockPos startPos, JumpPoint nextJumpPoint){
        Direction prevDirection = nextJumpPoint.getPrevDirection();
        // 첫 탐색인 경우
        if(nextJumpPoint.getBlockPos().equals(startPos)){
            return new ArrayList<>(Arrays.asList(Direction.EAST, Direction.SOUTHEAST, Direction.SOUTH,
                    Direction.SOUTHWEST, Direction.WEST, Direction.NORTHWEST, Direction.NORTH,
                    Direction.NORTHEAST));
        }
        ArrayList<Direction> directions = new ArrayList<>();
        // 왼쪽이 막혔으면 왼쪽과 왼쪽 대각선 추가 탐색
        if(nextJumpPoint.isLeftBlocked()){
            if(prevDirection.isDiagonal()){
                Direction leftDirection = prevDirection.getLeftDirection();
                directions.add(leftDirection);
                directions.add(leftDirection.getLeftDiagForDiag());
            }else{
                directions.add(prevDirection.getLeftDirection());
                directions.add(prevDirection.getLeftDiagDirection());
            }
        }
        // 가던 방향은 기본적으로 탐색
        directions.add(prevDirection);
        // 대각선 방향이었으면 왼쪽, 오른쪽 추가 탐색
        if(nextJumpPoint.getPrevDirection().isDiagonal()){
            directions.add(Direction.getDirectionByComponent(prevDirection.getX(), 0));
            directions.add(Direction.getDirectionByComponent(0, prevDirection.getZ()));
        }
        // 오른쪽이 막혔으면 오른쪽과 오른쪽 대각선 추가 탐색
        if(nextJumpPoint.isRightBlocked()){
            if(prevDirection.isDiagonal()){
                Direction rightDirection = prevDirection.getRightDirection();
                directions.add(rightDirection);
                directions.add(rightDirection.getRightDiagForDiag());
            }else{
                directions.add(prevDirection.getRightDirection());
                directions.add(prevDirection.getRightDiagDirection());
            }
        }
        return directions;
    }
}
