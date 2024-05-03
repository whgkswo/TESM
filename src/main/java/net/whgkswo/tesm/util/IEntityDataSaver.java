package net.whgkswo.tesm.util;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

public interface IEntityDataSaver {
    NbtCompound getPersistentData();

}
