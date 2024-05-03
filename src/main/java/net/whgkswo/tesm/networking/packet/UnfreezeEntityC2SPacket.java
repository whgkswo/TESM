package net.whgkswo.tesm.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class UnfreezeEntityC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender){
        // 아래 코드들은 서버에서만 실행됨!

        LivingEntity convPartner = (LivingEntity) player.getWorld().getEntityById(buf.readInt());

        if(convPartner!=null){
            convPartner.removeStatusEffect(StatusEffects.SLOWNESS);
            // n밀리초 후에 실행
            new Thread(() ->{
                try {
                    Thread.sleep(100);
                    player.removeStatusEffect(StatusEffects.JUMP_BOOST);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
            //player.sendMessage(Text.literal("[DefrostEntityC2SPacket] convPartner 확인됨."));
        }else{
            //player.sendMessage(Text.literal("[DefrostEntityC2SPacket] convPartner가 null입니다."));
        }
    }
}
