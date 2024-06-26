package net.whgkswo.tesm.pathfinding.v3;

import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.pathfinding.v2.Direction;

import java.util.HashMap;

public class NavMeshDataOfBlockPos {


    public NavMeshDataOfBlockPos() {
        directionData = new HashMap<>();
    }

    public void setDirectionData(HashMap<Direction, NavMeshDataOfDirection> directionData) {
        this.directionData = directionData;
    }

    public HashMap<Direction, NavMeshDataOfDirection> getDirectionData() {
        return directionData;
    }

    private HashMap<Direction, NavMeshDataOfDirection> directionData;
}
