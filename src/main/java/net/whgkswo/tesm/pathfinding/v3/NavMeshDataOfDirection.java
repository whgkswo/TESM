package net.whgkswo.tesm.pathfinding.v3;

import net.whgkswo.tesm.pathfinding.v2.Direction;

public class NavMeshDataOfDirection {

    private boolean obstacleFound;
    private Boolean leftBlocked;
    private Boolean rightBlocked;

    public Boolean getLeftBlocked() {
        return leftBlocked;
    }

    public Boolean getRightBlocked() {
        return rightBlocked;
    }

    public NavMeshDataOfDirection(boolean obstacleFound, Boolean leftBlocked, Boolean rightBlocked) {
        this.obstacleFound = obstacleFound;
        this.leftBlocked = leftBlocked;
        this.rightBlocked = rightBlocked;
    }

    public boolean isObstacleFound() {
        return obstacleFound;
    }

    public void setObstacleFound(boolean obstacleFound) {
        this.obstacleFound = obstacleFound;
    }

    public void setLeftBlocked(boolean leftBlocked) {
        this.leftBlocked = leftBlocked;
    }

    public void setRightBlocked(boolean rightBlocked) {
        this.rightBlocked = rightBlocked;
    }

    public boolean isLeftBlocked() {
        return leftBlocked;
    }

    public boolean isRightBlocked() {
        return rightBlocked;
    }
}