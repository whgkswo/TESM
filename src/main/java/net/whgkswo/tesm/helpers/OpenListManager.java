package net.whgkswo.tesm.helpers;

import net.whgkswo.tesm.pathfinding.v2.JumpPoint;

import java.util.ArrayList;

public class OpenListManager {
    public static int getMinFIndex(ArrayList<JumpPoint> openList){
        int minF = Integer.MAX_VALUE;
        int minFIndex = 0;
        for(int i = 0; i< openList.size(); i++){
            if(openList.get(i).getFValue() <= minF){
                minF = openList.get(i).getFValue();
                minFIndex = i;
            }
        }
        return minFIndex;
    }
    public static JumpPoint getMinFPoint(ArrayList<JumpPoint> openList){
        int minF = Integer.MAX_VALUE;
        JumpPoint minFPoint = null;
        for(int i = 0; i< openList.size(); i++){
            if(openList.get(i).getFValue() <= minF){
                minF = openList.get(i).getFValue();
                minFPoint = openList.get(i);
            }
        }
        return minFPoint;
    }
}
