package net.whgkswo.tesm.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class FreezeEntityC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender){
        // 아래 코드들은 서버에서만 실행됨!
        //convPartner.setMovementSpeed(0.0f);
        LivingEntity convPartner = (LivingEntity) player.getWorld().getEntityById(buf.readInt());
        if(convPartner!=null){
            convPartner.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,999999, 255, false,false,false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 999999,128,false,false,false));
            //player.sendMessage(Text.literal("[FreezeEntityC2SPacket] convPartner 확인됨."));
        }else{
            //player.sendMessage(Text.literal("[FreezeEntityC2SPacket] convPartner가 null입니다."));
        }


    }
}
