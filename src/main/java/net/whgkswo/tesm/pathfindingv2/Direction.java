package net.whgkswo.tesm.pathfindingv2;

public enum Direction {
    EAST(1,0),
    SOUTHEAST(1,1),
    SOUTH(0,1),
    SOUTHWEST(-1,1),
    WEST(-1,0),
    NORTHWEST(-1,-1),
    NORTH(0,-1),
    NORTHEAST(1,-1)
    ;
    private int directionX; private int directionZ;

    public static Direction getDirectionByNumber(int no){
        return switch (no) {
            case 1 -> EAST;
            case 2 -> SOUTHEAST;
            case 3 -> SOUTH;
            case 4 -> SOUTHWEST;
            case 5 -> WEST;
            case 6 -> NORTHWEST;
            case 7 -> NORTH;
            case 8 -> NORTHEAST;
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
    Direction(int directionX, int directionZ) {
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
        if(directionX != 0){
            return Direction.getDirectionByComponent(directionZ, -directionX);
        }else{
            return Direction.getDirectionByComponent(directionZ, directionX);
        }
    }
    public Direction getRightDirection(){
        if(directionX != 0){
            return getDirectionByComponent(directionZ, directionX);
        }else{
            return getDirectionByComponent(directionZ, -directionX);
        }
    }
    public boolean isDiagonal(){
        return Math.abs(directionX) == Math.abs(directionZ);
    }
}
