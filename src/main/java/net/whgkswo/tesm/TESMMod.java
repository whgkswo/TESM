package net.whgkswo.tesm;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.blocks.ModBlocks;
import net.whgkswo.tesm.commands.ScanCommand;
import net.whgkswo.tesm.commands.SummonVillager;
import net.whgkswo.tesm.commands.ToggleTimeflow;
import net.whgkswo.tesm.general.InitializeTasks;
import net.whgkswo.tesm.general.OnServerTicks;
import net.whgkswo.tesm.general.OnPlayerLeaves;
import net.whgkswo.tesm.items.TestItem;
import net.whgkswo.tesm.musics.MusicPlayer;
import net.whgkswo.tesm.networking.ModMessages;
import net.whgkswo.tesm.pathfinding.v1.PathFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class TESMMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MODID = "tesm";
	public static final Logger LOGGER = LoggerFactory.getLogger(TESMMod.MODID);
	public static final TestItem TEST_ITEM =
			Registry.register(Registries.ITEM, new Identifier(TESMMod.MODID,"test_item"),
					new TestItem(new FabricItemSettings()));
	private static final ItemGroup TESASSETS = FabricItemGroup.builder()
			.icon(() -> new ItemStack(TEST_ITEM))
			.displayName(Text.translatable("TESAssets"))
			.entries((context, entries) -> {
				entries.add(ModBlocks.GRASSY_SOIL);
				entries.add(ModBlocks.SNOWY_GRASS);
				entries.add(ModBlocks.SNOWY_GRASS_LIGHT);
				entries.add(ModBlocks.SNOWY_TUNDRA_GRASS);
				entries.add(ModBlocks.STONE_ROAD);
			})
			.build();
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> {
			content.add(TEST_ITEM);
		});

		//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 레지스트리 등록 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

		Registry.register(Registries.ITEM_GROUP,
				new Identifier(TESMMod.MODID, "tesassets"), TESASSETS);

        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 커스텀 커맨드 등록 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

		ToggleTimeflow.register();
		SummonVillager.register();
		ScanCommand.register();

		//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 이벤트 및 메소드 등록 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

		ModBlocks.registerModBlocks();
		InitializeTasks.registerPlayer();
		OnServerTicks.onServerTick();
		PathFinder.onServerTicks();
		/*PathfindingManager pathfindingManager = new PathfindingManager();
		pathfindingManager.onServerTicks();*/
		OnPlayerLeaves.onPlayerLeaves();

		new MusicPlayer().onServerTick();

		//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 클라이언트 통신 패킷 등록 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		ModMessages.registerC2SPackets();

		//TestClass.testClass(); // 테스트용!
	}
}