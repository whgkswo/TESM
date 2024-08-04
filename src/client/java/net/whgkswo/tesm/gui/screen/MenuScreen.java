package net.whgkswo.tesm.gui.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.conversation.quest.Quest;
import net.whgkswo.tesm.conversation.quest.QuestStatus;
import net.whgkswo.tesm.file.TextReader;
import net.whgkswo.tesm.general.GeneralUtil;
import net.whgkswo.tesm.gui.Alignment;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.CustomColor;
import net.whgkswo.tesm.gui.component.bounds.Boundary;
import net.whgkswo.tesm.gui.component.bounds.RectangularBound;
import net.whgkswo.tesm.gui.component.elements.BlankedBox;
import net.whgkswo.tesm.gui.component.elements.TextBox;
import net.whgkswo.tesm.gui.component.elements.TextLabel;
import net.whgkswo.tesm.gui.screen.templete.CustomScreen;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.stream.Collectors;

public class MenuScreen extends CustomScreen {
    public MenuScreen() {
        super();
    }
    private final Identifier MENU_BACKGROUND = new Identifier(TESMMod.MODID, "textures/gui/menu_background.png");

    @Override
    public void init(){
        super.init();
        List<Quest> onGoingQuests = Quest.QUESTS.entrySet().stream()
                        .filter(entry -> entry.getValue().getStatus() == QuestStatus.ONGOING)
                        .map(entry -> entry.getValue())
                        .collect(Collectors.toList());

        GeneralUtil.repeatWithIndex(onGoingQuests.size(), i -> {
            createComponent("testComponent" + i,
                    new TextLabel(
                            new CustomColor(200,160,130),
                            onGoingQuests.get(i).getName(),
                            0xffffff,
                            0.7f,
                            Alignment.LEFT,
                            new RectangularBound(Boundary.BoundType.FIXED,0.1, 0.25 + 0.05 * i, 0.2, 0.04),
                            0.05));
        });
        if(!onGoingQuests.isEmpty()){
            createComponent("questDescription",
                    new TextBox(CustomColor.ColorsPreset.WHITE.getColor(),
                            new RectangularBound(Boundary.BoundType.FLEXIBLE, 0.35, 0.25, 0.5, 0.3),
                            textRenderer,
                            onGoingQuests.get(0).getCurrentStage(),
                            0.7f,
                            0,0,Alignment.LEFT
                    ));
        }
    }
    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderingHelper.renderTexture(context, MENU_BACKGROUND, 0.1,0.1,0.8,0.8);
    }
    @Override
    public boolean shouldPause() {
        return false;
    }
    @Override
    public boolean shouldFreezeTicks(){
        return true;
    }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers){
        if(keyCode == GLFW.GLFW_KEY_TAB || keyCode == GLFW.GLFW_KEY_J){
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
