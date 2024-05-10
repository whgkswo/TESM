package net.whgkswo.tesm.pathfindingv2;

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
}
