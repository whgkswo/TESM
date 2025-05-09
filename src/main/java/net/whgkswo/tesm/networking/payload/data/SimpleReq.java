package net.whgkswo.tesm.networking.payload.data;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.networking.payload.id.SimpleTask;
import net.whgkswo.tesm.networking.payload.id.PayloadId;

public record SimpleReq(SimpleTask task) implements CustomPayload {
    // 패킷 식별자
    public static final CustomPayload.Id<SimpleReq> PACKET_ID =
            new CustomPayload.Id<>(Identifier.of(TESMMod.MODID, PayloadId.SIMPLE_REQ.getLowercaseName()));

    // 파라미터 -> 타입,밸류,타입,밸류, ... ,new 패턴으로 가야 함
    public static final PacketCodec<RegistryByteBuf, SimpleReq> PACKET_CODEC =
            PacketCodec.tuple(
                    PacketCodecs.STRING, SimpleReq::getTaskId,
                    taskId -> new SimpleReq(SimpleTask.valueOf(taskId))
            ).cast();

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    public String getTaskId(){
        return task.name();
    }
}
