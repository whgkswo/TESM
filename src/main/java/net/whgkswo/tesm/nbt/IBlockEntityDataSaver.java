package net.whgkswo.tesm.nbt;

import net.minecraft.nbt.NbtCompound;

public interface IBlockEntityDataSaver {
    NbtCompound getPersistentData();
}
