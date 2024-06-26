package net.whgkswo.tesm.pathfinding.v3;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

import java.util.HashMap;


public class NavMeshDataOfChunk {
    private Chunk chunk;
    private HashMap<BlockPos, NavMeshDataOfBlockPos> data;

    public NavMeshDataOfChunk(Chunk chunk) {
        this.chunk = chunk;
        data = new HashMap<>();
    }

    public NavMeshDataOfBlockPos getBlockData(BlockPos blockPos){
        return data.get(blockPos);
    }

    public HashMap<BlockPos, NavMeshDataOfBlockPos> getData() {
        return data;
    }
}
