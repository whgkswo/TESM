package net.whgkswo.tesm.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;

public class ModBlocks {
    // TODO: 포팅
    public static final Block GRASSY_SOIL = registerBlock("grassy_soil",
            new Block(AbstractBlock.Settings.copy(Blocks.GRASS_BLOCK).sounds(BlockSoundGroup.GRASS)));
    public static final Block SNOWY_GRASS = registerBlock("snowy_grass",
            new Block(AbstractBlock.Settings.copy(Blocks.GRASS_BLOCK).sounds(BlockSoundGroup.GRASS)));
    public static final Block SNOWY_GRASS_LIGHT = registerBlock("snowy_grass_light",
            new Block(AbstractBlock.Settings.copy(Blocks.GRASS_BLOCK).sounds(BlockSoundGroup.GRASS)));
    public static final Block SNOWY_TUNDRA_GRASS = registerBlock("snowy_tundra_grass",
            new Block(AbstractBlock.Settings.copy(Blocks.GRASS_BLOCK).sounds(BlockSoundGroup.GRASS)));
    public static final Block STONE_ROAD = registerBlock("stone_road",
            new Block(AbstractBlock.Settings.copy(Blocks.STONE).sounds(BlockSoundGroup.GRASS)));
    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(TESMMod.MODID,name), block);
    }
    private static Item registerBlockItem(String name, Block block){
        return Registry.register (Registries.ITEM, Identifier.of(TESMMod.MODID, name),
            new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks(){
        TESMMod.LOGGER.info("Registering ModBlocks for " + TESMMod.MODID);
    }
}
