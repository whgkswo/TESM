package net.whgkswo.tesm.gui.overlay;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.colors.CustomColor;
import net.whgkswo.tesm.gui.component.TransitionStatus;
import net.whgkswo.tesm.gui.component.bounds.Boundary;
import net.whgkswo.tesm.gui.component.elements.TextPopUp;

public class QuestOverlay implements HudRenderCallback {
    private static TextPopUp content;

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        if(content != null && content.getStatus() != TransitionStatus.TERMINATED){
            content.render(drawContext);
        }
    }
    public static void setQuest(String questName){
        content = new TextPopUp(
                new Boundary(Boundary.BoundType.FIXED, 0.05, 0.5),
                new CustomColor(255,255,255, 0),
                questName, 1f, 140, 45);
    }
}
