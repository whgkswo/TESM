package net.whgkswo.tesm.pathfindingv2;

public enum Directions {
    EAST(1,0),
    SOUTHEAST(1,1),
    SOUTH(0,1),
    SOUTHWEST(-1,1),
    WEST(-1,0),
    NORTHWEST(-1,-1),
    NORTH(0,-1),
    NORTHEAST(1,-1)
    ;

    private final Direction direction;

    public static Directions getDirectionByNumber(int no){
        switch(no){
            case 1:
                return EAST;
            case 2:
                return SOUTHEAST;
            case 3:
                return SOUTH;
            case 4:
                return SOUTHWEST;
            case 5:
                return WEST;
            case 6:
                return NORTHWEST;
            case 7:
                return NORTH;
            case 8:
                return NORTHEAST;
            default:
                return null;
        }
    }
    public Direction getDirection(){
        return direction;
    }

    Directions(int directionX, int directionZ) {
        direction = new Direction(directionX, directionZ);
    }
}
