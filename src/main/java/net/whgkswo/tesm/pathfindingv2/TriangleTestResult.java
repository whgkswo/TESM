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
    public TriangleTestResult(ServerWorld world, BlockPos refPos, Direction direction, RelativeDirection relativeDirection){
        triangleTest(world, refPos, direction, relativeDirection);
    }
    public void triangleTest(ServerWorld world, BlockPos refPos, Direction refDirection, RelativeDirection relativeDirection){
        Direction perpendicularDirection;
        if(relativeDirection == RelativeDirection.LEFT){
            perpendicularDirection = refDirection.getLeftDirection();
        }else{
            perpendicularDirection = refDirection.getRightDirection();
        }
        // 삼각 검사 - 밑변
        adjacentTestResult = BlockStateTester.isReachable(world, refPos, perpendicularDirection);
        if(adjacentTestResult){
            // 삼각 검사 - 옆변
            BlockPos sidePos = LinearSearcher.moveOneBlock(world, refPos, perpendicularDirection);
            oppositeTestResult = BlockStateTester.isReachable(world, sidePos, refDirection);
            // 삼각 검사 - 대각선
            Direction hypotenuseDirection = Direction.getDirectionByComponent(refDirection.getX() + perpendicularDirection.getX(),
                    refDirection.getZ() + perpendicularDirection.getZ());
            hypotenuseTestResult = BlockStateTester.isReachable(world, refPos,hypotenuseDirection);
        }
    }
}
