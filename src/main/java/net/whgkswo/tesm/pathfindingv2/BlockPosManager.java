package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.util.math.BlockPos;

public class BlockPosManager {
    public static double getRoughDistance(BlockPos pos1, BlockPos pos2){
        int farGap = Math.max(Math.abs(pos1.getX()- pos2.getX()),Math.abs(pos1.getZ()- pos2.getZ()));
        int closeGap = Math.min(Math.abs(pos1.getX()- pos2.getX()),Math.abs(pos1.getZ()- pos2.getZ()));
        return 1.4*(closeGap) + farGap-closeGap;
    }
}
