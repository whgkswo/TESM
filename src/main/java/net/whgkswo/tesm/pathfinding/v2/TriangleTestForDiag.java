package net.whgkswo.tesm.pathfinding.v2;

import net.minecraft.util.math.BlockPos;

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
        refPosToNeighborPos = BlockStateHelper.isReachable(refPos, refPosToNeighborDirection);

        Direction nextPosToNeighborDirection =
                testDirection == RelativeDirection.LEFT ? refPosToNeighborDirection.getLeftDirection() : refPosToNeighborDirection.getRightDirection();
        nextPosToNeighborPos = BlockStateHelper.isReachable(nextPos, nextPosToNeighborDirection);
    }
}
