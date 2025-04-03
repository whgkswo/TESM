package net.whgkswo.tesm.networking.payload.data.s2c_res;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.networking.codec.DoorDataCodec;
import net.whgkswo.tesm.networking.payload.id.PayloadId;
import net.whgkswo.tesm.properties.data.DoorData;

public record DoorDataRes(DoorData doorData) implements CustomPayload {
    // 패킷 식별자
    public static final CustomPayload.Id<DoorDataRes> PACKET_ID =
            new CustomPayload.Id<>(Identifier.of(TESMMod.MODID, PayloadId.BLOCK_DATA_REQ.getLowercaseName()));

    // 파라미터 -> 타입,밸류,타입,밸류, ... ,new 패턴으로 가야 함
    public static final PacketCodec<RegistryByteBuf, DoorDataRes> PACKET_CODEC =
            PacketCodec.tuple(
                    DoorDataCodec.CODEC, DoorDataRes::doorData,
                    DoorDataRes::new
            ).cast();

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
