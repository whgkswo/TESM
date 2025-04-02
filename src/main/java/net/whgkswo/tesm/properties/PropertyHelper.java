package net.whgkswo.tesm.properties;

import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.properties.data.DoorData;

public class PropertyHelper {
    public static DoorData getDoorData(BlockState blockState){
        return new DoorData(
                blockState.get(Properties.HORIZONTAL_FACING),
                blockState.get(Properties.DOUBLE_BLOCK_HALF),
                blockState.get(Properties.DOOR_HINGE),
                blockState.get(Properties.OPEN),
                blockState.get(Properties.POWERED)
        );
    }

    public static DoorData getDoorData(BlockPos blockPos){
        BlockState blockState = GlobalVariables.world.getBlockState(blockPos);
        return getDoorData(blockState);
    }
}
