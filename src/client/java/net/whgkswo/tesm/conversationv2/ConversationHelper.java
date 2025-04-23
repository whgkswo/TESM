package net.whgkswo.tesm.conversationv2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.whgkswo.tesm.conversation.ConversationScreen;
import net.whgkswo.tesm.conversationv2.screen.ConversationScreenV2;
import net.whgkswo.tesm.conversationv2.screen.ExampleScreen;
import net.whgkswo.tesm.data.ClientResourceHelper;
import net.whgkswo.tesm.data.json.DecisionDeserializer;
import net.whgkswo.tesm.data.json.DialogueTextDeserializer;
import net.whgkswo.tesm.data.json.FlowDeserializer;
import net.whgkswo.tesm.lang.LanguageHelper;
import net.whgkswo.tesm.networking.payload.data.c2s_req.ConversationNbtReq;
import net.whgkswo.tesm.gui.overlay.raycast.HUDRaycastHelper;
import net.whgkswo.tesm.networking.payload.data.s2c_res.ConversationNbtRes;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ConversationHelper {
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
    public static void registerUseEntityEventHandler(){
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            UseEntityCallback.EVENT.register((player, world, hand, target, entityHitResult) -> {
                if (target instanceof LivingEntity && world.isClient() && hand == Hand.MAIN_HAND && HUDRaycastHelper.interactOverlayOn) {
                    convPartner = (LivingEntity) target;

                    // 타깃의 nbt태그 검사(서버로 패킷 보내기 - 응답을 받는 핸들러에서 조건에 맞으면 대화 시작 메서드 호출)
                    ConversationNbtReq req = new ConversationNbtReq(convPartner.getId());
                    ClientPlayNetworking.send(req);
                }
                return ActionResult.PASS;
            });
        });
    }
    public static void conversationStart(ConversationNbtRes partnerInfo, Entity player, MinecraftClient client){
        // 플레이어 시선 방향 조정
        player.setYaw(getYawAndPitch(convPartner.getPos(),player.getPos())[0]);
        player.setPitch(getYawAndPitch(convPartner.getPos(),player.getPos())[1]);
        // 스크린 열기
        //client.setScreen(new ConversationScreen(convPartner));
        //client.setScreen(new ConversationScreenV2(partnerInfo));
        client.setScreen(new ExampleScreen(partnerInfo));
    }

    public static Queue<DialogueText> getTexts(String path){
        JsonArray json = ClientResourceHelper.getInstance().getJsonArray("conversation/" + path);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DialogueText.class, new DialogueTextDeserializer());
        Gson gson = gsonBuilder.create();

        // TypeToken을 사용하여 List<DialogueText> 타입으로 변환
        Type listType = new TypeToken<List<DialogueText>>(){}.getType();
        List<DialogueText> dialogueTextList = gson.fromJson(json, listType);

        // List를 Queue로 변환
        return new LinkedList<>(dialogueTextList);
    }

    public static List<DecisionV2> getDecisions(String path){
        JsonArray json = ClientResourceHelper.getInstance().getJsonArray("conversation/" + path);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DecisionV2.class, new DecisionDeserializer());
        Gson gson = gsonBuilder.create();

        // TypeToken을 사용하여 List<Decision> 타입으로 변환
        Type listType = new TypeToken<List<DecisionV2>>(){}.getType();
        return gson.fromJson(json, listType);
    }

    private static JsonObject getFlowJson(String partnerName, String fileName){
        String path = String.format("conversation/%s/flows/%s", LanguageHelper.toSnakeCase(partnerName), fileName);
        return ClientResourceHelper.getInstance().getJsonObject(path);
    }

    public static Flow getFlow(String partnerName, String fileName){
        JsonObject json = getFlowJson(partnerName, fileName);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Flow.class, new FlowDeserializer());
        Gson gson = gsonBuilder.create();

        return gson.fromJson(json, Flow.class);
    }

    public static Flow getFlow(String path){
        JsonObject json = ClientResourceHelper.getInstance().getJsonObject("conversation/" + path);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Flow.class, new FlowDeserializer());
        Gson gson = gsonBuilder.create();

        return gson.fromJson(json, Flow.class);
    }
}
