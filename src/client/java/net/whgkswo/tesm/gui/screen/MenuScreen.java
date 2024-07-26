package net.whgkswo.tesm.gui.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.conversation.quest.Quest;
import net.whgkswo.tesm.gui.Alignment;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.Colors;
import net.whgkswo.tesm.gui.screen.component.GuiComponent;
import net.whgkswo.tesm.gui.screen.templete.CustomScreen;
import org.lwjgl.glfw.GLFW;

public class MenuScreen extends CustomScreen {
    public MenuScreen() {
        super();
    }
    private final Identifier MENU_BACKGROUND = new Identifier(TESMMod.MODID, "textures/gui/menu_background.png");
    private final Identifier BASE_AAA685 = new Identifier(TESMMod.MODID, "textures/gui/base_aaa685.png");

    @Override
    public void init(){
        super.init();
        for(int i = 0; i< 3; i++){
            createComponent("testComponent" + i,
                    Colors.COLORED_TEXTURES.get("aaa685"),
                    "abc 가나다 123",
                    0.7f,
                    Alignment.LEFT,
                    0.1,
                    0.15 + 0.05 * i,
                    0.2,
                    0.04,
                    0.1);
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
    public boolean keyPressed(int keyCode, int scanCode, int modifiers){
        if(keyCode == GLFW.GLFW_KEY_TAB || keyCode == GLFW.GLFW_KEY_J){
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta){
        super.render(context,mouseX,mouseY,delta);
        /*RenderingHelper.renderText(Alignment.LEFT, context, 0.7f, "abc", 0.15,0.15,0xffffff);*/

        getGuiComponents().forEach((key, component) -> {
            RenderingHelper.renderComponent(context, component);
        });
        /*RenderingHelper.renderColoredComponent(context, BASE_AAA685, 0.1, 0.15, 0.2, 0.04,
                0.7f, "abc 가나다 123", Alignment.LEFT, 0.1);
        RenderingHelper.renderColoredComponent(context, BASE_AAA685, 0.1, 0.2, 0.2, 0.04,
                0.7f, "abc 가나다 123", Alignment.CENTER, 0);
        RenderingHelper.renderColoredComponent(context, BASE_AAA685, 0.1, 0.25, 0.2, 0.04,
                0.7f, "abc 가나다 123", Alignment.RIGHT,0.1);

        RenderingHelper.renderColoredComponent(context, BASE_AAA685, 0.5, 0.15, 0.2, 0.19,
                0.7f, "abc 가나다 123", Alignment.LEFT, 0.1);
        RenderingHelper.renderColoredComponent(context, BASE_AAA685, 0.5, 0.35, 0.2, 0.19,
                0.7f, "abc 가나다 123", Alignment.CENTER, 0);
        RenderingHelper.renderColoredComponent(context, BASE_AAA685, 0.5, 0.55, 0.2, 0.19,
                0.7f, "abc 가나다 123", Alignment.RIGHT,0.1);*/
    }
}
