package net.whgkswo.tesm.networking.receivers.c2s_req;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.whgkswo.tesm.blocks.blockentity.DoorBlockEntity;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.nbt.IEntityDataSaver;
import net.whgkswo.tesm.networking.payload.data.c2s_req.ConversationNbtReq;
import net.whgkswo.tesm.networking.payload.data.s2c_req.DoorNaming;
import net.whgkswo.tesm.networking.payload.data.s2c_res.ConversationNbtRes;

public class DoorNamingC2SReceiver {
    public static void handle(DoorNaming payload, ServerPlayNetworking.Context context){

        DoorBlockEntity door = (DoorBlockEntity) GlobalVariables.world.getBlockEntity(payload.blockPos());
        RegistryWrapper.WrapperLookup registries = GlobalVariables.world.getRegistryManager();

        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putString("InsideName", payload.insideName());
        nbtCompound.putString("OutsideName", payload.outsideName());
        nbtCompound.putBoolean("PushToOutside", payload.pushToOutside());

        door.setNbt(nbtCompound, registries);
    }
}
