package net.whgkswo.tesm.pathfinding.v2;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class DirectionSetter {

    public static ArrayList<PathfindDirection> setSearchDirections(BlockPos startPos, JumpPoint nextJumpPoint){
        PathfindDirection prevDirection = nextJumpPoint.getPrevDirection();
        // 첫 탐색인 경우
        if(nextJumpPoint.getBlockPos().equals(startPos)){
            return PathfindDirection.getAllDirections();
        }
        ArrayList<PathfindDirection> directions = new ArrayList<>();
        // 왼쪽이 막혔으면 왼쪽과 왼쪽 대각선 추가 탐색
        if(nextJumpPoint.isLeftBlocked()){
            if(prevDirection.isDiagonal()){
                PathfindDirection leftDirection = prevDirection.getLeftDirection();
                directions.add(leftDirection);
                directions.add(leftDirection.getLeftDiagDirection());
            }else{
                directions.add(prevDirection.getLeftDirection());
                directions.add(prevDirection.getLeftDiagDirection());
            }
        }
        // 가던 방향은 기본적으로 탐색
        directions.add(prevDirection);
        // 대각선 방향이었으면 왼쪽, 오른쪽 추가 탐색
        if(nextJumpPoint.getPrevDirection().isDiagonal()){
            directions.add(PathfindDirection.getDirectionByComponent(prevDirection.getX(), 0));
            directions.add(PathfindDirection.getDirectionByComponent(0, prevDirection.getZ()));
        }
        // 오른쪽이 막혔으면 오른쪽과 오른쪽 대각선 추가 탐색
        if(nextJumpPoint.isRightBlocked()){
            if(prevDirection.isDiagonal()){
                PathfindDirection rightDirection = prevDirection.getRightDirection();
                directions.add(rightDirection);
                directions.add(rightDirection.getLeftDiagDirection());
            }else{
                directions.add(prevDirection.getRightDirection());
                directions.add(prevDirection.getRightDiagDirection());
            }
        }
        return directions;
    }
}
