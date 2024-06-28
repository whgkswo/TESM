package net.whgkswo.tesm.pathfinding.v3;

public class ScanDataOfDirection {

    private boolean obstacleFound;
    private Boolean leftBlocked;
    private Boolean rightBlocked;

    public Boolean getLeftBlocked() {
        return leftBlocked;
    }

    public Boolean getRightBlocked() {
        return rightBlocked;
    }

    public ScanDataOfDirection(boolean obstacleFound, Boolean leftBlocked, Boolean rightBlocked) {
        this.obstacleFound = obstacleFound;
        this.leftBlocked = leftBlocked;
        this.rightBlocked = rightBlocked;
    }

    public ScanDataOfDirection() {
    }

    public boolean isObstacleFound() {
        return obstacleFound;
    }
}