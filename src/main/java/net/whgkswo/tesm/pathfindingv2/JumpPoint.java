package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.util.math.BlockPos;

public class JumpPoint {
    int xPos;   int yPos;   int zPos;
    int fValue;
    boolean leftBlocked;    boolean rightBlocked;
    public JumpPoint(BlockPos refPos, BlockPos endPos, int hValue, boolean leftBlocked, boolean rightBlocked){
        xPos = refPos.getX();
        yPos = refPos.getY();
        zPos = refPos.getZ();
        int gValue = 10 * BlockPosManager.getRoughDistance(refPos, endPos);
        this.fValue = hValue + gValue;
        this.leftBlocked = leftBlocked;
        this.rightBlocked = rightBlocked;
    }
}
