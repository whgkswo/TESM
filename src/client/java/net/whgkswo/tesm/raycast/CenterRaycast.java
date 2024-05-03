package net.whgkswo.tesm.raycast;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.networking.ModMessages;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public class CenterRaycast {
    public static boolean interactOverlayOn = false;
    public static String interactType;
    public static String interactTarget;
    public static void centerRaycast(){
        ClientTickEvents.END_WORLD_TICK.register(world -> {
            MinecraftClient client = MinecraftClient.getInstance();
            HitResult hitResult = client.crosshairTarget;
            PlayerEntity player = MinecraftClient.getInstance().player;

            if(hitResult==null){
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
                    BlockState blockState = client.world.getBlockState(blockPos);
                    Block block = blockState.getBlock();

                    //player.sendMessage(Text.literal(block.getName().getString()));

                    if(interactOverlayOn){
                        interactOverlayOn = false;
                    }
                    break;

                case ENTITY:
                    EntityHitResult entityHitResult = (EntityHitResult) hitResult;
                    Entity entity = entityHitResult.getEntity();

                    // 서버로 패킷 전송 (엔티티 NBT 데이터 읽어오기)
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeInt(entity.getId());
                    ClientPlayNetworking.send(ModMessages.GETNBT_ID, buf);

                    // 실행 결과는 결과 반환 패킷 내에서 처리되어 interactOverlayOn의 t/f를 결정함
                    if(interactOverlayOn){
                        interactType = "대화하기";
                        try{
                            interactTarget = entity.getCustomName().getString();
                        }catch (NullPointerException e){
                            interactTarget = "";
                        }
                    }else{
                        interactType = "";
                        interactTarget = "";
                    }
                    break;
            }
        });
    }
}
