package net.whgkswo.tesm.pathfinding.v3;

public class ScanDataOfDirection {

    public boolean obstacleFound;
    public Boolean leftBlocked;
    public Boolean rightBlocked;

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

    public void setobstacleFound(boolean obstacleFound) {
        this.obstacleFound = obstacleFound;
    }

    public void setleftBlocked(Boolean leftBlocked) {
        this.leftBlocked = leftBlocked;
    }

    public void setrightBlocked(Boolean rightBlocked) {
        this.rightBlocked = rightBlocked;
    }
}