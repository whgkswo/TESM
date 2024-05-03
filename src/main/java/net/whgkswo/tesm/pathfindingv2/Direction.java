package net.whgkswo.tesm.pathfindingv2;

public class Direction {
    private int directionX;

    public int getX() {
        return directionX;
    }

    public int getZ() {
        return directionZ;
    }

    private int directionZ;
    public Direction(int directionX, int directionZ){
        this.directionX = directionX;
        this.directionZ = directionZ;
    }
    public boolean isDiagonal(){
        return Math.abs(directionX) == Math.abs(directionZ);
    }
    public Direction getLeftDirection(){
        // 1,0 -> 0,-1
        // 0,1 -> 1,0
        // -1,0 -> 0,1
        // 0,-1 -> -1,0
        if(directionX != 0){
            return new Direction(directionZ, -directionX);
        }else{
            return new Direction(directionZ, directionX);
        }
    }
    public Direction getRightDirection(){
        if(directionX != 0){
            return new Direction(directionZ, directionX);
        }else{
            return new Direction(directionZ, -directionX);
        }
    }
}
