package net.whgkswo.tesm.pathfinding.v2;

import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.util.BlockPosUtil;

public class JumpPoint {
    private BlockPos largeRefPos;
    private BlockPos blockPos;
    private PathfindDirection prevDirection;
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

    public PathfindDirection getPrevDirection() {
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

    public JumpPoint(BlockPos largeRefPos, BlockPos blockPos, PathfindDirection prevDirection, BlockPos destPos, int hValue, boolean leftBlocked, boolean rightBlocked){
        this.largeRefPos = BlockPosUtil.getCopyPos(largeRefPos);
        this.blockPos = BlockPosUtil.getCopyPos(blockPos);
        this.prevDirection = prevDirection;
        this.hValue = hValue;
        int gValue = (int)(10* BlockPosUtil.getRoughDistance(blockPos, destPos));
        this.fValue = hValue + gValue;
        this.leftBlocked = leftBlocked;
        this.rightBlocked = rightBlocked;
    }
}
