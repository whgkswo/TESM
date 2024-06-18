package net.whgkswo.tesm.pathfindingv2;

public enum RelativeDirection {
    FRONT,
    FRONTRIGHT,
    RIGHT,
    BACKRIGHT,
    BACK,
    BACKLEFT,
    LEFT,
    FRONTLEFT
    ;
    public static RelativeDirection getRelativeDirection(int number){
        switch (number){
            case 0:
                return FRONT;
            case 1:
                return FRONTRIGHT;
            case 2:
                return RIGHT;
            case 3:
                return BACKRIGHT;
            case 4:
                return BACK;
            case 5:
                return BACKLEFT;
            case 6:
                return LEFT;
            case 7:
                return FRONTLEFT;
            default:
                return null;
        }
    }
}
