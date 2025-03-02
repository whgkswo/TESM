package net.whgkswo.tesm.networking.payload.data.s2c_res;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.networking.payload.id.PayloadId;

public record ConversationNbtRes(String tempName, String name) implements CustomPayload {
    // 패킷 식별자
    public static final CustomPayload.Id<ConversationNbtRes> PACKET_ID =
            new CustomPayload.Id<>(Identifier.of(TESMMod.MODID, PayloadId.CONVERSATION_NBT_RES.getId()));

    // 패킷 코덱
    public static final PacketCodec<RegistryByteBuf, ConversationNbtRes> PACKET_CODEC =
            PacketCodec.tuple(
                    PacketCodecs.STRING, ConversationNbtRes::tempName,
                    PacketCodecs.STRING, ConversationNbtRes::name,
                    ConversationNbtRes::new
            ).cast();

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
