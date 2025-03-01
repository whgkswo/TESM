package net.whgkswo.tesm.general;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.Entity;
import net.whgkswo.tesm.data.JsonManager;
import net.whgkswo.tesm.pathfinding.v3.ChunkScanner;

import static net.whgkswo.tesm.general.GlobalVariables.world;

public class OnPlayerLeaves {
    public static void onPlayerLeaves(){
        ServerPlayConnectionEvents.DISCONNECT.register((player, server) -> {
            GlobalVariables.pathfindEntityList.forEach(entity -> entity.kill(world));
            GlobalVariables.pathfindEntityList.clear();
            JsonManager.createJson(GlobalVariables.updatedChunkSet, "/updatedChunkSet.json");
        });
    }
}
