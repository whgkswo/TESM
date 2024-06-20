package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

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
    public TriangleTestResult(BlockPos refPos, Direction direction, RelativeDirection relativeDirection){
        triangleTest(refPos, direction, relativeDirection);
    }
    public void triangleTest(BlockPos refPos, Direction refDirection, RelativeDirection relativeDirection){
        Direction perpendicularDirection;
        if(relativeDirection == RelativeDirection.LEFT){
            perpendicularDirection = refDirection.getLeftDirection();
        }else{
            perpendicularDirection = refDirection.getRightDirection();
        }
        // 삼각 검사 - 밑변
        adjacentTestResult = BlockStateTester.isReachable(refPos, perpendicularDirection);
        if(adjacentTestResult){
            // 삼각 검사 - 옆변
            BlockPos sidePos = LinearSearcher.moveOneBlock(refPos, perpendicularDirection);
            oppositeTestResult = BlockStateTester.isReachable(sidePos, refDirection);
            // 삼각 검사 - 대각선
            Direction hypotenuseDirection = Direction.getDirectionByComponent(refDirection.getX() + perpendicularDirection.getX(),
                    refDirection.getZ() + perpendicularDirection.getZ());
            hypotenuseTestResult = BlockStateTester.isReachable(refPos,hypotenuseDirection);
        }
    }
}
