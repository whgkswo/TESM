package net.whgkswo.tesm.items;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.items.features.MagicWand;

import java.util.function.Function;

public class ModItems {
    public static final Item MAGIC_WAND = register("magic_wand", MagicWand::new, new Item.Settings());

    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        TESMMod.LOGGER.info("커스텀 아이템 등록");

        // 아이템 키 생성
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(TESMMod.MODID, name));
        return Items.register(itemKey, itemFactory, settings);
    }

    public static void registerItems(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register((itemgroup) -> itemgroup.add(ModItems.MAGIC_WAND));
    }
}
