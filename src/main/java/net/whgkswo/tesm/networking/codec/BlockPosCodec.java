package net.whgkswo.tesm.networking.codec;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;

public class BlockPosCodec {
    /**
     * BlockPos를 위한 코덱
     */
    public static final PacketCodec<RegistryByteBuf, BlockPos> CODEC = new PacketCodec<>() {
        @Override
        public void encode(RegistryByteBuf buf, BlockPos value) {
            // BlockPos의 x, y, z 좌표를 패킷 버퍼에 기록
            buf.writeVarInt(value.getX());
            buf.writeVarInt(value.getY());
            buf.writeVarInt(value.getZ());
        }

        @Override
        public BlockPos decode(RegistryByteBuf buf) {
            // 패킷 버퍼에서 x, y, z 좌표를 읽어 BlockPos 객체 생성
            int x = buf.readVarInt();
            int y = buf.readVarInt();
            int z = buf.readVarInt();
            return new BlockPos(x, y, z);
        }
    };
}