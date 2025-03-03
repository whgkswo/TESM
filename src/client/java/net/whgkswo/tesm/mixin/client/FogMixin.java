package net.whgkswo.tesm.mixin.client;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.FogShape;
import net.minecraft.text.Text;
import net.whgkswo.tesm.fog.FogHelper;
import net.whgkswo.tesm.general.GlobalVariables;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BackgroundRenderer.class)
public class FogMixin {
    @Inject(method = "applyFog", at = @At("RETURN"), cancellable = true)
    private static void onApplyFog(Camera camera, BackgroundRenderer.FogType fogType, Vector4f color,
                                   float viewDistance, boolean thickenFog, float tickDelta,
                                   CallbackInfoReturnable<Fog> cir) {
        // 기존 안개
        Fog originalFog = cir.getReturnValue();

        // 새로운 안개 설정 생성
        Fog customFog = FogHelper.getFog();

        GlobalVariables.player.sendMessage(Text.literal(String.format("현재 안개 농도: %f", customFog.alpha())), true);
        cir.setReturnValue(customFog);
        /*// 커스텀 안개가 더 짙을 때에만 반환값을 새 안개로 변경
        if(customFog.start() < originalFog.start()) {
            GlobalVariables.player.sendMessage(Text.literal(String.format("아침 안개 농도: %f", customFog.alpha())), true);
            cir.setReturnValue(customFog);
        }else{
            GlobalVariables.player.sendMessage(Text.literal(String.format("현재 안개 농도: %f", originalFog.alpha())), true);
        };*/
    }
}
