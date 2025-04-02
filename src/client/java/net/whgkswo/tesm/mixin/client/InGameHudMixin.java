package net.whgkswo.tesm.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.whgkswo.tesm.conversation.ConversationStart;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// 대화창이 열려 있을 때 HUD렌더링 비활성화
// TODO: 대화창 이외에도 어떤 스크린이 열려 있다면 HUD 비활성화 고려
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow @Final private MinecraftClient client;

    @Shadow protected abstract void renderVignetteOverlay(DrawContext context, @Nullable Entity entity);

    // 전체 HUD 비활성화 (비네팅 효과까지 제거)
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (ConversationStart.convOn) {
            ci.cancel(); // HUD 렌더링 취소

            if(this.client.player != null){
                // 비네팅은 수동으로 다시 그리기
                this.renderVignetteOverlay(context, this.client.player);
            }
        }
    }
}
