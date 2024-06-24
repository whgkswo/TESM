package net.whgkswo.tesm.pathfindingv2;

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
    private int directionNo;
    private int directionX; private int directionZ;

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
    Direction(int directionNo,int directionX, int directionZ) {
        this.directionNo = directionNo;
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
        /*if(directionX == 0){
            return getDirectionByComponent(directionZ, directionX);
        }else{
            return getDirectionByComponent(directionZ, -directionX);
        }*/
        return getDirectionByNumber(directionNo + 6);
    }
    public Direction getRightDirection(){
        /*if(directionX == 0){
            return getDirectionByComponent(-directionZ, directionX);
        }else{
            return getDirectionByComponent(directionZ, directionX);
        }*/
        return getDirectionByNumber(directionNo + 2);
    }
    public Direction getLeftDiagDirection(){
        Direction leftDirection = getLeftDirection();
        return getDirectionByComponent(directionX + leftDirection.getX(), directionZ + leftDirection.getZ());
    }
    public Direction getRightDiagDirection(){
        Direction rightDirection = getRightDirection();
        return getDirectionByComponent(directionX + rightDirection.getX(), directionZ + rightDirection.getZ());
    }
    public Direction getLeftDiagForDiag(){
        // x성분과 z성분이 다르면 z방향이 왼쪽
        if(directionX + directionZ == 0){
            return getDirectionByComponent(0, directionZ);
        }else{ // 같으면 x방향이 왼쪽
            return getDirectionByComponent(directionX, 0);
        }
    }
    public Direction getRightDiagForDiag(){
        // x성분과 z성분이 다르면 x방향이 오른쪽
        if(directionX + directionZ == 0){
            return getDirectionByComponent(directionX, 0);
        }else{ // 같으면 z방향이 오른쪽
            return getDirectionByComponent(0, directionZ);
        }
    }
    public boolean isDiagonal(){
        return Math.abs(directionX) == Math.abs(directionZ);
    }
    public int getDirectionNo(){
        return directionNo;
    }
    public RelativeDirection getRelativeDirection(Direction targetDirection){
        int refNo = getDirectionNo();
        int targetNo = targetDirection.getDirectionNo();
        int index = (targetNo - refNo) % 8;
        if(index < 0){
            index += 8;
        }
        return RelativeDirection.getRelativeDirection(index);
    }
}
