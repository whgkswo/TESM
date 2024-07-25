package net.whgkswo.tesm.nbt;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.whgkswo.tesm.util.IEntityDataSaver;

public class NBTManager {
    public static NbtCompound getCustomNbt(Entity entity){
        return ((IEntityDataSaver)entity).getPersistentData().getCompound("EntityData");
    }
}
