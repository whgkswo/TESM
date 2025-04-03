package net.whgkswo.tesm.gui.overlay.raycast;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.networking.payload.data.c2s_req.BlockDataReq;
import net.whgkswo.tesm.networking.payload.data.c2s_req.RaycastingNbtReq;


public class HUDRaycastHelper {
    public static boolean interactOverlayOn = false;
    public static String interactType;
    public static String interactTarget;
    public static void centerRaycast(){
        ClientTickEvents.END_WORLD_TICK.register(world -> {
            MinecraftClient client = MinecraftClient.getInstance();
            HitResult hitResult = client.crosshairTarget;

            if(hitResult == null){
                return;
            }
            switch (hitResult.getType()){
                case MISS:
                    if(interactOverlayOn){
                        interactOverlayOn = false;
                    }
                    break;

                case BLOCK:
                    BlockHitResult blockHit = (BlockHitResult) hitResult;
                    BlockPos blockPos = blockHit.getBlockPos();

                    handleBlockEntity(blockPos);

                    //player.sendMessage(Text.literal(block.getName().getString()));


                    break;

                case ENTITY:
                    EntityHitResult entityHitResult = (EntityHitResult) hitResult;
                    Entity entity;
                    try{
                        entity = entityHitResult.getEntity();
                    }catch (NullPointerException e){
                        return;
                    }
                    // 서버로 패킷 전송 (엔티티 NBT 데이터 읽어오기)
                    ClientPlayNetworking.send(new RaycastingNbtReq(entity.getId()));

                    // 실행 결과는 결과 반환 패킷 내에서 처리되어 interactOverlayOn의 t/f를 결정함
                    if(interactOverlayOn){
                        interactType = "대화하기";
                    }else{
                        interactType = "";
                        interactTarget = "";
                    }
                    break;
            }
        });
    }

    private static void handleBlockEntity(BlockPos blockPos){
        ClientPlayNetworking.send(new BlockDataReq(blockPos));
    }
}
