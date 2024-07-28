package net.whgkswo.tesm.gui.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
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
        createComponent("testLine",
                new StraightLine(
                        new CustomColor(200, 160, 130, 100),
                        LineDirection.HORIZONTAL,
                        new LinearBound(Boundary.BoundType.FIXED, 0.5, 0.5, 0.5, 1)
                        ));
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
                        "마인크래프트는 창의성과 모험을 결합한 독특한 게임입니다. 블록으로 이루어진 광활한 세계에서 플레이어들은 자유롭게 건축하고, 탐험하며, 생존해 나갑니다. 낮에는 자원을 모으고 건물을 지어 자신만의 세계를 만들어가고, 밤에는 몬스터들로부터 살아남아야 합니다. 단순해 보이는 그래픽 스타일 속에 무한한 가능성이 숨어 있어, 어린이부터 어른까지 모든 연령대가 즐길 수 있는 게임으로 자리잡았습니다.",
                        0.6f
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
