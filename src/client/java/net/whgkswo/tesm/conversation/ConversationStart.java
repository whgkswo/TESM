package net.whgkswo.tesm.conversation;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;
import net.whgkswo.tesm.conversation.v2.ConversationScreen;
import net.whgkswo.tesm.networking.ModMessages;
import net.whgkswo.tesm.raycast.CenterRaycast;

public class ConversationStart {
    public static boolean convOn = false;
    public static String convPartnerName;
    public static LivingEntity convPartner;

    //엔티티와 플레이어의 좌표를 이용해 시선 방향 계산
    public static float[] getYawAndPitch(Vec3d convPartnerPos, Vec3d playerPos){

        float yaw = (float) Math.toDegrees(
                Math.atan2(convPartnerPos.getX()-playerPos.getX(), convPartnerPos.getZ()-playerPos.getZ())
        );

        float pitch = (float) Math.toDegrees(
                // 상대는 발에서 +0.7블록 기준(가슴), 플레이어는 발에서 +1블록 기준(머리)
                (convPartnerPos.getY()+0.7-(playerPos.getY()+1)) / getXYDistance(convPartnerPos,playerPos)
        );

        return new float[] {-yaw,-pitch};
    }
    public static double getXYDistance(Vec3d convPartnerPos, Vec3d playerPos){
        double distance = Math.sqrt(
                Math.pow(convPartnerPos.getX()-playerPos.getX(),2)+Math.pow(convPartnerPos.getZ()-playerPos.getZ(),2)
        );
        return distance;
    }

    //본체 메소드
    public static void conversation(){
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            UseEntityCallback.EVENT.register((player, world, hand, target, entityHitResult) -> {

                if (target instanceof LivingEntity && world.isClient() && CenterRaycast.interactOverlayOn) {

                    convPartner = (LivingEntity) target;
                    convPartnerName = String.valueOf(entityHitResult.getEntity().getCustomName().getString());

                    /*if(ConversationScreen.getDLArray() == null) {
                        player.sendMessage(Text.literal("안녕하세요!"));
                    }else{
                        // 대화 상대 움직임 제한 (서버에 패킷 전송)
                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeInt(convPartner.getId());
                        ClientPlayNetworking.send(ModMessages.FREEZE_ENTITY_ID, buf);
                        // 플레이어 시선 방향 조정
                        player.setYaw(getYawAndPitch(convPartner.getPos(),player.getPos())[0]);
                        player.setPitch(getYawAndPitch(convPartner.getPos(),player.getPos())[1]);
                        // 스크린 열기
                        client.setScreen(new ConversationScreen());
                    }*/
                    // 대화 상대 움직임 제한 (서버에 패킷 전송)
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeInt(convPartner.getId());
                    ClientPlayNetworking.send(ModMessages.FREEZE_ENTITY_ID, buf);
                    // 플레이어 시선 방향 조정
                    player.setYaw(getYawAndPitch(convPartner.getPos(),player.getPos())[0]);
                    player.setPitch(getYawAndPitch(convPartner.getPos(),player.getPos())[1]);
                    // 스크린 열기
                    client.setScreen(new ConversationScreen(convPartner));
                }
                return ActionResult.PASS;
            });
        });
    }
}
