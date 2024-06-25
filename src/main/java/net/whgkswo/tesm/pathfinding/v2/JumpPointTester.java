package net.whgkswo.tesm.pathfinding.v2;

public class JumpPointTester {
    public static boolean jumpPointTest(TriangleTestResult triangleTestResult, boolean finalTestResult){
        if(finalTestResult){
            return !triangleTestResult.getAdjacentTestResult() || !triangleTestResult.getOppositeTestResult()
                    || !triangleTestResult.getHypotenuseTestResult();
        }
        return false;
    }
}
