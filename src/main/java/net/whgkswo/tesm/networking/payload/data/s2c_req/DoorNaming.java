package net.whgkswo.tesm.networking.payload.data.s2c_req;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.networking.codec.BlockPosCodec;
import net.whgkswo.tesm.networking.payload.id.PayloadId;

public record DoorNaming(BlockPos blockPos, String insideName, String outsideName, boolean pushToOutside) implements CustomPayload {
    // 패킷 식별자
    public static final CustomPayload.Id<DoorNaming> PACKET_ID =
            new CustomPayload.Id<>(Identifier.of(TESMMod.MODID, PayloadId.DOOR_NAMING.getLowercaseName()));

    // 파라미터 -> 타입,밸류,타입,밸류, ... ,new 패턴으로 가야 함
    public static final PacketCodec<RegistryByteBuf, DoorNaming> PACKET_CODEC =
            PacketCodec.tuple(
                    BlockPosCodec.CODEC, DoorNaming::blockPos,
                    PacketCodecs.STRING, DoorNaming::insideName,
                    PacketCodecs.STRING, DoorNaming::outsideName,
                    PacketCodecs.BOOLEAN, DoorNaming::pushToOutside,
                    DoorNaming::new
            ).cast();

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
