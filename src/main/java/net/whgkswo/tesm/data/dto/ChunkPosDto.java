package net.whgkswo.tesm.data.dto;

import net.minecraft.util.math.ChunkPos;

import java.util.Objects;

public class ChunkPosDto {
    private int x;
    private int z;
    public ChunkPosDto(int x, int z){
        this.x = x;
        this.z = z;
    }

    public ChunkPosDto() {
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChunkPosDto that)) return false;
        return getX() == that.getX() && getZ() == that.getZ();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getZ());
    }

    public ChunkPosDto(ChunkPos chunkPos){
        this.x = chunkPos.x;
        this.z = chunkPos.z;
    }
}
