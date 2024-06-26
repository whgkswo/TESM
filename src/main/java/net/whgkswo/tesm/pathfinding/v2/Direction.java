package net.whgkswo.tesm.pathfinding.v2;

import java.util.ArrayList;
import java.util.Arrays;

public enum Direction {
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
    public static ArrayList<Direction> getAllDirections(){
        return new ArrayList<>(Arrays.asList(Direction.EAST, Direction.SOUTHEAST, Direction.SOUTH,
                Direction.SOUTHWEST, Direction.WEST, Direction.NORTHWEST, Direction.NORTH, Direction.NORTHEAST));
    }
    public static Direction getDirectionByNumber(int no){
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
    public static Direction getDirectionByComponent(int directionX, int directionZ){
        for(Direction dir : Direction.values()){
            if(directionX == dir.directionX && directionZ == dir.directionZ){
                return dir;
            }
        }
        return null;
    }
    Direction(int number, int directionX, int directionZ) {
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
    public Direction getLeftDirection(){
        return getDirectionByNumber(number + 6);
    }
    public Direction getRightDirection(){
        return getDirectionByNumber(number + 2);
    }
    public Direction getLeftDiagDirection(){
        return getDirectionByNumber(number + 7);
    }
    public Direction getRightDiagDirection(){
        return getDirectionByNumber(number + 1);
    }
    public boolean isDiagonal(){
        return Math.abs(directionX) == Math.abs(directionZ);
    }
    public int getNumber(){
        return number;
    }
    public RelativeDirection getRelativeDirection(Direction targetDirection){
        int refNo = getNumber();
        int targetNo = targetDirection.getNumber();
        int index = (targetNo - refNo) % 8;
        if(index < 0){
            index += 8;
        }
        return RelativeDirection.getRelativeDirection(index);
    }
}
