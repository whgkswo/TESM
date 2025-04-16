package net.whgkswo.tesm.networking.payload.data.s2c_res;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.networking.payload.id.PayloadId;

import java.util.UUID;

public record ConversationNbtRes(String engName, String tempName, String name, int partnerId) implements CustomPayload {
    // 패킷 식별자
    public static final CustomPayload.Id<ConversationNbtRes> PACKET_ID =
            new CustomPayload.Id<>(Identifier.of(TESMMod.MODID, PayloadId.CONVERSATION_NBT_RES.getLowercaseName()));

    // 패킷 코덱
    public static final PacketCodec<RegistryByteBuf, ConversationNbtRes> PACKET_CODEC =
            PacketCodec.tuple(
                    PacketCodecs.STRING, ConversationNbtRes::engName,
                    PacketCodecs.STRING, ConversationNbtRes::tempName,
                    PacketCodecs.STRING, ConversationNbtRes::name,
                    PacketCodecs.VAR_INT, ConversationNbtRes::partnerId,
                    ConversationNbtRes::new
            ).cast();

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
