package net.whgkswo.tesm.pathfinding.v3;

import net.minecraft.util.math.BlockPos;

import java.util.HashMap;


public class ScanDataOfChunk {
    private HashMap<BlockPos, ScanDataOfBlockPos> data;

    public ScanDataOfChunk() {
        data = new HashMap<>();
    }

    public ScanDataOfBlockPos getBlockData(BlockPos blockPos){
        return data.get(blockPos);
    }
    public void putBlockData(BlockPos blockPos, ScanDataOfBlockPos data){
        this.data.put(blockPos, data);
    }

    public HashMap<BlockPos, ScanDataOfBlockPos> getData() {
        return data;
    }
}
