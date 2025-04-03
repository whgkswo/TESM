package net.whgkswo.tesm.properties.data;

import net.minecraft.block.enums.DoorHinge;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.util.math.Direction;

public record DoorData(
        Direction facing,
        DoubleBlockHalf half,
        DoorHinge hinge,
        boolean open,
        boolean powered,
        String insideName,
        String outsideName,
        boolean pushToOutside
) {

}
