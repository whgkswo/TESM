package net.whgkswo.tesm.blocks.blockentity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;

public class ModBlockEntityTypes {
    public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(TESMMod.MODID, path), blockEntityType);
    }

    public static final BlockEntityType<DoorBlockEntity> DOOR = register(
            "door",
            // For versions before 1.21.2, please use BlockEntityType.Builder.
            FabricBlockEntityTypeBuilder.create(DoorBlockEntity::new,
                    Blocks.OAK_DOOR,
                    Blocks.OAK_TRAPDOOR
            ).build()
    );

    public static void initialize() {
    }
}
