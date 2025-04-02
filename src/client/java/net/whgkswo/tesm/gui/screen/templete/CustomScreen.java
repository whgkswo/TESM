package net.whgkswo.tesm.gui.screen.templete;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.networking.payload.data.SimpleReq;
import net.whgkswo.tesm.networking.payload.id.SimpleTask;

import java.util.HashMap;
import java.util.Map;

public class CustomScreen extends Screen {
    private boolean shouldFreezeTicks = true;
    private final Map<String, GuiComponent> guiComponents = new HashMap<>();

    public CustomScreen(){
        super(Text.literal(""));
    }

    public CustomScreen(boolean shouldFreezeTicks){
        super(Text.literal(""));
        this.shouldFreezeTicks = shouldFreezeTicks;
    }
    @Override
    public boolean shouldPause() {
        return false;
    }
    @Override
    public void init(){
        // 틱 프리즈 (서버에 패킷 전송)
        if(shouldFreezeTicks()) ClientPlayNetworking.send(new SimpleReq(SimpleTask.TICK_FREEZE));
        registerMouseWheelEvent();
    }
    @Override
    public void close(){
        // 틱 언프리즈 (서버에 패킷 전송)
        if(shouldFreezeTicks()) ClientPlayNetworking.send(new SimpleReq(SimpleTask.TICK_UNFREEZE));
        // 다른 Gui들을 위해 셰이더 색상 초기화
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        super.close();
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta){
        super.render(context, mouseX, mouseY, delta);

        getGuiComponents().forEach((key, component) -> {
            component.render(context);
        });
    }
    private void registerMouseWheelEvent(){
        ScreenMouseEvents.afterMouseScroll(this).register((screen1, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
            if(verticalAmount > 0){
                onScrollUp();
            } else if (verticalAmount < 0) {
                onScrollDown();
            }
        });
    }
    public void onScrollUp(){}
    public void onScrollDown(){}
    public boolean shouldFreezeTicks(){
        return shouldFreezeTicks;
    }
    public Map<String, GuiComponent> getGuiComponents() {
        return guiComponents;
    }
    public void createComponent(String name, GuiComponent component){
        guiComponents.put(name, component);
    }
}
