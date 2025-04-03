package net.whgkswo.tesm.pathfinding.v3;

import net.whgkswo.tesm.pathfinding.v2.PathfindDirection;

import java.util.HashMap;

public class ScanDataOfBlockPos {
    public ScanDataOfBlockPos(HashMap<PathfindDirection, ScanDataOfDirection> data) {
        this.data = data;
    }
    public ScanDataOfBlockPos() {
    }
    public void setData(HashMap<PathfindDirection, ScanDataOfDirection> data) {
        this.data = data;
    }
    public void putDirectionData(PathfindDirection direction, ScanDataOfDirection data){
        this.data.put(direction,data);
    }

    public HashMap<PathfindDirection, ScanDataOfDirection> getData() {
        return data;
    }

    private HashMap<PathfindDirection, ScanDataOfDirection> data;
    public ScanDataOfDirection getDirectionData(PathfindDirection direction){
        return data.get(direction);
    }
}
