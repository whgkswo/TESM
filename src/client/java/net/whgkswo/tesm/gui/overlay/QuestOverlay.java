package net.whgkswo.tesm.gui.overlay;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.whgkswo.tesm.conversation.quest.objective.QuestObjective;
import net.whgkswo.tesm.gui.Alignment;
import net.whgkswo.tesm.gui.colors.CustomColor;
import net.whgkswo.tesm.gui.component.FadeSequence;
import net.whgkswo.tesm.gui.component.TransitionStatus;
import net.whgkswo.tesm.gui.component.bounds.Boundary;
import net.whgkswo.tesm.gui.component.bounds.RectangularBound;
import net.whgkswo.tesm.gui.component.elements.TextPopUp;
import net.whgkswo.tesm.gui.component.elements.TextPopUpV2;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class QuestOverlay implements HudRenderCallback {
    private static TextPopUpV2 eventType;
    private static TextPopUp questName;
    private static Set<TextPopUp> objectiveSet = new HashSet<>();

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        if(eventType != null){
            eventType.render(drawContext);
        }
        if(questName != null){
            questName.render(drawContext);
        }
        for(TextPopUp objective : objectiveSet){
            objective.render(drawContext);
        }
    }
    public static void displayStartPopUp(String type, String questName, Map<String, QuestObjective> objectives){

        /*eventType = new TextPopUp(
                new Boundary(Boundary.BoundType.FIXED, 0.05, 0.45),
                new CustomColor(255,255,255),
                Text.literal(type), 1.2f,
                new FadeSequence(16,248,  19));*/
        eventType = new TextPopUpV2(CustomColor.ColorsPreset.WHITE.getColor(),
                Text.literal(type), 1.2f, Alignment.LEFT,
                new RectangularBound(Boundary.BoundType.FIXED, 0.05, 0.45, 0.1, 0.05),
                0, new FadeSequence(16, 248, 19));
        QuestOverlay.questName = new TextPopUp(
                new Boundary(Boundary.BoundType.FIXED, 0.05, 0.5),
                new CustomColor(255,255,255),
                Text.literal(questName), 1f,
                new FadeSequence(50,186, 40));

        int i = 0;
        for(QuestObjective objective : objectives.values()){
            objectiveSet.add(new TextPopUp(
                    new Boundary(Boundary.BoundType.FIXED, 0.05, 0.55 + i * 0.04),
                            CustomColor.ColorsPreset.RODEO_DUST.getColor(),
                            Text.literal(i == 0 ? objective.getDescription() : "또는 " + objective.getDescription()),
                    0.7f,
                    new FadeSequence(20,252, 20)
            ));
            i++;
        }
    }
    public static void displayAdvancePopUp(String object, Map<String, QuestObjective> nextObjectives){
        int i = 0;
        /*eventType = new TextPopUp(
                new Boundary(Boundary.BoundType.FIXED, 0.05, 0.45),
                new CustomColor(150,150,150),
                Text.literal(object).styled(style -> style.withStrikethrough(true)),
                0.7f, new FadeSequence(20, 160,20)
        );*/
        eventType = new TextPopUpV2(new CustomColor(150,150,150),
                Text.literal(object).styled(style -> style.withStrikethrough(true)),
                0.7f, Alignment.LEFT,
                new RectangularBound(Boundary.BoundType.FIXED, 0.05, 0.45, 0.1, 0.05),
                0, new FadeSequence(20, 160, 20)
                );
        for(QuestObjective objective : nextObjectives.values()){
            objectiveSet.add(new TextPopUp(new Boundary(Boundary.BoundType.FIXED, 0.05, 0.45 + i * 0.04),
                    CustomColor.ColorsPreset.RODEO_DUST.getColor(),
                    Text.literal(i == 0 ? objective.getDescription() : "또는 " + objective.getDescription()),
                    0.7f, new FadeSequence(200, 20, 252,20)));
            i++;
        }
    }
    public static void displayCompletePopUp(String type, String questName){
        /*eventType = new TextPopUp(
                new Boundary(Boundary.BoundType.FIXED, 0.05, 0.45),
                new CustomColor(255,255,255),
                Text.literal(type), 1.2f, new FadeSequence(16, 235, 35));*/
        eventType = new TextPopUpV2(CustomColor.ColorsPreset.WHITE.getColor(),
                Text.literal(type), 1.2f, Alignment.LEFT,
                new RectangularBound(Boundary.BoundType.FIXED, 0.1, 0.45, 0.1, 0.05),
                0, new FadeSequence(16, 235, 35));
        QuestOverlay.questName = new TextPopUp(
                new Boundary(Boundary.BoundType.FIXED, 0.05, 0.5),
                new CustomColor(255,255,255),
                Text.literal(questName), 1f, new FadeSequence(50,205,40));
    }
}
