package net.whgkswo.tesm.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import net.whgkswo.tesm.data.JsonManager;
import net.whgkswo.tesm.data.dto.ChunkPosDto;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.util.BlockPosUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;
import java.util.stream.Collectors;

import static net.whgkswo.tesm.general.GlobalVariables.*;

@Mixin(WorldChunk.class)
public class WorldMixin {
    @Shadow @Final private World world;
    @Inject(method = "setBlockState", at = @At("HEAD"))
    private void onBlockStateChange(BlockPos pos, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> cir) {
        if(GlobalVariables.world == null || world.isClient){
            return;
        }
        BlockState oldState = world.getBlockState(pos);

        // 블록 상태가 실제로 변경되었는지 확인
        if (!oldState.equals(state)) {
            // 히트박스가 안 바꼈으면
            if(BlockPosUtil.isLowBlock(pos) == (state.getCollisionShape(world,pos).getMax(Direction.Axis.Y) < LOW_BLOCKHEIGHT_REF)){
                return;
            }

            GlobalVariables.player.sendMessage(Text.literal("Block changed at " + pos));
            GlobalVariables.updatedChunkSet.add(new ChunkPosDto(world.getChunk(pos).getPos()));

            // 청크 업데이트 목록 저장
            JsonManager.createJson(updatedChunkSet, "updatedChunkSet.json");
        }
    }
}
