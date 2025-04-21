package net.whgkswo.tesm.gui.component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;
import net.whgkswo.tesm.gui.GuiDirection;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.component.bounds.*;
import net.whgkswo.tesm.gui.component.bounds.providers.PositionProvider;
import net.whgkswo.tesm.gui.component.components.BoxPanel;
import net.whgkswo.tesm.gui.component.components.features.BackgroundHoverHandler;
import net.whgkswo.tesm.gui.component.components.features.HoverType;
import net.whgkswo.tesm.gui.component.components.features.base.HoverHandler;
import net.whgkswo.tesm.gui.component.components.style.DefaultStyleProvider;
import net.whgkswo.tesm.gui.component.components.style.GuiStyle;
import net.whgkswo.tesm.gui.component.components.style.StylePreset;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;
import net.whgkswo.tesm.message.MessageHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;

@NoArgsConstructor
@Getter
// 스타일 요소가 아닌 필드는 여기에 초기값 명시
public abstract class GuiComponent<T extends GuiComponent<T, S>, S extends GuiStyle> {
    @Setter
    private String id;
    @Setter
    private boolean shouldHide;
    @Setter
    private HorizontalAlignment selfHorizontalAlignment;
    @Setter
    private VerticalAlignment selfVerticalAlignment;
    @Nullable
    private ParentComponent<?, ?> parent;
    @Setter
    private RelativeBound cachedAbsoluteBound;
    @Setter
    private double rightMarginRatio;
    @Setter
    private double bottomMarginRatio;
    @Setter
    private double leftMarginRatio;
    @Setter
    private double topMarginRatio;
    @Setter
    private StylePreset<S> stylePreset;
    @Setter
    private PositionProvider positionProvider;
    private HoverHandler hoverHandler;

    public GuiComponent(@Nullable ParentComponent<?, ?> parent){
        this.parent = parent;
    }

    protected abstract void renderSelf(DrawContext context);

    public abstract RelativeBound getBound();

    public void render(DrawContext context){
        // 자신 렌더링
        renderSelfWithScissor(context);
    };

    protected void renderSelfWithScissor(DrawContext context){
        boolean scissorEnabled = parent != null;

        if(scissorEnabled){
            AbsolutePosition absolutePosition = parent.getAbsolutePosition();
            // 부모의 영역을 넘어가면 자르도록 설정
            context.enableScissor(
                    absolutePosition.x1(),
                    absolutePosition.y1(),
                    absolutePosition.x2(),
                    absolutePosition.y2()
            );
        }
        // 렌더링 실행
        renderSelf(context);
        // 원상태로 복원
        if(scissorEnabled) context.disableScissor();
    }

    protected boolean shouldHide(){
        return shouldHide;
    }

    public @Nullable ParentComponent<?, ?> getParent(){
        return parent;
    }

    protected T self(){
        return (T) this;
    }

    public void setParent(ParentComponent<?, ?> parent){
        if(parent != null && !parent.getChildren().contains(this)){
            this.parent = parent;
            parent.addChild(this);
        }
    }

    public Set<GuiComponent<?, ?>> getHoveredComponents(int mouseX, int mouseY, Set<GuiComponent<?,?>> result, BoxPanel rootComponent){
        if(isMouseOver(mouseX, mouseY)){
            result.add(this);
            for (GuiComponent<?, ?> child : getChildren()){
                result = child.getHoveredComponents(mouseX, mouseY, result, rootComponent);
            }
        }else{ // 마우스가 밖에 있음
            if(this == rootComponent){
                return new HashSet<>();
            }
        }
        return result;
    }

    public void handleHover(){
        if(hoverHandler != null) hoverHandler.handleHover();
    }

    public void handleHoverExit(){
        if(hoverHandler != null) hoverHandler.handleHoverExit();
    }

    protected boolean isMouseOver(int mouseX, int mouseY){
        AbsolutePosition absoluteBound = getAbsolutePosition();
        return absoluteBound.x1() <= mouseX && absoluteBound.x2() >= mouseX
                && absoluteBound.y1() <= mouseY && absoluteBound.y2() >= mouseY;
    }

    public AbsolutePosition getAbsolutePosition(){
        RelativeBound bound = this.getAbsoluteBound();

        Window window = MinecraftClient.getInstance().getWindow();
        int scaledWidth = window.getScaledWidth();
        int scaledHeight = window.getScaledHeight();

        return new AbsolutePosition(
                (int) (bound.getXOffsetRatio() * scaledWidth),
                (int) ((bound.getXOffsetRatio() + bound.getWidthRatio()) * scaledWidth),
                (int) (bound.getYOffsetRatio() * scaledHeight),
                (int) ((bound.getYOffsetRatio() + bound.getHeightRatio()) * scaledHeight)
        );
    }

    protected abstract Class<S> getStyleType();

    public void initializeStyle(){
        // 스타일이 없으면 디폴트 스타일 가져오기
        if(stylePreset == null) {
            Class<S> styleType = getStyleType();
            StylePreset<S> defaultStyle = DefaultStyleProvider.getDefaultStyle(styleType);

            if(defaultStyle != null) stylePreset = defaultStyle;
        };

        // 스타일 프리셋 선적용
        applyStylePreset(stylePreset.style());
        // 바운드 초기화
        initializeBound();
        // 추가작업(선택사항)
        initializeStyleExtended();
    }

