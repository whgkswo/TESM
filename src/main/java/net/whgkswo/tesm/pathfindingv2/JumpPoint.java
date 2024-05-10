package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.util.math.BlockPos;

public class JumpPoint {
    private BlockPos blockPos;
    private Direction prevDirection;
    private int fValue;
    private boolean leftBlocked;

    public int getFValue() {
        return fValue;
    }

    public boolean isLeftBlocked() {
        return leftBlocked;
    }

    public boolean isRightBlocked() {
        return rightBlocked;
    }

    public Direction getPrevDirection() {
        return prevDirection;
    }

    private boolean rightBlocked;

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public JumpPoint(BlockPos blockPos, Direction prevDirection,BlockPos destPos, int hValue, boolean leftBlocked, boolean rightBlocked){
        this.blockPos = blockPos;
        this.prevDirection = prevDirection;
        int gValue = 10 * BlockPosManager.getRoughDistance(blockPos, destPos);
        this.fValue = hValue + gValue;
        this.leftBlocked = leftBlocked;
        this.rightBlocked = rightBlocked;
    }
}
