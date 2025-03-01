package net.whgkswo.tesm.conversation;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.whgkswo.tesm.networking.payload.c2s_req.ConversationNbtReq;
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
    public static void checkCondition(){
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            UseEntityCallback.EVENT.register((player, world, hand, target, entityHitResult) -> {
                if (target instanceof LivingEntity && world.isClient() && hand == Hand.MAIN_HAND && CenterRaycast.interactOverlayOn) {
                    convPartner = (LivingEntity) target;

                    // 타깃의 nbt태그 검사(서버로 패킷 보내기 - 응답을 받는 핸들러에서 조건에 맞으면 대화 시작 메서드 호출)
                    ConversationNbtReq req = new ConversationNbtReq(convPartner.getId());
                    ClientPlayNetworking.send(req);
                }
                return ActionResult.PASS;
            });
        });
    }
    public static void conversationStart(Entity player, MinecraftClient client){
        // 플레이어 시선 방향 조정
        player.setYaw(getYawAndPitch(convPartner.getPos(),player.getPos())[0]);
        player.setPitch(getYawAndPitch(convPartner.getPos(),player.getPos())[1]);
        // 스크린 열기
        client.setScreen(new ConversationScreen(convPartner));
    }
}
