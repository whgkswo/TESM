package net.whgkswo.tesm.networking.codec;


import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

    public class BlockHitResultCodec {
        /**
         * BlockHitResult를 위한 코덱
         */
        public static final PacketCodec<RegistryByteBuf, BlockHitResult> CODEC = new PacketCodec<>() {
            @Override
            public void encode(RegistryByteBuf buf, BlockHitResult value) {
                // 위치 벡터 작성
                buf.writeDouble(value.getPos().x);
                buf.writeDouble(value.getPos().y);
                buf.writeDouble(value.getPos().z);

                // 방향 작성
                buf.writeEnumConstant(value.getSide());

                // 블록 위치 작성
                buf.writeBlockPos(value.getBlockPos());

                // 내부 충돌 여부 작성
                buf.writeBoolean(value.isInsideBlock());
            }

            @Override
            public BlockHitResult decode(RegistryByteBuf buf) {
                // 위치 벡터 읽기
                double x = buf.readDouble();
                double y = buf.readDouble();
                double z = buf.readDouble();
                Vec3d pos = new Vec3d(x, y, z);

                // 방향 읽기
                Direction direction = buf.readEnumConstant(Direction.class);

                // 블록 위치 읽기
                BlockPos blockPos = buf.readBlockPos();

                // 내부 충돌 여부 읽기
                boolean insideBlock = buf.readBoolean();

                // BlockHitResult 생성 및 반환
                return new BlockHitResult(pos, direction, blockPos, insideBlock);
            }
        };
}
