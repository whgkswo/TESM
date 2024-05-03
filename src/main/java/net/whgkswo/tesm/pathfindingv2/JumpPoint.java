package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.util.math.BlockPos;

public class JumpPoint {
    int xPos;   int yPos;   int zPos;
    int hValue; int gValue; int fValue;
    boolean leftBlocked;    boolean rightBlocked;
    public JumpPoint(BlockPos blockPos, int hValue, int gValue, int fValue, boolean leftBlocked, boolean rightBlocked){
        xPos = blockPos.getX();
        yPos = blockPos.getY();
        zPos = blockPos.getZ();
        this.hValue = hValue;   this.gValue = gValue; this.fValue = fValue;
        this.leftBlocked = leftBlocked;
        this.rightBlocked = rightBlocked;
    }
}
