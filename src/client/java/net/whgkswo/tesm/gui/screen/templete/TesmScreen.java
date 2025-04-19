package net.whgkswo.tesm.gui.screen.templete;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.GuiAxis;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.elements.Box;
import net.whgkswo.tesm.networking.payload.data.SimpleReq;
import net.whgkswo.tesm.networking.payload.id.SimpleTask;

public abstract class TesmScreen extends Screen {
    private boolean shouldFreezeTicks = true;
    private boolean shouldRenderBackground = true;
    private boolean initialized;
    public final Box rootComponent;

    public TesmScreen(){
        this(false);
    }

    public TesmScreen(boolean shouldFreezeTicks){
        super(Text.literal(""));
        this.shouldFreezeTicks = shouldFreezeTicks;
        rootComponent = Box.builder()
                .bound(RelativeBound.FULL_SCREEN)
                .screenRelativeBound(RelativeBound.FULL_SCREEN)
                .backgroundColor(TesmColor.TRANSPARENT)
                .edgeColor(TesmColor.TRANSPARENT)
                .childrenHorizontalAlignment(HorizontalAlignment.CENTER)
                .axis(GuiAxis.VERTICAL)
                .id("root")
                .build()
                .register(null)
        ;
    }
    @Override
    public boolean shouldPause() {
        return false;
    }

    public abstract void initExtended();

    @Override
    public void init(){
        if(!initialized){
            // 틱 프리즈 (서버에 패킷 전송)
            if(shouldFreezeTicks()) ClientPlayNetworking.send(new SimpleReq(SimpleTask.TICK_FREEZE));
            registerMouseWheelEvent();

            initExtended();
            initialized = true;
        }{
            // 구성 요소 리사이즈
            clearCachedBounds(rootComponent);
        }
    }

    private void clearCachedBounds(GuiComponent<?, ?> component){
        component.setScreenRelativeBound(null);
        component.initializeBound();
        for (GuiComponent<?, ?> child : component.getChildren()){
            clearCachedBounds(child);
        }
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

        rootComponent.render(context);
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

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        if(shouldRenderBackground) super.renderBackground(context, mouseX, mouseY, delta);
    }

    public void shouldRenderBackground(boolean flag){
        shouldRenderBackground = flag;
    }
}
