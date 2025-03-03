package net.whgkswo.tesm.events;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.ActionResult;
import net.whgkswo.tesm.networking.payload.data.c2s_req.UseBlockReq;

public class UseBlockEvent {
    public static void register(){
        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {
            // 왼손, 오른손 각각 서버&클라이언트 2번씩 총 4번 실행되므로 조건을 달아 줘야 함
            if(hand.equals(player.getActiveHand()) && world.isClient){
                if(player.getMainHandStack().isEmpty()){
                    // 서버로 요청 전송
                    ClientPlayNetworking.send(new UseBlockReq(hitResult.getBlockPos()));
                }
            }
            return ActionResult.PASS;
        }));
    }
}
