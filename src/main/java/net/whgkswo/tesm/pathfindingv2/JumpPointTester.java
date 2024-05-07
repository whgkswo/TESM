package net.whgkswo.tesm.pathfindingv2;

public class JumpPointTester {
    public static boolean jumpPointTest(TriangleTest triangleTestResult, boolean finalTestResult){
        if(finalTestResult){
            return !triangleTestResult.adjacentTestPassed() || !triangleTestResult.oppositeTestPassed()
                    || !triangleTestResult.hypotenuseTestPassed();
        }
        return false;
    }
}
