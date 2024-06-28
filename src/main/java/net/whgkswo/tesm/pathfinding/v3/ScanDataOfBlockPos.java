package net.whgkswo.tesm.pathfinding.v3;

import net.whgkswo.tesm.pathfinding.v2.Direction;

import java.util.HashMap;

public class ScanDataOfBlockPos {
    public ScanDataOfBlockPos(HashMap<Direction, ScanDataOfDirection> data) {
        this.data = data;
    }
    public ScanDataOfBlockPos() {
    }
    public void setData(HashMap<Direction, ScanDataOfDirection> data) {
        this.data = data;
    }
    public void putDirectionData(Direction direction, ScanDataOfDirection data){
        this.data.put(direction,data);
    }

    public HashMap<Direction, ScanDataOfDirection> getData() {
        return data;
    }

    private HashMap<Direction, ScanDataOfDirection> data;
    public ScanDataOfDirection getDirectionData(Direction direction){
        return data.get(direction);
    }
}
