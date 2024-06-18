package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.util.math.BlockPos;

public class JumpPoint {
    private BlockPos largeRefPos;
    private BlockPos blockPos;
    private Direction prevDirection;
    private int fValue;
    private int hValue;
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

    public int getHValue() {
        return hValue;
    }

    public BlockPos getLargeRefPos() {
        return largeRefPos;
    }

    public JumpPoint(BlockPos largeRefPos, BlockPos blockPos, Direction prevDirection, BlockPos destPos, int hValue, boolean leftBlocked, boolean rightBlocked){
        this.largeRefPos = BlockPosManager.getCopyPos(largeRefPos);
        this.blockPos = BlockPosManager.getCopyPos(blockPos);
        this.prevDirection = prevDirection;
        this.hValue = hValue;
        int gValue = (int)(10*BlockPosManager.getRoughDistance(blockPos, destPos));
        this.fValue = hValue + gValue;
        this.leftBlocked = leftBlocked;
        this.rightBlocked = rightBlocked;
    }
}
