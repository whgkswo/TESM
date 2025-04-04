package net.whgkswo.tesm.exceptions;

import net.minecraft.util.math.BlockPos;

public class BlockDataNotFoundExeption extends RuntimeException {
    private BlockPos blockPos;
    public BlockPos getBlockPos(){
        return blockPos;
    }
    public BlockDataNotFoundExeption(BlockPos blockPos){
        super();
        this.blockPos = blockPos;
    }
}
