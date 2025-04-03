package net.whgkswo.tesm.networking.payload.data.c2s_req;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.networking.codec.BlockPosCodec;
import net.whgkswo.tesm.networking.payload.id.PayloadId;

public record BlockDataReq(BlockPos blockPos) implements CustomPayload {
    // 패킷 식별자
    public static final CustomPayload.Id<BlockDataReq> PACKET_ID =
            new CustomPayload.Id<>(Identifier.of(TESMMod.MODID, PayloadId.BLOCK_DATA_REQ.getLowercaseName()));

    // 파라미터 -> 타입,밸류,타입,밸류, ... ,new 패턴으로 가야 함
    public static final PacketCodec<RegistryByteBuf, BlockDataReq> PACKET_CODEC =
            PacketCodec.tuple(
                    BlockPosCodec.CODEC, BlockDataReq::blockPos,
                    BlockDataReq::new
            ).cast();

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
