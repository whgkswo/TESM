package net.whgkswo.tesm.gui.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.file.TextReader;
import net.whgkswo.tesm.general.GeneralUtil;
import net.whgkswo.tesm.gui.Alignment;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.CustomColor;
import net.whgkswo.tesm.gui.component.LineDirection;
import net.whgkswo.tesm.gui.component.bounds.Boundary;
import net.whgkswo.tesm.gui.component.bounds.LinearBound;
import net.whgkswo.tesm.gui.component.bounds.RectangularBound;
import net.whgkswo.tesm.gui.component.elements.BlankedBox;
import net.whgkswo.tesm.gui.component.elements.StraightLine;
import net.whgkswo.tesm.gui.component.elements.TextBox;
import net.whgkswo.tesm.gui.component.elements.TextBoxWIthBackground;
import net.whgkswo.tesm.gui.screen.templete.CustomScreen;
import org.lwjgl.glfw.GLFW;

public class MenuScreen extends CustomScreen {
    public MenuScreen() {
        super();
    }
    private final Identifier MENU_BACKGROUND = new Identifier(TESMMod.MODID, "textures/gui/menu_background.png");

    @Override
    public void init(){
        super.init();
        GeneralUtil.repeatWithIndex(3, i -> {
            createComponent("testComponent" + i,
                    new TextBoxWIthBackground(
                            new CustomColor(200,160,130),
                            "abc 가나다 123",
                            0xffffff,
                            0.7f,
                            Alignment.LEFT,
                            new RectangularBound(Boundary.BoundType.FIXED,0.1, 0.15 + 0.05 * i, 0.2, 0.04),
                            0.05));
        });
        createComponent("testBox",
                new BlankedBox(
                        new CustomColor(200, 160, 130),
                        new RectangularBound(Boundary.BoundType.FIXED, 0.25,0.25,0.5,0.5),1
                ));
        createComponent("testTextbox",
                new TextBox(
                        new CustomColor(255,255,255),
                        new RectangularBound(Boundary.BoundType.FLEXIBLE, 0.25, 0.25, 0.5, 0.5),
                        textRenderer,
                        TextReader.read("/books/biography_of_the_wolf_queen.txt"),
                        0.67f, 0.04, 0.04, Alignment.LEFT
                ));
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
