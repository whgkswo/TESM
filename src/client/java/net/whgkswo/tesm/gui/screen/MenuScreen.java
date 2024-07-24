package net.whgkswo.tesm.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.networking.ModMessages;
import org.lwjgl.glfw.GLFW;

public class MenuScreen extends Screen {
    public MenuScreen() {
        super(Text.literal("메뉴 화면"));
    }
    private final Identifier MENU_BACKGROUND = new Identifier(TESMMod.MODID, "textures/gui/menu_background.png");

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        /*RenderingHelper.renderTexture(context, MENU_BACKGROUND, 0.1,0.1,0.8,0.8);*/
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
    public void init(){
        // 틱 프리즈 (서버에 패킷 전송)
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(true); // freezeOn = true
        ClientPlayNetworking.send(ModMessages.TICK_FREEZE_TOGGLE_ID, buf);
    }
    @Override
    public void close(){
        // 틱 언프리즈 (서버에 패킷 전송)
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(false); // freezeOn = false
        ClientPlayNetworking.send(ModMessages.TICK_FREEZE_TOGGLE_ID, buf);
        super.close();
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta){
        super.render(context,mouseX,mouseY,delta);
    }
}
