package net.whgkswo.tesm.gui.component.components;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.GuiDirection;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.component.bounds.*;
import net.whgkswo.tesm.gui.component.bounds.positions.PositionProvider;
import net.whgkswo.tesm.gui.component.components.features.BackgroundHoverHandler;
import net.whgkswo.tesm.gui.component.components.features.GuiFeatureType;
import net.whgkswo.tesm.gui.component.components.features.HoverType;
import net.whgkswo.tesm.gui.component.components.features.base.ClickHandler;
import net.whgkswo.tesm.gui.component.components.features.base.HoverHandler;
import net.whgkswo.tesm.gui.component.components.features.base.Scrollable;
import net.whgkswo.tesm.gui.component.components.style.DefaultStyleProvider;
import net.whgkswo.tesm.gui.component.components.style.GuiStyle;
import net.whgkswo.tesm.gui.component.components.style.StylePreset;
import net.whgkswo.tesm.gui.exceptions.GuiException;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;
import net.whgkswo.tesm.gui.screen.base.TesmScreen;
import net.whgkswo.tesm.message.MessageHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;

@NoArgsConstructor
@Getter
// 스타일 요소가 아닌 필드는 여기에 초기값 명시
public abstract class GuiComponent<C extends GuiComponent<C, S>, S extends GuiStyle> {
    @Setter
    private String id;
    private Boolean isVisible;
    @Setter
    private HorizontalAlignment selfHorizontalAlignment;
    @Setter
    private VerticalAlignment selfVerticalAlignment;
    @Nullable
    private ParentComponent<?, ?> parent;
    @Setter
    private RelativeBound cachedAbsoluteBound;
    @Setter
    private AbsolutePosition cachedScissor;
    @Setter
    private Double rightMarginRatio;
    @Setter
    private Double bottomMarginRatio;
    @Setter
    private Double leftMarginRatio;
    @Setter
    private Double topMarginRatio;
    @Setter
    private StylePreset<S> stylePreset;
    @Setter
    private PositionProvider positionProvider;
    private HoverHandler hoverHandler;
    private ClickHandler clickHandler;
    private TesmScreen motherScreen;
    @Setter
    private Boolean isBuildFinished = false;

    public GuiComponent(@Nullable ParentComponent<?, ?> parent){
        this.parent = parent;
    }

    protected abstract void renderSelf(DrawContext context);

    public abstract RelativeBound getBound();

    public void tryRender(DrawContext context){
        if(isVisible) render(context);
    }

    public void render(DrawContext context){
        // 자신 렌더링
        renderSelfWithScissor(context);
    };

    protected void renderSelfWithScissor(DrawContext context){
        boolean scissorEnabled = parent != null;

        if(scissorEnabled){
            if(cachedScissor == null) cachedScissor = getScissor();
            // 부모의 영역을 넘어가면 자르도록 설정
            context.enableScissor(
                    cachedScissor.x1(),
                    cachedScissor.y1(),
                    cachedScissor.x2(),
                    cachedScissor.y2()
            );
        }
        // 렌더링 실행
        renderSelf(context);
        // 원상태로 복원
        if(scissorEnabled) context.disableScissor();
    }

    protected Stack<GuiComponent<?,?>> getAncestors(Stack<GuiComponent<?,?>> stack){
        if(parent == null) return stack;
        stack.add(parent);
        return parent.getAncestors(stack);
    }

    // 모든 선조들의 범위와 교집합 구하기
    private AbsolutePosition getScissor(){
        if(parent == null) return RelativeBound.FULL_SCREEN.toAbsolutePosition();
        Stack<GuiComponent<?,?>> ancestors = getAncestors(new Stack<>());

        AbsolutePosition scissor = ancestors.pop().getAbsolutePosition();
        for (GuiComponent<?, ?> ancestor : ancestors){
            AbsolutePosition ancestorPosition = ancestor.getAbsolutePosition();
            scissor = new AbsolutePosition(
                    Math.max(scissor.x1(), ancestorPosition.x1()),
                    Math.max(scissor.y1(), ancestorPosition.y1()),
                    Math.min(scissor.x2(), ancestorPosition.x2()),
                    Math.min(scissor.y2(), ancestorPosition.y2())
            );
        }
        return scissor;
    }

    public boolean isVisible(){
        return isVisible;
    }

    public @Nullable ParentComponent<?, ?> getParent(){
        return parent;
    }

    protected C self(){
        return (C) this;
    }

