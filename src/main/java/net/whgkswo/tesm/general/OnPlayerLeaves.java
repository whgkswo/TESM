package net.whgkswo.tesm.general;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.whgkswo.tesm.data.ScanHelper;

import static net.whgkswo.tesm.general.GlobalVariables.world;

public class OnPlayerLeaves {
    public static void onPlayerLeaves(){
        ServerPlayConnectionEvents.DISCONNECT.register((player, server) -> {
            GlobalVariables.pathfindEntityList.forEach(entity -> entity.kill(world));
            GlobalVariables.pathfindEntityList.clear();
            ScanHelper.createScanData(GlobalVariables.updatedChunkSet, "/updatedChunkSet.json");
        });
    }
}
