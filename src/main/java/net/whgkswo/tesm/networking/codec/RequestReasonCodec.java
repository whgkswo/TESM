package net.whgkswo.tesm.networking.codec;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.whgkswo.tesm.networking.payload.id.RequestReason;

public class RequestReasonCodec {
    public static final PacketCodec<RegistryByteBuf, RequestReason> CODEC = new PacketCodec<>() {
        @Override
        public void encode(RegistryByteBuf buf, RequestReason value) {
            buf.writeEnumConstant(value);
        }

        @Override
        public RequestReason decode(RegistryByteBuf buf) {
            return buf.readEnumConstant(RequestReason.class);
        }
    };
}
