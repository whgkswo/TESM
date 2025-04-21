package net.whgkswo.tesm.gui.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.screen.base.TesmScreen;
import org.lwjgl.glfw.GLFW;

public class MenuScreen extends TesmScreen {
    public MenuScreen() {
        super();
    }
    private final Identifier MENU_BACKGROUND = Identifier.of(TESMMod.MODID, "textures/gui/menu_background.png");

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderingHelper.renderTexture(context, MENU_BACKGROUND, 0.1,0.1,0.8,0.8);
    }
    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void initExtended() {
        /*List<Quest> onGoingQuests = Quest.QUESTS.entrySet().stream()
                .filter(entry -> entry.getValue().getStatus() == QuestStatus.ONGOING)
                .map(entry -> entry.getValue())
                .collect(Collectors.toList());

        GeneralUtil.repeatWithIndex(onGoingQuests.size(), i -> {
            TextLabel textLabel = new TextLabel(
                    Text.literal(onGoingQuests.get(i).getName()),
                    Box.builder()
                            .axis(GuiDirection.HORIZONTAL)
                            .edgeColor(TesmColor.TRANSPARENT)
                            .backgroundColor(TesmColor.NEUTRAL_GOLD)
                            .bound(new RelativeBound(0.1, 0.25 + 0.05 * i, 0.2, 0.04))
                            .build(),
                    *//*new FilledBox(
                            null,
                            new RectangularBound(Boundary.BoundType.FIXED,0.1, 0.25 + 0.05 * i, 0.2, 0.04),
                            TesmColor.NEUTRAL_GOLD
                    ),*//*
                    0.7f,
                    HorizontalAlignment.LEFT,

                    0.05);
            rootComponent.addChild(textLabel);
        });
        if(!onGoingQuests.isEmpty()){
            TextBox textBox = new TextBox(
                    null,
                    Box.builder()
                            .bound(new RelativeBound(0.35, 0.25, 0.5, 0.3))
                            .backgroundColor(TesmColor.WHITE)
                            .build(),
                    *//*new EdgedBox(
                            new RectangularBound(Boundary.BoundType.FLEXIBLE, 0.35, 0.25, 0.5, 0.3),
                            GuiDirection.HORIZONTAL,
                            TesmColor.WHITE,
                            null,
                            1
                    ),*//*
                    textRenderer,
                    Text.literal(onGoingQuests.get(0).getCurrentStage()),
                    0.7f,
                    0,0, HorizontalAlignment.LEFT
            );
            rootComponent.addChild(textBox);
        }*/
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
