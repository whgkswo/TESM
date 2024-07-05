package net.whgkswo.tesm.general;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.Entity;
import net.whgkswo.tesm.pathfinding.v3.ChunkScanner;

public class OnPlayerLeaves {
    public static void onPlayerLeaves(){
        ServerPlayConnectionEvents.DISCONNECT.register((player, server) -> {
            GlobalVariables.pathfindEntityList.forEach(Entity::kill);
            GlobalVariables.pathfindEntityList.clear();
        });
    }
}
