package net.whgkswo.tesm.pathfinding.v3;

import net.whgkswo.tesm.pathfinding.v2.Direction;

public class NavMeshDataOfDirection {
    private Direction direction;

    private boolean obstacleFound;
    private boolean leftBlocked;
    private boolean rightBlocked;

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
