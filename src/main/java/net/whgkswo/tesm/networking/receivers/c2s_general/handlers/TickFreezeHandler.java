package net.whgkswo.tesm.networking.receivers.c2s_general.handlers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.general.OnServerTicks;

public class TickFreezeHandler {
    public static void freeze(){
        MinecraftServer server = GlobalVariables.world.getServer();
        server.getTickManager().setFrozen(true);
        OnServerTicks.timeFlowOn = false;
    }

    public static void unfreeze(){
        MinecraftServer server = GlobalVariables.world.getServer();
        server.getTickManager().setFrozen(false);
        OnServerTicks.timeFlowOn = true;
    }
}
