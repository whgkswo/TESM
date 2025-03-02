package net.whgkswo.tesm.gui.overlay;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.whgkswo.tesm.conversation.quest.objective.QuestObjective;
import net.whgkswo.tesm.gui.Alignment;
import net.whgkswo.tesm.gui.colors.CustomColor;
import net.whgkswo.tesm.gui.component.FadeSequence;
import net.whgkswo.tesm.gui.component.TransitionStatus;
import net.whgkswo.tesm.gui.component.bounds.Boundary;
import net.whgkswo.tesm.gui.component.bounds.RectangularBound;
import net.whgkswo.tesm.gui.component.elements.TextPopUpV2;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class QuestOverlay implements HudRenderCallback {
    private static TextPopUpV2 eventType;
    private static TextPopUpV2 eventName;
    private static Set<TextPopUpV2> objectiveSet = new HashSet<>();

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        if(eventType != null){
            eventType.render(drawContext);
            if(eventType.getStatus() == TransitionStatus.TERMINATED){
                eventType = null;
            }
        }
        if(eventName != null){
            eventName.render(drawContext);
            if(eventName.getStatus() == TransitionStatus.TERMINATED){
                eventName = null;
            }
        }
        for(TextPopUpV2 objective : objectiveSet){
            objective.render(drawContext);
        }
    }
    public static void displayStartPopUp(String type, String questName, Map<String, QuestObjective> objectives){
        eventType = new TextPopUpV2(CustomColor.ColorsPreset.WHITE.getColor(),
                Text.literal(type), 1.2f, Alignment.LEFT,
                new RectangularBound(Boundary.BoundType.FIXED, 0.05, 0.45, 0.1, 0.05),
                0, new FadeSequence(16, 248, 19));

        eventName = new TextPopUpV2(CustomColor.ColorsPreset.WHITE.getColor(),
                Text.literal(questName), 1f, Alignment.LEFT,
                new RectangularBound(Boundary.BoundType.FIXED, 0.05, 0.5, 0.1, 0.05),
                0, new FadeSequence(50, 186, 40)
        );

        objectiveSet.clear();
        int i = 0;
        for(QuestObjective objective : objectives.values()){
            objectiveSet.add(new TextPopUpV2(CustomColor.ColorsPreset.RODEO_DUST.getColor(),
                    Text.literal(i == 0 ? objective.getDescription() : "또는 " + objective.getDescription()),
                    0.7f, Alignment.LEFT,
                    new RectangularBound(Boundary.BoundType.FIXED, 0.05, 0.55 + i * 0.04, 0.1, 0.05),
                    0, new FadeSequence(20,252, 20)
                    ));
            i++;
        }
    }
    public static void displayAdvancePopUp(String object, Map<String, QuestObjective> nextObjectives){
        int i = 0;
        eventType = new TextPopUpV2(new CustomColor(150,150,150),
                Text.literal(object).styled(style -> style.withStrikethrough(true)),
                0.7f, Alignment.LEFT,
                new RectangularBound(Boundary.BoundType.FIXED, 0.05, 0.45, 0.1, 0.05),
                0, new FadeSequence(20, 160, 20)
                );
        objectiveSet.clear();
        for(QuestObjective objective : nextObjectives.values()){
            objectiveSet.add(new TextPopUpV2(CustomColor.ColorsPreset.RODEO_DUST.getColor(),
                    Text.literal(i == 0 ? objective.getDescription() : "또는 " + objective.getDescription()),
                    0.7f, Alignment.LEFT,
                    new RectangularBound(Boundary.BoundType.FIXED, 0.05, 0.45 + i * 0.04, 0.1, 0.05),
                    0, new FadeSequence(200, 20, 252,20)
                    ));
            i++;
        }
    }
    public static void displayCompletePopUp(String type, String questName){
        eventType = new TextPopUpV2(CustomColor.ColorsPreset.WHITE.getColor(),
                Text.literal(type), 1.2f, Alignment.LEFT,
                new RectangularBound(Boundary.BoundType.FIXED, 0.05, 0.45, 0.1, 0.05),
                0, new FadeSequence(16, 235, 35));
        eventName = new TextPopUpV2(CustomColor.ColorsPreset.WHITE.getColor(),
                Text.literal(questName), 1f, Alignment.LEFT,
                new RectangularBound(Boundary.BoundType.FIXED, 0.05, 0.5, 0.1, 0.05),
                0, new FadeSequence(50, 205, 40)
                );
    }
}
