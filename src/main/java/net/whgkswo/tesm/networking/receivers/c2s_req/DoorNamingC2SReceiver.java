package net.whgkswo.tesm.networking.receivers.c2s_req;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.blocks.blockentity.DoorBlockEntity;
import net.whgkswo.tesm.door.DoorHelper;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.networking.payload.data.s2c_req.DoorNaming;
import java.util.Set;

public class DoorNamingC2SReceiver {
    public static void handle(DoorNaming payload, ServerPlayNetworking.Context context){

        RegistryWrapper.WrapperLookup registries = GlobalVariables.world.getRegistryManager();

        Set<BlockPos> doorPositions = DoorHelper.getDoorPositions(payload.blockPos());

        for(BlockPos pos : doorPositions){
            DoorBlockEntity door = (DoorBlockEntity) GlobalVariables.world.getBlockEntity(pos);
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putString("InsideName", payload.insideName());
            nbtCompound.putString("OutsideName", payload.outsideName());
            nbtCompound.putBoolean("PushToOutside", payload.pushToOutside());

            door.setNbt(nbtCompound, registries);
        }
    }
}
