package net.whgkswo.tesm.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.whgkswo.tesm.generaltasks.OnServerTicks;

public class TickFreezeToggleC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender){
        // 아래 코드들은 서버에서만 실행됨!
        boolean freezeOn = buf.readBoolean();

        if(freezeOn){
            server.getTickManager().setFrozen(true);
            OnServerTicks.timeFlowOn = false;
        }else{
            server.getTickManager().setFrozen(false);
            OnServerTicks.timeFlowOn = true;
        }
    }
}
