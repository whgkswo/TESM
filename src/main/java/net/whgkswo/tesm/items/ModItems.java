package net.whgkswo.tesm.items;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;

import java.util.function.Function;

public class ModItems {
    public static final Item MAGIC_TOOL = register("magic_tool",Item::new, new Item.Settings());

    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        // 아이템 키 생성
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(TESMMod.MODID, name));

        // 아이템 객체 생성
        Item item = itemFactory.apply(settings.registryKey(itemKey));

        // 아이템 등록
        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }

    public static void initialize(){

    }
}
