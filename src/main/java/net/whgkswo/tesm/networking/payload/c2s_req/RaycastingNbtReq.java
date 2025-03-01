package net.whgkswo.tesm.networking.payload.c2s_req;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;

public record RaycastingNbtReq(int entityId) implements CustomPayload {
    // 패킷 식별자
    public static final CustomPayload.Id<RaycastingNbtReq> PACKET_ID =
            new CustomPayload.Id<>(Identifier.of(TESMMod.MODID, "raycasting_nbt_request"));

    // 파라미터 -> 타입,밸류,타입,밸류, ... ,new 패턴으로 가야 함
    public static final PacketCodec<RegistryByteBuf, RaycastingNbtReq> PACKET_CODEC =
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, RaycastingNbtReq::entityId,
                    RaycastingNbtReq::new
            ).cast();

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
