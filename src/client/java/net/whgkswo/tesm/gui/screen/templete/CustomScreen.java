package net.whgkswo.tesm.gui.screen.templete;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.gui.Alignment;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.screen.component.GuiComponent;
import net.whgkswo.tesm.gui.screen.component.TextBoxWIthBackground;
import net.whgkswo.tesm.networking.ModMessages;

import java.util.HashMap;
import java.util.Map;

public class CustomScreen extends Screen {
    private Map<String, GuiComponent> guiComponents = new HashMap<>();
    public CustomScreen(){
        super(Text.literal("GUI 템플릿 (Freeze)"));
    }
    @Override
    public boolean shouldPause() {
        return false;
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
        super.render(context, mouseX, mouseY, delta);

        getGuiComponents().forEach((key, component) -> {
            component.render(context);
        });
    }
    public Map<String, GuiComponent> getGuiComponents() {
        return guiComponents;
    }
    public void createTextBoxWithBackground(String name, Identifier background, String content, int textColor,float textScale, Alignment contentAlignment, double xRatio, double yRatio,
                                            double widthRatio, double heightRatio, double xMarginRatio){
        TextBoxWIthBackground component = new TextBoxWIthBackground(name, background, content, textColor, textScale, contentAlignment, xRatio, yRatio, widthRatio, heightRatio, xMarginRatio);
        guiComponents.put(name, component);

    }
}
