package net.whgkswo.tesm.data;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import net.minecraft.util.math.BlockPos;

import java.io.IOException;

public class BlockPosKeyDeserializer extends KeyDeserializer {
    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        // "BlockPos{x=-10, y=77, z=5}"
        String[] parts = key.replace("BlockPos{x=", "").replace("}", "").split(",");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1].replace(" y=", ""));
        int z = Integer.parseInt(parts[2].replace(" z=", ""));
        return new BlockPos(x, y, z);
    }
}