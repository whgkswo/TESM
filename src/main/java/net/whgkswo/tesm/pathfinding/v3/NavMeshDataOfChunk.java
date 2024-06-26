package net.whgkswo.tesm.pathfinding.v3;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

import java.util.HashMap;


public class NavMeshDataOfChunk {
    private HashMap<BlockPos, NavMeshDataOfBlockPos> data;

    public NavMeshDataOfChunk() {
        data = new HashMap<>();
    }

    public NavMeshDataOfBlockPos getBlockData(BlockPos blockPos){
        return data.get(blockPos);
    }
    public void putBlockData(BlockPos blockPos,NavMeshDataOfBlockPos data){
        this.data.put(blockPos, data);
    }

    public HashMap<BlockPos, NavMeshDataOfBlockPos> getData() {
        return data;
    }
}
