package net.whgkswo.tesm.tags;

import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.whgkswo.tesm.TESMMod;

public class BiomeTags {

    public static final TagKey<Biome> TEST_TAG = TagKey.of(RegistryKeys.BIOME, new Identifier(TESMMod.MODID,"test_tag"));
    public static final TagKey<Biome> CYR_EXTERIORS = TagKey.of(RegistryKeys.BIOME, new Identifier(TESMMod.MODID,"cyr_exteriors"));
    public static final TagKey<Biome> MW_EXTERIORS = TagKey.of(RegistryKeys.BIOME, new Identifier(TESMMod.MODID,"mw_exteriors"));
    public static final TagKey<Biome> SKY_EXTERIORS = TagKey.of(RegistryKeys.BIOME, new Identifier(TESMMod.MODID,"sky_exteriors"));
}
