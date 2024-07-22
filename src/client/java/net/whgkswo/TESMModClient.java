package net.whgkswo;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.TestClassClient;
import net.whgkswo.tesm.conversation.ConversationStart;
import net.whgkswo.tesm.conversation.quest.Quest;
import net.whgkswo.tesm.events.UseBlockEvent;
import net.whgkswo.tesm.general.OnClientTicks;
import net.whgkswo.tesm.gui.overlay.Compass;
import net.whgkswo.tesm.gui.overlay.InteractOverlay;
import net.whgkswo.tesm.gui.overlay.QuestStartAndClear;
import net.whgkswo.tesm.gui.overlay.Watch;
import net.whgkswo.tesm.keybinds.KeyInputHandler;
import net.whgkswo.tesm.networking.ModMessages;
import net.whgkswo.tesm.networking.ModMessagesClient;
import net.whgkswo.tesm.raycast.CenterRaycast;



public class TESMModClient implements ClientModInitializer {

	//private static final Identifier FREEZE_ENTITY_PACKET_ID = new Identifier(TESMMod.MODID,"freeze_entity");

	private static int getColorForGrassyBlock(net.minecraft.world.BlockRenderView world, BlockPos pos) {
		// 예시: 기본적으로 잔디 블록의 색상 틴트를 사용합니다.
		int baseColor = BiomeColors.getGrassColor(world, pos);

		// 색상 강도를 낮추고 투명도를 높이기
		int red = (int) (1.2 * ((baseColor >> 16) & 255));
		int green = (int) (1.2 * ((baseColor >> 8) & 255));
		int blue = (int) (1.2* (baseColor & 255));
		int alpha = (int) (9.6 * ((baseColor >> 24) & 255));

		// 각 색상 채널을 0-255 범위 내로 조정
		red = Math.min(Math.max(red, 0), 255);
		green = Math.min(Math.max(green, 0), 255);
		blue = Math.min(Math.max(blue, 0), 255);
		alpha = Math.min(Math.max(alpha, 0), 255);

		// 최종 색상 반환
		return (alpha << 24) | (red << 16) | (green << 8) | blue;
	}

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 컬러맵 등록 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

		/*ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
			//return getColorForGrassyBlock(world, pos);
			return BiomeColors.getGrassColor(world,pos);
		},GRASSY_SOIL);*/

		//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 블록 투명성 등록 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		//BlockRenderLayerMap.INSTANCE.putBlock(GRASSY_SOIL, RenderLayer.getTranslucent());

		//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ GUI 등록 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

		HudRenderCallback.EVENT.register(new InteractOverlay());
		HudRenderCallback.EVENT.register(new Compass());
		HudRenderCallback.EVENT.register(new QuestStartAndClear());
		HudRenderCallback.EVENT.register(new Watch());

		//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 클라이언트 사이드 메소드 등록 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		CenterRaycast.centerRaycast();
		ConversationStart.checkCondition();
		OnClientTicks.getArrowState();
		//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 클라이언트 사이드 이벤트 등록 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		UseBlockEvent.register();

		//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 서버 통신 패킷 등록 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		ModMessages.registerS2CPackets(); // 서버로 송신
		ModMessagesClient.registerS2CPackets(); // 결과값 처리

		//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 키바인딩 등록 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		KeyInputHandler.register();

		//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 퀘스트 등록 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		Quest.registerQuests();

		TestClassClient.testClassClient(); // 테스트용!
	}
}