    public void setParentAndMotherScreen(ParentComponent<?, ?> parent){
        this.parent = parent;
        if(parent != null && !parent.getChildren().contains(this)){
            parent.addChild(this);
        }
        if(parent != null && parent.getMotherScreen() != null) setMotherScreen(parent.getMotherScreen());
    }

    public Set<GuiComponent<?, ?>> getTargetedComponents(GuiFeatureType type, int mouseX, int mouseY, Set<GuiComponent<?,?>> result, BoxPanel refComponent){
        if(isMouseOver(mouseX, mouseY)){
            // 상호작용 가능한 경우에만 대상으로 추가
            switch (type){
                case CLICK -> {
                    if (isClickable()) result.add(this);
                }
                case HOVER -> {
                    if (isHoverable()) result.add(this);
                }
                case SCROLL -> {
                    if (isScrollable()) result.add(this);
                }
            }
            for (GuiComponent<?, ?> child : getChildren()){
                result = child.getTargetedComponents(type, mouseX, mouseY, result, refComponent);
            }
        }else{ // 마우스가 밖에 있음
            if(this == refComponent){
                return new HashSet<>();
            }
        }
        return result;
    }

    private boolean isClickable(){
        return this.clickHandler != null;
    }

    private boolean isHoverable(){
        return this.hoverHandler != null;
    }

    private boolean isScrollable(){
        return this instanceof Scrollable && ((Scrollable)this).getScrollHandler() != null;
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
        return bound.toAbsolutePosition();
    }

    protected abstract Class<S> getStyleType();

    public void initializeStyle(){
        // 스타일이 없으면 디폴트 스타일 가져오기
        if(stylePreset == null) {
            Class<S> styleType = getStyleType();
            StylePreset<S> defaultStyle = DefaultStyleProvider.getDefaultStyle(styleType, motherScreen);

            if(defaultStyle != null) stylePreset = defaultStyle;
        };

        // 스타일 프리셋 적용
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
        if(parent == null) new GuiException(motherScreen, "부모가 없는 컴포넌트의 자식번호를 가져올 수 없습니다: " + id).handle();
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
            case BACKGROUND_BLUR_EFFECTER -> this.hoverHandler = new BackgroundHoverHandler(this);
        }
    }

    public void setHoverHandler(HoverHandler hoverHandler){
        this.hoverHandler = hoverHandler;
    }

    public GuiComponent<?, ?> getDescendant(String id){
        if(this.id.equals(id)) return this;

        if (!this.getChildren().isEmpty()) {
            for (GuiComponent<?, ?> child : getChildren()) {
                GuiComponent<?, ?> descendant = child.getDescendant(id);
                if (descendant != null) return descendant;
            }
        }
        return null;
    }

    public void setMotherScreen(TesmScreen motherScreen){
        // motherScreen이 이미 있는데 덮어씌우려고 시도하거나, 루트 컴포넌트가 아닌데 수동으로 넣을 수 없음(id 널체크는 단지 equals npe 때문에 넣은 거)
        if(this.motherScreen != null || (id == null || !id.equals(TesmScreen.ROOT_ID)) && parent == null){
            new GuiException(this.motherScreen, "Mother Screen은 임의로 설정할 수 없습니다. 컴포넌트 ID: " + id).handle();
            return;
        }
        this.motherScreen = motherScreen;
    }
    public void clearCaches(){
        this.setCachedAbsoluteBound(null);
        this.setCachedScissor(null);
        this.initializeBound();
        for (GuiComponent<?, ?> child : this.getChildren()){
            child.clearCaches();
        }
    }
    public void changeVisibility(boolean isVisible){
        this.isVisible = isVisible;
        motherScreen.clearAllCaches();
    }

    public void onHover(HoverHandler hoverHandler){
        this.hoverHandler = hoverHandler;
    }

    public void onClick (ClickHandler clickHandler){
        this.clickHandler = clickHandler;
    }

    public void onClick (Runnable onClick){
        this.clickHandler = ClickHandler.of(onClick);
    }

    public boolean hasChildren(){
        if(!(this instanceof ParentComponent<C,S>)) return false;
        List<GuiComponent<?, ?>> children = this.getChildren();
        return children != null && !children.isEmpty();
    }

    public boolean doOccupySpace(){
        // 숨겨진 요소는 스킵
        if (!this.isVisible()) return false;
        // FIXED 타입은 스킵
        PositionType positionType = this.getPositionProvider().getType();
        if(positionType.equals(PositionType.FIXED)) return false;
        return true;
    }

    public void setVisibility(boolean isVisible){
        this.isVisible = isVisible;
    }
}
