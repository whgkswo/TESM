package net.whgkswo.tesm.networking.payload.data.c2s_req;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.networking.codec.MixedMapCodecs;
import net.whgkswo.tesm.networking.payload.id.PayloadId;

import java.util.HashMap;
import java.util.Map;

public record SetNbtReq(int entityId, Map<String, Object> data) implements CustomPayload{
    // 패킷 식별자
    public static final CustomPayload.Id<SetNbtReq> PACKET_ID =
            new CustomPayload.Id<>(Identifier.of(TESMMod.MODID, PayloadId.SET_NBT_REQ.getId()));

    // 파라미터 -> 타입,밸류,타입,밸류, ... ,new 패턴으로 가야 함
    public static final PacketCodec<RegistryByteBuf, SetNbtReq> PACKET_CODEC =
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, SetNbtReq::entityId,
                    MixedMapCodecs.mixedMap(HashMap::new), SetNbtReq::data,
                    SetNbtReq::new
            ).cast();

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
