package net.whgkswo.tesm.pathfinding.v3;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;


public class ScanDataOfChunk {
    private HashMap<BlockPos, ScanDataOfBlockPos> data;

    public ScanDataOfChunk() {
    }

    public ScanDataOfChunk(HashMap<BlockPos, ScanDataOfBlockPos> data) {
        this.data = data;
    }

    public ScanDataOfBlockPos getBlockData(BlockPos blockPos){
        return data.get(blockPos);
    }
    public void putBlockData(BlockPos blockPos, ScanDataOfBlockPos data){
        this.data.put(blockPos, data);
    }

    public void setData(HashMap<BlockPos, ScanDataOfBlockPos> data) {
        this.data = data;
    }

    public HashMap<BlockPos, ScanDataOfBlockPos> getData() {
        return data;
    }
}
