package net.whgkswo.tesm.pathfinding.v2;

import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.helpers.BlockPosUtil;

public class TriangleTestResult {
    private boolean adjacentTestResult;
    private boolean oppositeTestResult;

    public boolean getAdjacentTestResult() {
        return adjacentTestResult;
    }

    public boolean getOppositeTestResult() {
        return oppositeTestResult;
    }

    public boolean getHypotenuseTestResult() {
        return hypotenuseTestResult;
    }

    private boolean hypotenuseTestResult;
    public TriangleTestResult(BlockPos refPos, PathfindDirection direction, RelativeDirection relativeDirection){
        triangleTest(refPos, direction, relativeDirection);
    }
    private void triangleTest(BlockPos refPos, PathfindDirection refDirection, RelativeDirection testDirection){
        PathfindDirection perpendicularDirection;
        if(testDirection == RelativeDirection.LEFT){
            perpendicularDirection = refDirection.getLeftDirection();
        }else{
            perpendicularDirection = refDirection.getRightDirection();
        }
        // 삼각 검사 - 밑변
        adjacentTestResult = BlockPosUtil.isReachable(refPos, perpendicularDirection);
        if(adjacentTestResult){
            // 삼각 검사 - 옆변
            BlockPos sidePos = BlockPosUtil.getNextBlock(refPos, perpendicularDirection);
            oppositeTestResult = BlockPosUtil.isReachable(sidePos, refDirection);
            // 삼각 검사 - 대각선
            PathfindDirection hypotenuseDirection = PathfindDirection.getDirectionByComponent(refDirection.getX() + perpendicularDirection.getX(),
                    refDirection.getZ() + perpendicularDirection.getZ());
            hypotenuseTestResult = BlockPosUtil.isReachable(refPos,hypotenuseDirection);
        }
    }
}
