package net.whgkswo.tesm.helpers;

import net.whgkswo.tesm.pathfinding.v2.TriangleTestResult;

public class JumpPointTester {
    public static boolean jumpPointTest(TriangleTestResult triangleTestResult, boolean finalTestResult){
        if(finalTestResult){
            return !triangleTestResult.getAdjacentTestResult() || !triangleTestResult.getOppositeTestResult()
                    || !triangleTestResult.getHypotenuseTestResult();
        }
        return false;
    }
}
