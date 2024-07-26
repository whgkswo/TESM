package net.whgkswo.tesm.gui.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.general.GeneralUtil;
import net.whgkswo.tesm.gui.Alignment;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.Colors;
import net.whgkswo.tesm.gui.component.Box;
import net.whgkswo.tesm.gui.component.LineDirection;
import net.whgkswo.tesm.gui.component.implementation.BlankedBox;
import net.whgkswo.tesm.gui.component.implementation.StraightLine;
import net.whgkswo.tesm.gui.component.implementation.TextBoxWIthBackground;
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
                            Colors.COLORED_TEXTURES.get("aaa685"),
                            "abc 가나다 123",
                            0xffffff,
                            0.7f,
                            Alignment.LEFT,
                            0.1,
                            0.15 + 0.05 * i,
                            0.2,
                            0.04,
                            0.1));
        });
        createComponent("testLine",
                new StraightLine(
                        LineDirection.HORIZONTAL,
                        Colors.COLORED_TEXTURES.get("aaa685"),
                        new Box(0.5, 0.5, 0.5, 0.5),
                        1
                        ));
        createComponent("testBox",
                new BlankedBox(
                        Colors.COLORED_TEXTURES.get("aaa685"),
                        new Box(0.25,0.25,0.5,0.5),1
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
    public boolean keyPressed(int keyCode, int scanCode, int modifiers){
        if(keyCode == GLFW.GLFW_KEY_TAB || keyCode == GLFW.GLFW_KEY_J){
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
