package net.whgkswo.tesm.door;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.whgkswo.tesm.blocks.blockentity.DoorBlockEntity;
import net.whgkswo.tesm.executions.ExecutionHelper;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.properties.BlockEntityHelper;
import net.whgkswo.tesm.properties.data.DoorData;

import java.util.HashSet;
import java.util.Set;

public class DoorHelper {
    // 기준 좌표 포함
    public static Set<BlockPos> getDoorPositions(BlockPos blockPos, DoorPosRange doorPosRange){
        DoorData doorData = BlockEntityHelper.getDoorData(blockPos);
        Direction facing = doorData.facing();

        // hinge가 right면 facing 기준 왼쪽 방향, left면 오른쪽 방향
        int dx; int dz;
        if(doorData.hinge().equals(DoorHinge.LEFT)){
            Direction right = facing.rotateYClockwise();
            dx = right.getOffsetX();
            dz = right.getOffsetZ();
        }else{
            Direction left = facing.rotateYCounterclockwise();
            dx = left.getOffsetX();
            dz = left.getOffsetZ();
        }

        // half가 upper면 아래로 한칸, lower면 위로 한칸
        int dy = doorData.half().equals(DoubleBlockHalf.LOWER) ? 1 : -1;

        Set<BlockPos> set = new HashSet<>();
        if(doorPosRange.equals(DoorPosRange.ALL)) addToSet(doorData, set, blockPos); // 기준좌표
        addToSet(doorData, set, blockPos.add(dx, 0, dz)); // 옆 좌표
        if(!doorPosRange.equals(DoorPosRange.OPPOSITE_ONLY)) addToSet(doorData, set, blockPos.add(0, dy, 0)); // 위/아래 좌표
        addToSet(doorData, set, blockPos.add(dx, dy, dz)); // 대각선 좌표

        return set;
    }

    public static Set<BlockPos> getDoorPositions(BlockPos blockPos){
        return getDoorPositions(blockPos, DoorPosRange.ALL);
    }

    private static void addToSet(DoorData doorData, Set<BlockPos> set, BlockPos blockPos){
        if(isValidPos(doorData, blockPos)) set.add(blockPos);
    }

    private static boolean isValidPos(DoorData refData, BlockPos blockPos){
        BlockEntity blockEntity = GlobalVariables.world.getBlockEntity(blockPos);

        if (!(blockEntity instanceof DoorBlockEntity)) return false;

        DoorData doorData = BlockEntityHelper.getDoorData(blockPos);

        return doorData.facing().equals(refData.facing());
    }

    public static void registerDoorUseEvent(){
        UseBlockCallback.EVENT.register(((playerEntity, world, hand, blockHitResult) -> {
            // 메인 핸드가 아니거나 클라이언트면 패스
            if(hand != Hand.MAIN_HAND || world.isClient()) return ActionResult.PASS;

            BlockPos refPos = blockHitResult.getBlockPos();
            BlockState refState = world.getBlockState(refPos);
            // 문 블록이 아니면 패스
            if(!(refState.getBlock() instanceof DoorBlock)) return ActionResult.PASS;

            Set<BlockPos> doorPositions = DoorHelper.getDoorPositions(refPos, DoorPosRange.EXCLUDE_REF);
            boolean isOpen = refState.get(Properties.OPEN);
            world.setBlockState(refPos, refState.with(Properties.OPEN, !isOpen));
            // 반대쪽 문도 같이 열리게 하기
            for(BlockPos pos : doorPositions){
                BlockState state = world.getBlockState(pos).with(Properties.OPEN, !isOpen);
                world.setBlockState(pos, state);
            }
            // 일정 시간 뒤에 닫히도록 예약
            if(!isOpen){
                ExecutionHelper.addToOnServerTick(30, () -> {
                    Set<BlockPos> fullDoorPositions = DoorHelper.getDoorPositions(refPos);
                    for(BlockPos pos : fullDoorPositions){
                        BlockState state = world.getBlockState(pos).with(Properties.OPEN, false);
                        world.setBlockState(pos, state);
                    }
                });
            }
            return ActionResult.SUCCESS; // SUCCESS를 반환하면 이 뒤의 내용이 취소됨
        }));
    }

    private enum DoorPosRange{
        ALL,
        OPPOSITE_ONLY,
        EXCLUDE_REF
        ;
    }
}
