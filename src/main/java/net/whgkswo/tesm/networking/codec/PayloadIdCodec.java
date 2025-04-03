package net.whgkswo.tesm.networking.codec;

import net.minecraft.block.enums.DoorHinge;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.Direction;
import net.whgkswo.tesm.networking.payload.id.PayloadId;
import net.whgkswo.tesm.properties.data.DoorData;

public class PayloadIdCodec {
    public static final PacketCodec<RegistryByteBuf, PayloadId> CODEC = new PacketCodec<>() {
        @Override
        public void encode(RegistryByteBuf buf, PayloadId value) {
            buf.writeEnumConstant(value);
        }

        @Override
        public PayloadId decode(RegistryByteBuf buf) {
            return buf.readEnumConstant(PayloadId.class);
        }
    };
}
