package net.whgkswo.tesm.gui.component;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.component.bounds.AbsolutePosition;
import net.whgkswo.tesm.gui.component.bounds.FlowPositionProvider;
import net.whgkswo.tesm.gui.component.bounds.PositionProvider;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.elements.Box;
import net.whgkswo.tesm.gui.component.elements.features.Hoverable;
import net.whgkswo.tesm.gui.component.elements.style.DefaultStyleProvider;
import net.whgkswo.tesm.gui.component.elements.style.GuiStyle;
import net.whgkswo.tesm.gui.component.elements.style.StylePreset;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;
import net.whgkswo.tesm.message.MessageHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;

@SuperBuilder
@NoArgsConstructor
@Getter
public abstract class GuiComponent<T extends GuiComponent<T, S>, S extends GuiStyle> {
    private String id;
    private String className;
    private boolean shouldHide;
    @Setter
    private HorizontalAlignment selfHorizontalAlignment;
    @Setter
    private VerticalAlignment selfVerticalAlignment;
    @Nullable
    private ParentComponent<?, ?> parent;
    @Builder.Default
    @Setter
    private RelativeBound cachedAbsoluteBound = null;
    private StylePreset<S> stylePreset;
    private PositionProvider positionProvider;

    public GuiComponent(@Nullable ParentComponent<?, ?> parent){
        this.parent = parent;
    }

    protected abstract void renderSelf(DrawContext context);

    public abstract RelativeBound getBound();

    public void render(DrawContext context){
        renderSelf(context);
    };

    protected boolean shouldHide(){
        return shouldHide;
    }

    public @Nullable ParentComponent<?, ?> getParent(){
        return parent;
    }

    protected T self(){
        return (T) this;
    }

    public T register(ParentComponent<?, ?> parent){
        if(parent != null && !parent.getChildren().contains(this)){
            this.parent = parent;
            parent.addChild(this);
        }
        // 기본 포지션 정책은 플로우
        if(positionProvider == null) positionProvider = new FlowPositionProvider(this, parent);

        // 스타일 초기화
        initializeStyle();
        return self();
    }

    public Set<GuiComponent<?, ?>> getHoveredComponents(int mouseX, int mouseY, Set<GuiComponent<?,?>> result, Box rootComponent){
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
        if(this instanceof Hoverable hoverable){
            hoverable.handleHover();
        }
    }

    public void handleHoverExit(){
        if(this instanceof Hoverable hoverable){
            hoverable.handleHoverExit();
        }
    }

    protected boolean isMouseOver(int mouseX, int mouseY){
        AbsolutePosition absoluteBound = getAbsolutePosition();
        return absoluteBound.x1() <= mouseX && absoluteBound.x2() >= mouseX
                && absoluteBound.y1() <= mouseY && absoluteBound.y2() >= mouseY;
    }

    protected AbsolutePosition getAbsolutePosition(){
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

    private void initializeStyle(){
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

    public RelativeBound getCachedAbsoluteBound(){
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

    // 오버라이딩용
    public List<GuiComponent<?, ?>> getChildren(){
        return new ArrayList<>();
    }
}
