package net.whgkswo.tesm.gui.overlay;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.calendar.Time;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.gui.RenderUtil;

public class Watch implements HudRenderCallback {
    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        renderTime(drawContext);
    }
    private void renderTime(DrawContext drawContext){
        final float scale = 0.7f;

        // 시간 출력
        String time = GlobalVariables.currentTime.toString(Time.TimeFormat.LINGUAL_12H, true);
        RenderUtil.renderText(RenderUtil.Alignment.RIGHT, drawContext,scale,
                time, 0.95 ,0.95, 0xffffff);
        // 날짜 출력
        String date = GlobalVariables.currentDate.toString();
        RenderUtil.renderText(RenderUtil.Alignment.RIGHT, drawContext,scale,
                date, 0.95,0.91,0xffffff);
    }
}
