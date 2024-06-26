package net.whgkswo.tesm.pathfinding.v3;

import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.pathfinding.v2.Direction;

import java.util.HashMap;

public class NavMeshDataOfBlockPos {
    private BlockPos blockPos;

    public HashMap<Direction, NavMeshDataOfDirection> getData() {
        return data;
    }

    private HashMap<Direction, NavMeshDataOfDirection> data;
}
