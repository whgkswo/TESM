package net.whgkswo.tesm.pathfinding.v3;

import net.whgkswo.tesm.pathfinding.v2.Direction;

import java.util.HashMap;

public class NavMeshDataOfBlockPos {


    public NavMeshDataOfBlockPos() {
        data = new HashMap<>();
    }

    public void setData(HashMap<Direction, NavMeshDataOfDirection> data) {
        this.data = data;
    }
    public void putDirectionData(Direction direction, NavMeshDataOfDirection data){
        this.data.put(direction,data);
    }

    public HashMap<Direction, NavMeshDataOfDirection> getData() {
        return data;
    }

    private HashMap<Direction, NavMeshDataOfDirection> data;
}
