package net.whgkswo;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.TestClassClient;
import net.whgkswo.tesm.conversation.ConversationHelper;
import net.whgkswo.tesm.events.UseBlockEvent;
import net.whgkswo.tesm.general.ClientEvents;
import net.whgkswo.tesm.gui.overlay.*;
import net.whgkswo.tesm.keybinds.KeyInputHandler;
import net.whgkswo.tesm.networking.ClientNetworkManager;
import net.whgkswo.tesm.gui.overlay.raycast.HUDRaycastHelper;



public class TESMModClient implements ClientModInitializer {

	//private static final Identifier FREEZE_ENTITY_PACKET_ID = Identifier.of(TESMMod.MODID,"freeze_entity");

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
		HudRenderCallback.EVENT.register(new QuestOverlay());

		//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 클라이언트 사이드 메소드 등록 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		HUDRaycastHelper.centerRaycast();
		ConversationHelper.registerUseEntityEventHandler();
		ClientEvents.getArrowState();
		//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 클라이언트 사이드 이벤트 등록 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		UseBlockEvent.register();
		ClientEvents.onGameStart();

		//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 네트워킹 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		ClientNetworkManager.registerS2CCodecs(); // 코덱 등록
		// 주의: 리시버는 항상 코덱이 등록된 후에 등록돼야 함
		ClientNetworkManager.registerS2CReceivers(); // 리시버 등록

		//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 키바인딩 등록 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		KeyInputHandler.register();

		TestClassClient.testClassClient(); // 테스트용!
	}
}