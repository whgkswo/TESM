package net.whgkswo.tesm.gui.overlay;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.whgkswo.tesm.calendar.InGameTime;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.RenderingHelper;

public class Watch implements HudRenderCallback {
    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        renderTime(drawContext);
    }
    private void renderTime(DrawContext drawContext){
        final float scale = 0.7f;

        // 시간 출력
        String time = GlobalVariables.currentInGameTime.toString(InGameTime.TimeFormat.LINGUAL_12H, false);
        RenderingHelper.renderText(HorizontalAlignment.RIGHT, drawContext,scale,
                time, 0.95 ,0.95, 0xffffff);
        // 날짜 출력
        String date = GlobalVariables.currentInGameDate.toString();
        RenderingHelper.renderText(HorizontalAlignment.RIGHT, drawContext,scale,
                date, 0.95,0.91,0xffffff);
    }
}
