package net.whgkswo.tesm.items.features;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.whgkswo.tesm.blocks.blockentity.DoorBlockEntity;
import net.whgkswo.tesm.networking.payload.data.s2c_req.DoorNaming;
import net.whgkswo.tesm.tags.BlockTags;

public class MagicWand extends Item {
    public MagicWand(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand){
        if(world.isClient) return ActionResult.PASS;

        HitResult hitResult = player.raycast(5, 0, false);

        switch (hitResult.getType()){
            case BLOCK -> {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                BlockState blockState = world.getBlockState(blockHitResult.getBlockPos());

                if(blockState.isIn(BlockTags.DOORS)){
                    return useOnDoor(world, blockHitResult, player);
                }else{
                    return useOnBlock(blockHitResult, player);
                }
            }
            case ENTITY -> {
                return useOnEntity(hitResult, player);
            }
            case MISS -> {
                return useOnMissed(hitResult, player);
            }
        }
        return ActionResult.PASS;
    }

    private ActionResult useOnBlock(BlockHitResult hitResult, PlayerEntity player){
        player.sendMessage(Text.literal("블록에 마법봉 사용!"), false);
        return ActionResult.SUCCESS;
    }
    private ActionResult useOnDoor(World world, BlockHitResult hitResult, PlayerEntity player){
        BlockEntity blockEntity = world.getBlockEntity(hitResult.getBlockPos());
        DoorBlockEntity doorEntity = (DoorBlockEntity) blockEntity;

        if(blockEntity == null) ActionResult.FAIL.isAccepted();

        RegistryWrapper.WrapperLookup registries = world.getRegistryManager();
        NbtCompound nbt = doorEntity.getNbt(registries);

        String insideName = nbt.getString("InsideName");
        String outsideName = nbt.getString("OutsideName");
        boolean pushToOutside = nbt.getBoolean("PushToOutside");

        // 문 이름 설정 스크린 요청
        ServerPlayNetworking.send((ServerPlayerEntity) player, new DoorNaming(hitResult.getBlockPos(), insideName, outsideName, pushToOutside));
        return ActionResult.SUCCESS;
    }
    private ActionResult useOnEntity(HitResult hitResult, PlayerEntity player){
        player.sendMessage(Text.literal("엔티티에 마법봉 사용!"), false);
        return ActionResult.SUCCESS;
    }
    private ActionResult useOnMissed(HitResult hitResult, PlayerEntity player){
        player.sendMessage(Text.literal("허공에 마법봉 사용!"), false);
        return ActionResult.SUCCESS;
    }
}
