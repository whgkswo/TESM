package net.whgkswo.tesm.networking.payload.c2s_req;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;

public record ConversationNbtReq(int entityId) implements CustomPayload {
    // 패킷 식별자
    public static final CustomPayload.Id<ConversationNbtReq> PACKET_ID =
            new CustomPayload.Id<>(Identifier.of(TESMMod.MODID, "conversation_nbt_request"));

    // 파라미터 -> 타입,밸류,타입,밸류, ... ,new 패턴으로 가야 함
    public static final PacketCodec<RegistryByteBuf, ConversationNbtReq> PACKET_CODEC =
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, ConversationNbtReq::entityId,
                    ConversationNbtReq::new
            ).cast();

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
