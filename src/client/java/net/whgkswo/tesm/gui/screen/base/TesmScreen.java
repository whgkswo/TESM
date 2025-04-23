package net.whgkswo.tesm.gui.screen.base;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.bounds.PositionType;
import net.whgkswo.tesm.gui.component.components.GuiComponent;
import net.whgkswo.tesm.gui.component.GuiAxis;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.components.BoxPanel;
import net.whgkswo.tesm.gui.component.components.features.GuiFeatureType;
import net.whgkswo.tesm.gui.component.components.features.base.ClickHandler;
import net.whgkswo.tesm.gui.component.components.features.base.ScrollHandler;
import net.whgkswo.tesm.gui.component.components.features.base.Scrollable;
import net.whgkswo.tesm.gui.component.components.style.BoxStyle;
import net.whgkswo.tesm.gui.exceptions.GuiException;
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
    private GuiComponent<?, ?> hoverTarget;
    private GuiComponent<?, ?> clickTarget;
    private Scrollable scrollTarget;
    private int prevMouseX = -1;
    private int prevMouseY = -1;
    private double windowScaleBackup;
    private static final double WINDOW_SCALE_FACTOR = 1;
    @Getter
    private final Set<String> componentIdSet = new HashSet<>();
    private final BoxPanel modalContainer;
    @Getter
    @Setter
    protected final BoxPanel rootModal;
    public static final String ROOT_ID = "root";
    public static final String ROOT_MODAL_ID = "root_modal";

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
        // 모달 부모 관련 npe 방지용 더미 컴포넌트
        modalContainer = BoxPanel.builder(null)
                .bound(RelativeBound.FULL_SCREEN)
                .backgroundColor(TesmColor.TRANSPARENT)
                .edgeColor(TesmColor.TRANSPARENT)
                .childrenHorizontalAlignment(HorizontalAlignment.CENTER)
                .axis(GuiAxis.VERTICAL)
                .id(ROOT_ID)
                .build()
        ;
        rootModal = BoxPanel.builder(modalContainer)
                .id(ROOT_MODAL_ID)
                .stylePreset(BoxStyle.ROOT_MODAL)
                .positionType(PositionType.FIXED)
                .visibility(false)
                .build();
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
            initExtended();
            initialized = true;
        }{
            // 구성 요소 리사이즈
            rootComponent.clearCaches();
            rootModal.clearCaches();
        }
        // 이벤트는 매번 다시 등록해줘야됨
        registerMouseWheelEvent();
    }

    public void clearAllCaches(){
        rootComponent.clearCaches();
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
        // 모달 렌더링
        rootModal.tryRender(context);
        // 인터렉션 타겟 컴포넌트 탐색
        clickTarget = findClickTarget(mouseX, mouseY);
        scrollTarget = findScrollTarget(mouseX, mouseY);
        // 마우스 호버링 처리
        handleHoverEvents(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button){
        if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT){
            if(clickTarget == null) return false;
            ClickHandler clickHandler = clickTarget.getClickHandler();
            if(clickHandler == null) return false;
            clickHandler.run();
        }
        return super.mouseClicked(mouseX,mouseY,button);
    }
    
    private GuiComponent<?,?> findClickTarget(int mouseX, int mouseY){
        return findInteractionTarget(GuiFeatureType.CLICK, mouseX, mouseY);
    }

    private Scrollable findScrollTarget(int mouseX, int mouseY){
        return (Scrollable) findInteractionTarget(GuiFeatureType.SCROLL, mouseX, mouseY);
    }

    private void handleHoverEvents(int mouseX, int mouseY){
        if(prevMouseX == mouseX && prevMouseY == mouseY) return;

        prevMouseX = mouseX;
        prevMouseY = mouseY;

        // 타겟 찾기
        GuiComponent<?,?> newTarget = findInteractionTarget(GuiFeatureType.HOVER, mouseX, mouseY);
        // 이벤트 실행
        executeHoverEvent(hoverTarget, newTarget);
        // 타겟 업데이트
        hoverTarget = newTarget;
    }

    private GuiComponent<?, ?> findInteractionTarget(GuiFeatureType type, int mouseX, int mouseY){
        // 마우스 올려진 컴포넌트들 중 입력된 타입 처리 가능한 것들 찾기
        Set<GuiComponent<?, ?>> hoveredComponents;
        if(rootModal.isVisible()){ // 모달 on
            hoveredComponents = rootModal.getTargetedComponents(type, mouseX, mouseY, new HashSet<>(), rootModal);
        }else{ // 모달 off
            hoveredComponents = rootComponent.getTargetedComponents(type, mouseX, mouseY, new HashSet<>(), rootComponent);
        }

        if(hoveredComponents.isEmpty()) return null;
        return getTopTarget(hoveredComponents); // 최우선순위 반환
    }

    private void executeHoverEvent(GuiComponent<?, ?> previousTarget, GuiComponent<?, ?> currentTarget){
        // 타겟이 변경되었을 때만 처리
        if (previousTarget != currentTarget) {
            // 이전 타겟 호버 종료
            if (previousTarget != null) previousTarget.handleHoverExit();
            // 현재 타겟 호버 시작
            if (currentTarget != null) currentTarget.handleHover();
        }
    }

    private GuiComponent<?, ?> getTopTarget(Set<GuiComponent<?, ?>> targetComponents){
        return targetComponents.stream()
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
    public void onScrollUp(){
        if(scrollTarget == null) return;
        if(scrollTarget instanceof Scrollable){
            ScrollHandler handler = scrollTarget.getScrollHandler();
            if(handler != null) handler.onScrollUp();
        }
    }
    public void onScrollDown(){
        if(scrollTarget == null) return;
        if(scrollTarget instanceof Scrollable){
            ScrollHandler handler = scrollTarget.getScrollHandler();
            if(handler != null) handler.onScrollDown();
        }
    }
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
