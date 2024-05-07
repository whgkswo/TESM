package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class TriangleTest {
    private boolean adjacentTestResult;
    private boolean oppositeTestResult;

    public boolean adjacentTestPassed() {
        return adjacentTestResult;
    }

    public boolean oppositeTestPassed() {
        return oppositeTestResult;
    }

    public boolean hypotenuseTestPassed() {
        return hypotenuseTestResult;
    }

    private boolean hypotenuseTestResult;
    public TriangleTest(ServerWorld world, BlockPos refPos, Direction direction, TestDirection testDirection){
        triangleTest(world, refPos, direction, testDirection);
    }
    public void triangleTest(ServerWorld world, BlockPos refPos, Direction refDirection, TestDirection testDirection){
        Direction perpendicularDirection;
        if(testDirection == TestDirection.LEFT){
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
