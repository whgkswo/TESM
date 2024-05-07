package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.util.math.BlockPos;

public class BlockPosManager {
    public static int getRoughDistance(BlockPos pos1, BlockPos pos2){
        return Math.abs(pos2.getX() - pos1.getX())
                + Math.abs(pos2.getY() - pos1.getY())
                + Math.abs(pos2.getZ() - pos1.getZ());
    }
}
