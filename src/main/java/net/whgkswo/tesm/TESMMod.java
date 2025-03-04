package net.whgkswo.tesm;

import net.fabricmc.api.ModInitializer;
import net.whgkswo.tesm.commands.*;
import net.whgkswo.tesm.general.InitializeTasks;
import net.whgkswo.tesm.general.OnServerTicks;
import net.whgkswo.tesm.general.OnPlayerLeaves;
import net.whgkswo.tesm.networking.ServerNetworkManager;
import net.whgkswo.tesm.pathfinding.v1.PathFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class TESMMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MODID = "tesm";
	public static final Logger LOGGER = LoggerFactory.getLogger(TESMMod.MODID);

	// TODO: 포팅
	/*public static final TestItem TEST_ITEM =
			Registry.register(Registries.ITEM, Identifier.of(TESMMod.MODID,"test_item"),
					new TestItem(new FabricItemSettings()));*/

	// TODO: 포팅
	/*private static final ItemGroup TESASSETS = FabricItemGroup.builder()
			.icon(() -> new ItemStack(TEST_ITEM))
			.displayName(Text.translatable("TESAssets"))
			.entries((context, entries) -> {
				entries.add(ModBlocks.GRASSY_SOIL);
				entries.add(ModBlocks.SNOWY_GRASS);
				entries.add(ModBlocks.SNOWY_GRASS_LIGHT);
				entries.add(ModBlocks.SNOWY_TUNDRA_GRASS);
				entries.add(ModBlocks.STONE_ROAD);
			})
			.build();*/
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		// TODO: 포팅
		/*ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> {
			content.add(TEST_ITEM);
		});*/

		//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 레지스트리 등록 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

		// TODO: 포팅
		/*Registry.register(Registries.ITEM_GROUP,
				Identifier.of(TESMMod.MODID, "tesassets"), TESASSETS);*/

        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 커스텀 커맨드 등록 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

		ToggleTimeflow.register();
		SummonNpc.register();
		ScanCommand.register();
		ResetQuests.register();
		Maze.register();

		//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 이벤트 및 메소드 등록 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

		// TODO: 포팅
		//ModBlocks.registerModBlocks();
		InitializeTasks.registerPlayer();
		InitializeTasks.onServerStarted();
		OnServerTicks.onServerTick();
		PathFinder.onServerTicks();
		/*PathfindingManager pathfindingManager = new PathfindingManager();
		pathfindingManager.onServerTicks();*/
		OnPlayerLeaves.onPlayerLeaves();

		//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 네트워킹 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		ServerNetworkManager.registerC2SCodecs(); // 코덱 등록
		// 주의: 리시버는 항상 코덱이 먼저 등록된 후에 등록되어야 함
		ServerNetworkManager.registerC2SReceivers();


		//TestClass.testClass(); // 테스트용!
		// TODO: 은신 시스템
		// TODO: 문 오버레이
		// TODO: 마법봉
		// TODO: 디버깅 메시지
		// TODO: 매드 다이스, 매드 포커
		// TODO: 대화 시스템 UI 리디자인
		// TODO: 대화 시스템 JSON 리뉴얼
	}
}