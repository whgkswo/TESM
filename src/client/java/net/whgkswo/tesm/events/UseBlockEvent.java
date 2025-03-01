package net.whgkswo.tesm.events;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.ActionResult;

public class UseBlockEvent {
    public static void register(){
        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {
            // 왼손, 오른손 각각 서버&클라이언트 2번씩 총 4번 실행되므로 조건을 달아 줘야 함
            if(hand.equals(player.getActiveHand()) && world.isClient){
                if(player.getMainHandStack().isEmpty()){
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeBlockHitResult(hitResult);
                    // TODO: 포팅
                    //ClientPlayNetworking.send(ModMessages.USE_BLOCK_ID, buf);
                }
            }
            return ActionResult.PASS;
        }));
    }
}
