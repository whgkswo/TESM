package net.whgkswo.tesm.gui.screen.base;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.GuiAxis;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.components.BoxPanel;
import net.whgkswo.tesm.gui.component.components.features.base.ClickHandler;
import net.whgkswo.tesm.gui.exceptions.GuiException;
import net.whgkswo.tesm.message.MessageHelper;
import net.whgkswo.tesm.networking.payload.data.SimpleReq;
import net.whgkswo.tesm.networking.payload.id.SimpleTask;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public abstract class TesmScreen extends Screen {
    private boolean shouldFreezeTicks = true;
    private boolean shouldRenderBackground = true;
    private boolean initialized;
    public final BoxPanel rootComponent;
    private GuiComponent<?, ?> hoveredComponent;
    private int prevMouseX = -1;
    private int prevMouseY = -1;
    private double windowScaleBackup;
    private static final double WINDOW_SCALE_FACTOR = 1;
    @Getter
    private final Set<String> componentIdSet = new HashSet<>();
    public static final String ROOT_ID = "root";

    public TesmScreen(){
        this(false);
    }

    public TesmScreen(boolean shouldFreezeTicks){
        super(Text.literal(""));
        this.shouldFreezeTicks = shouldFreezeTicks;
        rootComponent = BoxPanel.builder(null)
                .bound(RelativeBound.FULL_SCREEN)
                .backgroundColor(TesmColor.TRANSPARENT)
                .edgeColor(TesmColor.TRANSPARENT)
                .childrenHorizontalAlignment(HorizontalAlignment.CENTER)
                .axis(GuiAxis.VERTICAL)
                .id(ROOT_ID)
                .build()
                ;
        rootComponent.setMotherScreen(this);
    }
    @Override
    public boolean shouldPause() {
        return false;
    }

    public abstract void initExtended();

    @Override
    public void init(){
        Window window = MinecraftClient.getInstance().getWindow();
        windowScaleBackup = window.getScaleFactor();
        window.setScaleFactor(WINDOW_SCALE_FACTOR);

        if(!initialized){
            // 틱 프리즈 (서버에 패킷 전송)
            if(shouldFreezeTicks()) ClientPlayNetworking.send(new SimpleReq(SimpleTask.TICK_FREEZE));
            registerMouseWheelEvent();

            initExtended();
            initialized = true;
        }{
            // 구성 요소 리사이즈
            rootComponent.clearCachedBounds();
        }
    }

    public void clearAllCachedBounds(){
        rootComponent.clearCachedBounds();
    }

    @Override
    public void close(){
        // 틱 언프리즈 (서버에 패킷 전송)
        if(shouldFreezeTicks()) ClientPlayNetworking.send(new SimpleReq(SimpleTask.TICK_UNFREEZE));
        // 다른 Gui들을 위해 셰이더 색상 초기화
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        super.close();
        Window window = MinecraftClient.getInstance().getWindow();
        window.setScaleFactor(windowScaleBackup);
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta){
        super.render(context, mouseX, mouseY, delta);

        // 컴포넌트 렌더링
        rootComponent.tryRender(context);
        // 마우스 호버링 처리
        handleHoverEvents(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button){
        if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT){
            ClickHandler clickHandler = hoveredComponent.getClickHandler();
            if(clickHandler == null) return false;
            clickHandler.run();
        }
        return super.mouseClicked(mouseX,mouseY,button);
    }

    private void handleHoverEvents(int mouseX, int mouseY){
        if(prevMouseX == mouseX && prevMouseY == mouseY) return;

        prevMouseX = mouseX;
        prevMouseY = mouseY;

        // 마우스 올려진 컴포넌트 찾기
        Set<GuiComponent<?, ?>> hoveredComponents = rootComponent.getHoveredComponents(mouseX, mouseY, new HashSet<>(), rootComponent);
        if(!hoveredComponents.isEmpty()){
            GuiComponent<?, ?> prevHoveredComponent = hoveredComponent;
            hoveredComponent = getHoveredComponent(hoveredComponents);
            // 호버 대상이 바뀌었다면
            if(prevHoveredComponent != hoveredComponent){
                // 호버 Exit 처리
                if(prevHoveredComponent != null) prevHoveredComponent.handleHoverExit();
                // 호버 이벤트 처리
                if(hoveredComponent != null) hoveredComponent.handleHover();
                //MessageHelper.sendMessage(String.format("마우스(%d, %d), 대상 컴포넌트: %s", mouseX, mouseY, hoveredComponent.getId()));
            }
        }
    }

    private GuiComponent<?, ?> getHoveredComponent(Set<GuiComponent<?, ?>> hoveredComponents){
        return hoveredComponents.stream()
                .max(Comparator.<GuiComponent<?, ?>, Integer>comparing(
                    // 1순위: 세대 번호(깊이)가 큰 순서로 정렬 (내림차순)
                    GuiComponent::getGenerationIndex)
                        // 2순위: 형제 인덱스가 큰 순서로 정렬 (내림차순)
                        .thenComparing(GuiComponent::getChildIndex))
                .orElse(null);
    }

    protected GuiComponent<?, ?> searchComponent(String id){
        GuiComponent<?, ?> component = rootComponent.getDescendant(id);
        if(component == null){
            new GuiException(this, "컴포넌트 검색에 실패했습니다: " + id).handle();
        }
        return component;
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