    protected void initializeStyleExtended(){
        // 오버라이딩용
    }

    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        // 현재 클래스에서 GuiComponent 클래스(포함)까지 모든 필드 탐색
        while (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

            // GuiComponent에 도달했거나 지나갔으면 종료
            if (clazz == GuiComponent.class || !GuiComponent.class.isAssignableFrom(clazz)) {
                break;
            }

            clazz = clazz.getSuperclass();
        }

        return fields;
    }

    protected void applyStylePreset(S style) {
        if(style == null) return;

        try {
            // 현재 객체의 모든 필드 가져오기 (상속된 필드 포함)
            List<Field> thisFields = getAllFields(this.getClass());

            for (Field thisField : thisFields) {
                try {
                    // 스타일 객체에서 같은 이름의 필드 찾기
                    Field styleField = style.getClass().getDeclaredField(thisField.getName());

                    // 필드에 접근 가능하게 설정
                    thisField.setAccessible(true);
                    styleField.setAccessible(true);

                    // 객체에서 값 가져오기
                    Object thisValue = thisField.get(this);
                    Object styleValue = styleField.get(style);

                    // 본래의 필드가 비어 있고, 스타일 값이 null이 아닌 경우에만 복사
                    if (thisValue == null && styleValue != null) {
                        // 필드 타입이 호환되는지 확인
                        if (thisField.getType().isAssignableFrom(styleField.getType())) {
                            thisField.set(this, styleValue);
                        }
                    }
                } catch (NoSuchFieldException e) {
                    // 해당 필드가 스타일 객체에 없으면 무시
                }
            }
        } catch (Exception e) {
            MessageHelper.sendMessage("스타일 적용 중 오류 발생: " + e.getMessage());
        }
    }

    public void initializeBound(){
        setAlignmentsSameAsParent();
        initializeSize();
    }

    protected void initializeSize(){
        // 하위 클래스에서 선택적으로 오버라이딩해서 사용
    };

    private void setAlignmentsSameAsParent(){
        if(parent == null) return;

        if(selfVerticalAlignment.equals(VerticalAlignment.NONE)){
            selfVerticalAlignment = parent.getChildrenVerticalAlignment();
        }
        if(selfHorizontalAlignment.equals(HorizontalAlignment.NONE)){
            selfHorizontalAlignment = parent.getChildrenHorizontalAlignment();
        }
    }

    public int getChildIndex(){
        if(parent == null) return -1;
        return parent.getChildren().indexOf(this);
    }

    public int getGenerationIndex(){
        return getGenerationIndex(1);
    }

    protected int getGenerationIndex(int result){
        if(parent == null){
            return result;
        }else {
            return parent.getGenerationIndex(result + 1);
        }
    }

    public GuiComponent<?, ?> getSibling(int index) {
        if (parent == null) return null;
        List<GuiComponent<?, ?>> siblings = parent.getChildren();
        return siblings.get(index);
    }

    public RelativeBound getAbsoluteBoundWithUpdate(){
        cachedAbsoluteBound = getAbsoluteBound();
        return cachedAbsoluteBound;
    }

    public RelativeBound getAbsoluteBound() {
        return positionProvider.getAbsoluteBound();
    }

    public double getAbsoluteWHRatio(double parentWH, double childWH){
        return parentWH * childWH;
    }

    public double[] getAbsoluteWHRatio(){
        RelativeBound childBound = getBound();
        if(parent == null) return new double[]{childBound.getWidthRatio(), childBound.getHeightRatio()};

        RelativeBound parentBound = parent.getAbsoluteBound();

        double parentWidthRatio = parentBound.getWidthRatio();
        double parentHeightRatio = parentBound.getHeightRatio();

        return new double[]{getAbsoluteWHRatio(parentWidthRatio, childBound.getWidthRatio()), getAbsoluteWHRatio(parentHeightRatio, childBound.getHeightRatio())};
    }

    public Map<GuiDirection, Double> getAbsoluteMarginRatio(){
        if(parent == null) {
            return Map.of(
                    GuiDirection.TOP, topMarginRatio,
                    GuiDirection.BOTTOM, bottomMarginRatio,
                    GuiDirection.LEFT, leftMarginRatio,
                    GuiDirection.RIGHT, rightMarginRatio
                    );
        };

        RelativeBound parentBound = parent.getAbsoluteBound();

        double parentWidthRatio = parentBound.getWidthRatio();
        double parentHeightRatio = parentBound.getHeightRatio();

        return Map.of(
                GuiDirection.TOP, parentHeightRatio * topMarginRatio,
                GuiDirection.BOTTOM,parentHeightRatio * bottomMarginRatio,
                GuiDirection.LEFT, parentWidthRatio * leftMarginRatio,
                GuiDirection.RIGHT, parentHeightRatio * rightMarginRatio
        );
    }

    // 오버라이딩용
    public List<GuiComponent<?, ?>> getChildren(){
        return new ArrayList<>();
    }

    public void setHoverHandler(HoverType hoverType){
        if(hoverType == null){
            hoverHandler = null;
            return;
        }

        switch (hoverType){
            case BACKGROUND -> this.hoverHandler = new BackgroundHoverHandler(this);
        }
    }
}
