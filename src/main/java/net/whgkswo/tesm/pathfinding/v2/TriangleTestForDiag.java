package net.whgkswo.tesm.pathfinding.v2;

import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.util.BlockPosUtil;

public class TriangleTestForDiag {
    private boolean refPosToNeighborPos;
    private boolean nextPosToNeighborPos;

    public boolean isRefPosToNeighborPos() {
        return refPosToNeighborPos;
    }

    public boolean isNextPosToNeighborPos() {
        return nextPosToNeighborPos;
    }
    public TriangleTestForDiag(BlockPos refPos, BlockPos nextPos, Direction direction, RelativeDirection testDirection){
        triangleTest(refPos, nextPos, direction, testDirection);
    }
    private void triangleTest(BlockPos refPos, BlockPos nextPos, Direction direction, RelativeDirection testDirection){

        Direction refPosToNeighborDirection =
                testDirection == RelativeDirection.LEFT ? direction.getLeftDiagDirection() : direction.getRightDiagDirection();
        refPosToNeighborPos = BlockPosUtil.isReachable(refPos, refPosToNeighborDirection);

        Direction nextPosToNeighborDirection =
                testDirection == RelativeDirection.LEFT ? refPosToNeighborDirection.getLeftDirection() : refPosToNeighborDirection.getRightDirection();
        nextPosToNeighborPos = BlockPosUtil.isReachable(nextPos, nextPosToNeighborDirection);
    }
}
