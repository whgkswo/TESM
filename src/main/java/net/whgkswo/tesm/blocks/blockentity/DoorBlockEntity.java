package net.whgkswo.tesm.blocks.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public class DoorBlockEntity extends BlockEntity {
    private String insideName = "";
    private String outsideName = "";
    private boolean pushToOutside = false;

    public DoorBlockEntity(BlockPos pos, BlockState state){
        super(ModBlockEntityTypes.DOOR, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        nbt.putString("InsideName", insideName);
        nbt.putString("OutsideName", outsideName);
        nbt.putBoolean("PushToOutside", pushToOutside);

        //super.writeNbt(nbt, registries); // 없어도 동작, 왜인지는 모름 - markDirty 때문인가?
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        insideName = nbt.getString("InsideName");
        outsideName = nbt.getString("OutsideName");
        pushToOutside = nbt.getBoolean("PushToOutside");

        //super.readNbt(nbt, registries); // 없어도 동작, 왜인지는 모름 - markDirty 때문인가?
    }

    public NbtCompound getNbt(RegistryWrapper.WrapperLookup registries){
        return createNbt(registries);
    }

    public NbtCompound getNbt(){
        RegistryWrapper.WrapperLookup registries = world.getRegistryManager();
        return createNbt(registries);
    }

    public void setNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        readNbt(nbt, registries);
        markDirty();
    }

    public void setNbt(NbtCompound nbt){
        RegistryWrapper.WrapperLookup registries = world.getRegistryManager();
        readNbt(nbt, registries);
        markDirty();
    }

    public String getInsideName(){
        return insideName;
    }

    public String getOutsideName(){
        return outsideName;
    }

    public boolean getPushToOutside(){
        return pushToOutside;
    }
}
