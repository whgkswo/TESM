package net.whgkswo.tesm.pathfindingv2;

import java.util.ArrayList;

public class OpenList {
    private ArrayList<JumpPoint> jumpPointList;
    private int minFValue;

    public void setMinFValue(int minFValue) {
        this.minFValue = minFValue;
    }

    public ArrayList<JumpPoint> getJumpPointList() {
        return jumpPointList;
    }

    public int getMinFValue() {
        return minFValue;
    }

    public OpenList() {
        this.jumpPointList = new ArrayList<>();
        this.minFValue = 0;
    }
}
