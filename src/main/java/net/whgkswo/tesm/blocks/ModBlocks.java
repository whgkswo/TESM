package net.whgkswo.tesm.blocks;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
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
    public static final Block GRASSY_SOIL = registerBlock("grassy_soil",
            new Block(FabricBlockSettings.copyOf(Blocks.GRASS_BLOCK).sounds((BlockSoundGroup.GRASS))));
    public static final Block SNOWY_GRASS = registerBlock("snowy_grass",
            new Block(FabricBlockSettings.copyOf(Blocks.GRASS_BLOCK).sounds((BlockSoundGroup.GRASS))));
    public static final Block SNOWY_GRASS_LIGHT = registerBlock("snowy_grass_light",
            new Block(FabricBlockSettings.copyOf(Blocks.GRASS_BLOCK).sounds((BlockSoundGroup.GRASS))));
    public static final Block SNOWY_TUNDRA_GRASS = registerBlock("snowy_tundra_grass",
            new Block(FabricBlockSettings.copyOf(Blocks.GRASS_BLOCK).sounds((BlockSoundGroup.GRASS))));
    public static final Block STONE_ROAD = registerBlock("stone_road",
            new Block(FabricBlockSettings.copyOf(Blocks.STONE).sounds((BlockSoundGroup.STONE))));
    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(TESMMod.MODID,name), block);
    }
    private static Item registerBlockItem(String name, Block block){
        return Registry.register (Registries.ITEM, new Identifier(TESMMod.MODID, name),
            new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks(){
        TESMMod.LOGGER.info("Registering ModBlocks for " + TESMMod.MODID);
    }
}
