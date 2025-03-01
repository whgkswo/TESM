package net.whgkswo.tesm.networking.receivers;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.nbt.NBTManager;
import net.whgkswo.tesm.networking.payload.c2s_req.SetNbtReq;

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
                        NBTManager.getCustomNbt(entity).putString(key, (String) value);
                    }
                    case Integer i -> {
                        NBTManager.getCustomNbt(entity).putInt(key, (int) value);
                    }
                    case Boolean b -> {
                        NBTManager.getCustomNbt(entity).putBoolean(key, (boolean) value);
                    }
                    default -> {

                    }
                }
            }
        }
    }
}
