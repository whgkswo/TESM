package net.whgkswo.tesm.nbt;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;

public class NBTHelper {
    public static NbtCompound getEntityNbt(Entity entity){
        return ((IEntityDataSaver)entity).getPersistentData().getCompound("EntityData");
    }
}
