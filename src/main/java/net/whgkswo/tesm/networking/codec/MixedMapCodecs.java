package net.whgkswo.tesm.networking.codec;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

import java.util.Map;
import java.util.HashMap;
import java.util.function.Supplier;

public class MixedMapCodecs {
    /**
     * Object 값을 가진 맵을 위한 코덱을 만드는 메서드
     * @param mapFactory 맵을 생성하는 공급자 함수
     * @return 혼합 타입 맵을 위한 코덱
     */
    public static <M extends Map<String, Object>> PacketCodec<RegistryByteBuf, M> mixedMap(Supplier<M> mapFactory) {
        return new PacketCodec<>() {
            @Override
            public void encode(RegistryByteBuf buf, M value) {
                // 맵 크기 쓰기
                buf.writeVarInt(value.size());

                // 각 항목 쓰기
                for (Map.Entry<String, Object> entry : value.entrySet()) {
                    // 키 쓰기
                    buf.writeString(entry.getKey());

                    // 값의 타입에 따라 다르게 처리
                    Object val = entry.getValue();
                    if (val instanceof String str) {
                        buf.writeByte(0); // String 타입 코드
                        buf.writeString(str);
                    } else if (val instanceof Integer i) {
                        buf.writeByte(1); // Integer 타입 코드
                        buf.writeVarInt(i);
                    } else if (val instanceof Boolean b) {
                        buf.writeByte(2); // Boolean 타입 코드
                        buf.writeBoolean(b);
                    } else if (val == null) {
                        buf.writeByte(3); // null 타입 코드
                    } else {
                        // 지원되지 않는 타입
                        throw new IllegalArgumentException("Unsupported value type: " +
                                (val != null ? val.getClass().getName() : "null"));
                    }
                }
            }

            @Override
            public M decode(RegistryByteBuf buf) {
                // 맵 크기 읽기
                int size = buf.readVarInt();
                M map = mapFactory.get();

                // 각 항목 읽기
                for (int i = 0; i < size; i++) {
                    String key = buf.readString();
                    byte typeCode = buf.readByte();

                    // 타입 코드에 따라 적절한 값 읽기
                    Object value = switch (typeCode) {
                        case 0 -> buf.readString();       // String
                        case 1 -> buf.readVarInt();       // Integer
                        case 2 -> buf.readBoolean();      // Boolean
                        case 3 -> null;                   // null
                        default -> throw new IllegalStateException("Unknown type code: " + typeCode);
                    };

                    // 맵에 추가
                    map.put(key, value);
                }

                return map;
            }
        };
    }

    /**
     * 기본 HashMap을 사용하는 혼합 타입 맵 코덱
     */
    public static PacketCodec<RegistryByteBuf, Map<String, Object>> mixedMap() {
        return mixedMap(HashMap::new);
    }
}