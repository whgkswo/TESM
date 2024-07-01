package net.whgkswo.tesm.exceptions;

import net.minecraft.util.math.ChunkPos;

public class ChunkDataNotFoundExeption extends RuntimeException{
    private ChunkPos chunkPos;

    public ChunkPos getChunkPos() {
        return chunkPos;
    }

    public ChunkDataNotFoundExeption(ChunkPos chunkPos){
        super();
        this.chunkPos = chunkPos;
    }
}
