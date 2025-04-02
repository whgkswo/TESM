package net.whgkswo.tesm.networking.receivers.c2s_req;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.nbt.NBTHelper;
import net.whgkswo.tesm.networking.payload.data.c2s_req.SetNbtReq;

import java.util.Map;

public class SetNbtReceiver {
    public static void handle(SetNbtReq payload, ServerPlayNetworking.Context context){

        // 엔티티 찾기
        int id = payload.entityId();
        Entity entity = GlobalVariables.world.getEntityById(id);

        if (entity != null) {
            Map<String, Object> data = payload.data();

            for(Map.Entry<String, Object> entry : data.entrySet()){
                String key = entry.getKey();
                Object value = entry.getValue();
                switch (value){
                    case String s -> {
                        NBTHelper.getEntityNbt(entity).putString(key, (String) value);
                    }
                    case Integer i -> {
                        NBTHelper.getEntityNbt(entity).putInt(key, (int) value);
                    }
                    case Boolean b -> {
                        NBTHelper.getEntityNbt(entity).putBoolean(key, (boolean) value);
                    }
                    default -> {

                    }
                }
            }
        }
    }
}
