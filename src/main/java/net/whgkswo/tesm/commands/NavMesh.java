package net.whgkswo.tesm.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ExecuteCommand;
import net.whgkswo.tesm.pathfinding.v3.NavMasher;


public class NavMesh {
    public static void register(){
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) ->
                dispatcher.register(
                        CommandManager.literal("navmesh")
                                .executes(context -> {
                                    NavMasher navMasher = new NavMasher();
                                    navMasher.navMesh();
                                    return 1;
                                })
                )));
    }
}
