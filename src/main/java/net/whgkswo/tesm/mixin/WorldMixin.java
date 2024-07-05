package net.whgkswo.tesm.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.GrassBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.util.BlockPosUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.whgkswo.tesm.general.GlobalVariables.LOW_BLOCKHEIGHT_REF;
import static net.whgkswo.tesm.general.GlobalVariables.world;

@Mixin(WorldChunk.class)
public class WorldMixin {
    @Inject(method = "setBlockState", at = @At("HEAD"))
    private void onBlockStateChange(BlockPos pos, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> cir) {
        World world = ((WorldChunk)(Object)this).getWorld();
        if(!(world instanceof ServerWorld)){
            return;
        }
        BlockState oldState = world.getBlockState(pos);

        // 블록 상태가 실제로 변경되었는지 확인
        if (!oldState.equals(state)) {
            // 히트박스가 안 바꼈으면
            if(BlockPosUtil.isLowBlock(pos) == (state.getCollisionShape(world,pos).getMax(Direction.Axis.Y) < LOW_BLOCKHEIGHT_REF)){
                return;
            }

            /*GlobalVariables.player.sendMessage(Text.literal("Block changed at " + pos));*/
            GlobalVariables.updatedChunkSet.add(world.getChunk(pos).getPos());
        }
    }
}
