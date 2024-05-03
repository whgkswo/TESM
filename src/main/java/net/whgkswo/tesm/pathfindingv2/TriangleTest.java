package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class TriangleTest {
    boolean adjacentTestResult;
    boolean oppositeTestResult;
    boolean hypotenuseTestResult;
    public TriangleTest(ServerWorld world, BlockPos refPos, Direction direction, TestDirection testDirection){
        triangleTest(world, refPos, direction, testDirection);
    }
    public void triangleTest(ServerWorld world, BlockPos refPos, Direction direction, TestDirection testDirection){
        Direction perpendicularDirection;
        if(testDirection == TestDirection.LEFT){
            perpendicularDirection = direction.getLeftDirection();
        }else{
            perpendicularDirection = direction.getRightDirection();
        }
        // 삼각 검사 - 밑변
        adjacentTestResult = BlockStateTester.isReachable(world, refPos, perpendicularDirection);
        if(adjacentTestResult){
            // 삼각 검사 - 옆변
            BlockPos sidePos = LinearSearcher.moveOneBlock(world, refPos, perpendicularDirection);
            oppositeTestResult = BlockStateTester.isReachable(world, sidePos, direction);
            // 삼각 검사 - 대각선
            Direction hypotenuseDirection = new Direction(direction.getX() + perpendicularDirection.getX(),
                    direction.getX() + perpendicularDirection.getZ());
            hypotenuseTestResult = BlockStateTester.isReachable(world, refPos,hypotenuseDirection);
        }
    }
    public static boolean[] triangleTest2(ServerWorld world, BlockPos refPos, Direction direction, TestDirection testDirection){
        Direction perpendicularDirection;
        if(testDirection == TestDirection.LEFT){
            perpendicularDirection = direction.getLeftDirection();
        }else{
            perpendicularDirection = direction.getRightDirection();
        }

        // 삼각 검사 - 밑변
        boolean adjacentTest = BlockStateTester.isReachable(world, refPos, perpendicularDirection);
        if(adjacentTest){
            // 삼각 검사 - 옆변
            BlockPos sidePos = LinearSearcher.moveOneBlock(world, refPos, perpendicularDirection);
            boolean oppositTest = BlockStateTester.isReachable(world, sidePos, direction);
            // 삼각 검사 - 대각선
            Direction hypotenuseDirection = new Direction(direction.getX() + perpendicularDirection.getX(),
                    direction.getX() + perpendicularDirection.getZ());
            boolean hypotenuseTest = BlockStateTester.isReachable(world, refPos,hypotenuseDirection);
            return new boolean[]{adjacentTest, oppositTest, hypotenuseTest};
        }else{
            return new boolean[]{adjacentTest};
        }
    }
}
