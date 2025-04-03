package net.whgkswo.tesm.networking.payload.data.c2s_req;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.networking.codec.BlockPosCodec;
import net.whgkswo.tesm.networking.payload.id.PayloadId;

public record UseBlockReq(BlockPos blockPos) implements CustomPayload {
    // 패킷 식별자
    public static final CustomPayload.Id<UseBlockReq> PACKET_ID =
            new CustomPayload.Id<>(Identifier.of(TESMMod.MODID, PayloadId.USE_BLOCK_REQ.getLowercaseName()));

    // 파라미터 -> 타입,밸류,타입,밸류, ... ,new 패턴으로 가야 함
    public static final PacketCodec<RegistryByteBuf, UseBlockReq> PACKET_CODEC =
            PacketCodec.tuple(
                    BlockPosCodec.CODEC, UseBlockReq::blockPos,
                    UseBlockReq::new
            ).cast();

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
