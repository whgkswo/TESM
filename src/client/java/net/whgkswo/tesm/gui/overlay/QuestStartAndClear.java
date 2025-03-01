package net.whgkswo.tesm.gui.overlay;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.whgkswo.tesm.npcs.quests.Quests;

import java.awt.*;


public class QuestStartAndClear implements HudRenderCallback {
    MinecraftClient client = MinecraftClient.getInstance();
    int width;
    int height;
    public static int alpha;

    // TODO: 포팅
    //@Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        int color = new Color(255,255,255,alpha).hashCode();
        if(client!=null){
            width = client.getWindow().getScaledWidth();
            height = client.getWindow().getScaledHeight();
        }
        if(Quests.questNameOLState != 0){
            String startOrClear = "";
            if(Quests.questNameOLState == 1){
                startOrClear = "시작: ";
            }else if (Quests.questNameOLState == 2){
                startOrClear = "완료: ";
            }
            final float QUEST_NAME_MAG = 1.5f;
            drawContext.getMatrices().push();
            drawContext.getMatrices().scale(QUEST_NAME_MAG,QUEST_NAME_MAG,1);
            drawContext.drawCenteredTextWithShadow(client.textRenderer,startOrClear+Quests.updatingQuestName,(int)(width/2/QUEST_NAME_MAG),(int)(height*0.2/QUEST_NAME_MAG),color);
            drawContext.getMatrices().pop();
        }
        if(Quests.questObjectiveOLOn){
            drawContext.drawCenteredTextWithShadow(client.textRenderer,Quests.updatingObjective,width/2,(int)(height*0.24),0xaa9682);
        }
    }

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {

    }
}
