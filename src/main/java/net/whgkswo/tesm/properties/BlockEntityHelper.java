package net.whgkswo.tesm.properties;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.blocks.blockentity.DoorBlockEntity;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.properties.data.DoorData;

public class BlockEntityHelper {
    private static DoorData getDoorData(BlockState blockState, String insideName, String outsideName, boolean pushToOutside){

        return new DoorData(
                blockState.get(Properties.HORIZONTAL_FACING),
                blockState.get(Properties.DOUBLE_BLOCK_HALF),
                blockState.get(Properties.DOOR_HINGE),
                blockState.get(Properties.OPEN),
                blockState.get(Properties.POWERED),
                insideName,
                outsideName,
                pushToOutside
        );
    }

    public static DoorData getDoorData(BlockPos blockPos){
        DoorBlockEntity door = (DoorBlockEntity) GlobalVariables.world.getBlockEntity(blockPos);
        BlockState blockState = GlobalVariables.world.getBlockState(blockPos);

        return getDoorData(blockState, door.getInsideName(), door.getOutsideName(), door.getPushToOutside());
    }
}
