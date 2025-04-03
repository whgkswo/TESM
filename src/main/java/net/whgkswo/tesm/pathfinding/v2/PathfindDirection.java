package net.whgkswo.tesm.pathfinding.v2;

import java.util.ArrayList;
import java.util.Arrays;

public enum PathfindDirection {
    EAST(0,1,0),
    SOUTHEAST(1,1,1),
    SOUTH(2,0,1),
    SOUTHWEST(3,-1,1),
    WEST(4,-1,0),
    NORTHWEST(5,-1,-1),
    NORTH(6,0,-1),
    NORTHEAST(7,1,-1)
    ;
    private int number;
    private int directionX; private int directionZ;
    public static ArrayList<PathfindDirection> getAllDirections(){
        return new ArrayList<>(Arrays.asList(PathfindDirection.EAST, PathfindDirection.SOUTHEAST, PathfindDirection.SOUTH,
                PathfindDirection.SOUTHWEST, PathfindDirection.WEST, PathfindDirection.NORTHWEST, PathfindDirection.NORTH, PathfindDirection.NORTHEAST));
    }
    public static PathfindDirection getDirectionByNumber(int no){
        return switch (no % 8) {
            case 0 -> EAST;
            case 1 -> SOUTHEAST;
            case 2 -> SOUTH;
            case 3 -> SOUTHWEST;
            case 4 -> WEST;
            case 5 -> NORTHWEST;
            case 6 -> NORTH;
            case 7 -> NORTHEAST;
            default -> null;
        };
    }
    public static PathfindDirection getDirectionByComponent(int directionX, int directionZ){
        for(PathfindDirection dir : PathfindDirection.values()){
            if(directionX == dir.directionX && directionZ == dir.directionZ){
                return dir;
            }
        }
        return null;
    }
    PathfindDirection(int number, int directionX, int directionZ) {
        this.number = number;
        this.directionX = directionX;
        this.directionZ = directionZ;
    }
    public int getX(){
        return directionX;
    }
    public int getZ(){
        return directionZ;
    }
    public PathfindDirection getLeftDirection(){
        return getDirectionByNumber(number + 6);
    }
    public PathfindDirection getRightDirection(){
        return getDirectionByNumber(number + 2);
    }
    public PathfindDirection getLeftDiagDirection(){
        return getDirectionByNumber(number + 7);
    }
    public PathfindDirection getRightDiagDirection(){
        return getDirectionByNumber(number + 1);
    }
    public boolean isDiagonal(){
        return Math.abs(directionX) == Math.abs(directionZ);
    }
    public int getNumber(){
        return number;
    }
    public RelativeDirection getRelativeDirection(PathfindDirection targetDirection){
        int refNo = getNumber();
        int targetNo = targetDirection.getNumber();
        int index = (targetNo - refNo) % 8;
        if(index < 0){
            index += 8;
        }
        return RelativeDirection.getRelativeDirection(index);
    }
}
