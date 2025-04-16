package net.whgkswo.tesm.gui.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.conversation.quest.Quest;
import net.whgkswo.tesm.conversation.quest.QuestStatus;
import net.whgkswo.tesm.general.GeneralUtil;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiDirection;
import net.whgkswo.tesm.gui.component.bounds.Boundary;
import net.whgkswo.tesm.gui.component.bounds.RectangularBound;
import net.whgkswo.tesm.gui.component.elements.EdgedBox;
import net.whgkswo.tesm.gui.component.elements.FilledBox;
import net.whgkswo.tesm.gui.component.elements.TextBox;
import net.whgkswo.tesm.gui.component.elements.TextLabel;
import net.whgkswo.tesm.gui.screen.templete.TesmScreen;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.stream.Collectors;

public class MenuScreen extends TesmScreen {
    public MenuScreen() {
        super(GuiDirection.HORIZONTAL);
    }
    private final Identifier MENU_BACKGROUND = Identifier.of(TESMMod.MODID, "textures/gui/menu_background.png");

    @Override
    public void init(){
        super.init();
        List<Quest> onGoingQuests = Quest.QUESTS.entrySet().stream()
                        .filter(entry -> entry.getValue().getStatus() == QuestStatus.ONGOING)
                        .map(entry -> entry.getValue())
                        .collect(Collectors.toList());

        GeneralUtil.repeatWithIndex(onGoingQuests.size(), i -> {
            TextLabel textLabel = new TextLabel(
                    Text.literal(onGoingQuests.get(i).getName()),
                    EdgedBox.builder()
                            .axis(GuiDirection.HORIZONTAL)
                            .thickness(1)
                            .edgeColor(TesmColor.TRANSPARENT)
                            .backgroundColor(TesmColor.NEUTRAL_GOLD)
                            .bound(new RectangularBound(Boundary.BoundType.FIXED,0.1, 0.25 + 0.05 * i, 0.2, 0.04))
                            .build(),
                    /*new FilledBox(
                            null,
                            new RectangularBound(Boundary.BoundType.FIXED,0.1, 0.25 + 0.05 * i, 0.2, 0.04),
                            TesmColor.NEUTRAL_GOLD
                    ),*/
                    0.7f,
                    HorizontalAlignment.LEFT,

                    0.05);
            rootComponent.addChild(textLabel);
        });
        if(!onGoingQuests.isEmpty()){
            TextBox textBox = new TextBox(
                    EdgedBox.builder()
                            .thickness(1)
                            .bound(new RectangularBound(Boundary.BoundType.FLEXIBLE, 0.35, 0.25, 0.5, 0.3))
                            .backgroundColor(TesmColor.WHITE)
                            .build(),
                    /*new EdgedBox(
                            new RectangularBound(Boundary.BoundType.FLEXIBLE, 0.35, 0.25, 0.5, 0.3),
                            GuiDirection.HORIZONTAL,
                            TesmColor.WHITE,
                            null,
                            1
                    ),*/
                    textRenderer,
                    Text.literal(onGoingQuests.get(0).getCurrentStage()),
                    0.7f,
                    0,0, HorizontalAlignment.LEFT
            );
            rootComponent.addChild(textBox);
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
