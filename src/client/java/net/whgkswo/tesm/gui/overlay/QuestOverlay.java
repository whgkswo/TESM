package net.whgkswo.tesm.gui.overlay;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.colors.CustomColor;
import net.whgkswo.tesm.gui.component.TransitionStatus;
import net.whgkswo.tesm.gui.component.bounds.Boundary;
import net.whgkswo.tesm.gui.component.elements.TextPopUp;

public class QuestOverlay implements HudRenderCallback {
    private static TextPopUp eventType;
    private static TextPopUp questName;

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        if(eventType != null && eventType.getStatus() != TransitionStatus.TERMINATED){
            eventType.render(drawContext);
        }
        if(questName != null && questName.getStatus() != TransitionStatus.TERMINATED){
            questName.render(drawContext);
        }
    }
    public static void displayPopUp(String type, String questName){
        eventType = new TextPopUp(
                new Boundary(Boundary.BoundType.FIXED, 0.05, 0.45),
                new CustomColor(255,255,255),
                type, 1.2f, 198, 16, 19);
        QuestOverlay.questName = new TextPopUp(
                new Boundary(Boundary.BoundType.FIXED, 0.05, 0.5),
                new CustomColor(255,255,255),
                questName, 1f, 140, 50,40);
    }
}
