package net.whgkswo.tesm.networking.codec;

import net.minecraft.block.enums.DoorHinge;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.whgkswo.tesm.properties.data.DoorData;

public class DoorDataCodec {
    public static final PacketCodec<RegistryByteBuf, DoorData> CODEC = new PacketCodec<>() {
        @Override
        public void encode(RegistryByteBuf buf, DoorData value) {
            buf.writeEnumConstant(value.facing());
            buf.writeEnumConstant(value.half());
            buf.writeEnumConstant(value.hinge());
            buf.writeBoolean(value.open());
            buf.writeBoolean(value.powered());
            buf.writeString(value.insideName());
            buf.writeString(value.outsideName());
            buf.writeBoolean(value.pushToOutside());
        }

        @Override
        public DoorData decode(RegistryByteBuf buf) {
            Direction facing = buf.readEnumConstant(Direction.class);
            DoubleBlockHalf half = buf.readEnumConstant(DoubleBlockHalf.class);
            DoorHinge hinge = buf.readEnumConstant(DoorHinge.class);
            boolean open = buf.readBoolean();
            boolean powered = buf.readBoolean();
            String insideName = buf.readString();
            String outsideName = buf.readString();
            boolean pushToOutside = buf.readBoolean();

            return new DoorData(facing, half, hinge, open, powered, insideName, outsideName, pushToOutside);
        }
    };
}
