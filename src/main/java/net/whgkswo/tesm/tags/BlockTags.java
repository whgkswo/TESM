package net.whgkswo.tesm.tags;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;

public class BlockTags {
    public static final TagKey<Block> DOORS = TagKey.of(RegistryKeys.BLOCK, Identifier.of(TESMMod.MODID, "doors"));
    public static final TagKey<Block> TRAP_BLOCK = TagKey.of(RegistryKeys.BLOCK, Identifier.of(TESMMod.MODID, "trap_blocks"));
    public static final TagKey<Block> WALKABLE_BLOCKS = TagKey.of(RegistryKeys.BLOCK, Identifier.of(TESMMod.MODID, "walkable_blocks")); // 사용 안함. 블록이 solid한지 판별하는 메소드가 있음
}
