package net.whgkswo.tesm.gui.screen.templete;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.whgkswo.tesm.gui.component.GuiComponent;
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
        if(shouldFreezeTicks()){
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBoolean(true); // freezeOn = true
            ClientPlayNetworking.send(ModMessages.TICK_FREEZE_TOGGLE_ID, buf);
        }
    }
    @Override
    public void close(){
        // 틱 언프리즈 (서버에 패킷 전송)
        if(shouldFreezeTicks()){
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBoolean(false); // freezeOn = false
            ClientPlayNetworking.send(ModMessages.TICK_FREEZE_TOGGLE_ID, buf);
        }
        super.close();
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta){
        super.render(context, mouseX, mouseY, delta);

        getGuiComponents().forEach((key, component) -> {
            component.render(context);
        });
    }
    public boolean shouldFreezeTicks(){
        return false;
    }
    public Map<String, GuiComponent> getGuiComponents() {
        return guiComponents;
    }
    public void createComponent(String name, GuiComponent component){
        guiComponents.put(name, component);
    }
}
