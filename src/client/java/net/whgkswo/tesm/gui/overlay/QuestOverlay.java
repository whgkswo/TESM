package net.whgkswo.tesm.gui.overlay;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.whgkswo.tesm.conversation.quest.objective.QuestObjective;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.component.FadeSequence;
import net.whgkswo.tesm.gui.component.TransitionStatus;
import net.whgkswo.tesm.gui.component.bounds.Boundary;
import net.whgkswo.tesm.gui.component.bounds.RectangularBound;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.elements.Box;
import net.whgkswo.tesm.gui.component.elements.TextPopUp;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class QuestOverlay implements HudRenderCallback {
    private static TextPopUp eventType;
    private static TextPopUp eventName;
    private static Set<TextPopUp> objectiveSet = new HashSet<>();

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        if(eventType != null){
            eventType.renderSelf(drawContext);
            if(eventType.getStatus() == TransitionStatus.TERMINATED){
                eventType = null;
            }
        }
        if(eventName != null){
            eventName.renderSelf(drawContext);
            if(eventName.getStatus() == TransitionStatus.TERMINATED){
                eventName = null;
            }
        }
        for(TextPopUp objective : objectiveSet){
            objective.renderSelf(drawContext);
        }
    }
    public static void displayStartPopUp(String type, String questName, Map<String, QuestObjective> objectives){
        eventType = new TextPopUp(
                Text.literal(type),
                Box.builder()
                        .bound(new RelativeBound(0.05, 0.45, 0.1, 0.05))
                        .childrenHorizontalAlignment(HorizontalAlignment.LEFT)
                        .build(),
                //new FilledBox(null, new RectangularBound(Boundary.BoundType.FIXED, 0.05, 0.45, 0.1, 0.05)),
                1.2f, HorizontalAlignment.LEFT,
                0, new FadeSequence(16, 248, 19));

        eventName = new TextPopUp(
                Text.literal(questName),
                Box.builder()
                        .bound(new RelativeBound(0.05, 0.5, 0.1, 0.05))
                        .build(),
                //new FilledBox(null, new RectangularBound(Boundary.BoundType.FIXED, 0.05, 0.5, 0.1, 0.05)),
                1.0f,
                HorizontalAlignment.LEFT,
                0,
                new FadeSequence(50, 186, 40)
        );

        objectiveSet.clear();
        int i = 0;
        for(QuestObjective objective : objectives.values()){
            objectiveSet.add(new TextPopUp(
                    Text.literal(i == 0 ? objective.getDescription() : "또는 " + objective.getDescription()),
                    Box.builder().build(),
                    //new FilledBox(null, new RectangularBound(Boundary.BoundType.FIXED, 0.05, 0.55 + i * 0.04, 0.1, 0.05), TesmColor.RODEO_DUST),
                    0.7f,
                    HorizontalAlignment.LEFT,
                    0, new FadeSequence(20,252, 20)
                    ));
            i++;
        }
    }
    public static void displayAdvancePopUp(String object, Map<String, QuestObjective> nextObjectives){
        int i = 0;
        eventType = new TextPopUp(
                Text.literal(object).styled(style -> style.withStrikethrough(true)),
                Box.builder().build(),
                //new FilledBox(null, new RectangularBound(Boundary.BoundType.FIXED, 0.05, 0.45, 0.1, 0.05), new TesmColor(150,150,150)),
                0.7f, HorizontalAlignment.LEFT,
                0, new FadeSequence(20, 160, 20)
                );
        objectiveSet.clear();
        for(QuestObjective objective : nextObjectives.values()){
            objectiveSet.add(new TextPopUp(
                    Text.literal(i == 0 ? objective.getDescription() : "또는 " + objective.getDescription()),
                    Box.builder().build(),
                    //new FilledBox(null, new RectangularBound(Boundary.BoundType.FIXED, 0.05, 0.45 + i * 0.04, 0.1, 0.05), TesmColor.RODEO_DUST),
                    0.7f, HorizontalAlignment.LEFT,
                    0, new FadeSequence(200, 20, 252,20)
                    ));
            i++;
        }
    }
    public static void displayCompletePopUp(String type, String questName){
        eventType = new TextPopUp(
                Text.literal(type),
                Box.builder().build(),
                //new FilledBox(null, new RectangularBound(Boundary.BoundType.FIXED, 0.05, 0.45, 0.1, 0.05)),
                1.2f, HorizontalAlignment.LEFT,
                0, new FadeSequence(16, 235, 35));
        eventName = new TextPopUp(
                Text.literal(questName),
                Box.builder().build(),
                //new FilledBox(null, new RectangularBound(Boundary.BoundType.FIXED, 0.05, 0.5, 0.1, 0.05)),
                1f, HorizontalAlignment.LEFT,
                0, new FadeSequence(50, 205, 40)
                );
    }
}
