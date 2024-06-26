package net.whgkswo.tesm.pathfinding.v2;

public class JumpPointTestResult {
    private final boolean leftBlocked;
    private final boolean rightBlocked;

    public JumpPointTestResult(boolean leftBlocked, boolean rightBlocked) {
        this.leftBlocked = leftBlocked;
        this.rightBlocked = rightBlocked;
    }

    public boolean isLeftBlocked() {
        return leftBlocked;
    }

    public boolean isRightBlocked() {
        return rightBlocked;
    }
}
