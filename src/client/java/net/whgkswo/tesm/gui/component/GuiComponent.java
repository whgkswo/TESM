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
import net.whgkswo.tesm.gui.component.bounds.AbsoluteBound;
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
    private RelativeBound screenRelativeBound = null;
    private StylePreset<S> stylePreset;

    public GuiComponent(@Nullable ParentComponent<?, ?> parent){
        this.parent = parent;
    }

    protected abstract void renderSelf(DrawContext context);

    protected abstract RelativeBound getBound();

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

    public void handleHover(int mouseX, int mouseY){
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
        AbsoluteBound absoluteBound = getAbsoluteBound();
        return absoluteBound.x1() <= mouseX && absoluteBound.x2() >= mouseX
                && absoluteBound.y1() <= mouseY && absoluteBound.y2() >= mouseY;
    }

    protected AbsoluteBound getAbsoluteBound(){
        RelativeBound bound = this.getScreenRelativeBound();

        Window window = MinecraftClient.getInstance().getWindow();
        int scaledWidth = window.getScaledWidth();
        int scaledHeight = window.getScaledHeight();

        return new AbsoluteBound(
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

    public Optional<GuiComponent<?, ?>> getOptionalSibling(int index){
        if(parent == null) return Optional.empty();
        List<GuiComponent<?, ?>> siblings = parent.getChildren();
        return Optional.ofNullable(siblings.get(index));
    }

    public RelativeBound getScreenRelativeBoundWithUpdate(){
        screenRelativeBound = getScreenRelativeBound();
        return screenRelativeBound;
    }

    protected RelativeBound getParentBound(){
        return parent.getScreenRelativeBoundWithUpdate();
    }

    public RelativeBound getScreenRelativeBound() {
        // 캐싱된 값
        if (screenRelativeBound != null) return screenRelativeBound;

        RelativeBound childBound = getBound();
        if (parent == null) return childBound;

        RelativeBound parentBound = getParentBound();
        GuiAxis parentAxis = parent.getAxis();

        // 기본 위치 계산
        double xRatio = parentBound.getXOffsetRatio();
        double yRatio = parentBound.getYOffsetRatio();
        double parentWidthRatio = parentBound.getWidthRatio();
        double parentHeightRatio = parentBound.getHeightRatio();

        // 형제 요소들의 오프셋, 사이즈 계산
        double[] siblingBound = calculateSiblingBound(childBound, parentAxis);
        double siblingsWidthRatio = siblingBound[0];
        double siblingsHeightRatio = siblingBound[1];
        xRatio += siblingBound[2];
        yRatio += siblingBound[3];

        // 수평, 수직 정렬 적용
        xRatio = applyHorizontalAlignment(getRefHorizontalAlignment(), childBound,
                parentWidthRatio, siblingsWidthRatio, xRatio);

        yRatio = applyVerticalAlignment(getRefVerticalAlignment(), childBound,
                parentHeightRatio, siblingsHeightRatio, yRatio);

        screenRelativeBound = new RelativeBound(
                getScreenRelativeWH(parentWidthRatio, childBound.getWidthRatio()),
                getScreenRelativeWH(parentHeightRatio, childBound.getHeightRatio()),
                xRatio,
                yRatio
        );

        return screenRelativeBound;
    }

    /**
     * 모든 형제 요소들을 하나의 덩어리로 하여 크기 계산
     * @return double[4] {siblingsWidthRatio, siblingsHeightRatio, xOffset, yOffset}
     */
    private double[] calculateSiblingBound(RelativeBound childBound, GuiAxis parentAxis) {
        int childIndex = getChildIndex();
        double siblingsWidthRatio = childBound.getWidthRatio();
        double siblingsHeightRatio = childBound.getHeightRatio();
        double xOffset = 0;
        double yOffset = 0;

        for (int i = 0; i < parent.getChildren().size(); i++) {
            if (i == childIndex) continue; // 자기 자신은 초기값에 반영돼 있음

            double[] siblingWH = getSibling(i).getScreenRelativeWH();

            if (parentAxis == GuiAxis.HORIZONTAL) {
                siblingsWidthRatio += siblingWH[0];
                if (i < childIndex) xOffset += siblingWH[0]; // 수평축으로 형 요소들의 공간 더하기
            } else { // VERTICAL
                siblingsHeightRatio += siblingWH[1];
                if (i < childIndex) yOffset += siblingWH[1]; // 수직축으로 형 요소들의 공간 더하기
            }
        }

        return new double[] {siblingsWidthRatio, siblingsHeightRatio, xOffset, yOffset};
    }

    private double applyHorizontalAlignment(HorizontalAlignment alignment,
                                            RelativeBound childBound,
                                            double parentWidthRatio,
                                            double siblingsWidthRatio,
                                            double xRatio) {
        switch (alignment) {
            case LEFT:
                // 왼쪽 정렬은 기본값이므로 아무것도 하지 않음
                break;
            case CENTER: {
                double xGap = parentWidthRatio - siblingsWidthRatio;
                xRatio += xGap / 2;
                break;
            }
            case RIGHT: {
                xRatio += parentWidthRatio - childBound.getWidthRatio();
                break;
            }
        }

        return xRatio;
    }

    private double applyVerticalAlignment(VerticalAlignment alignment,
                                          RelativeBound childBound,
                                          double parentHeightRatio,
                                          double siblingsHeightRatio,
                                          double yRatio) {
        switch (alignment) {
            case UPPER:
                // 위쪽 정렬은 기본값이므로 아무것도 하지 않음
                break;
            case CENTER: {
                double yGap = parentHeightRatio - siblingsHeightRatio;
                yRatio += yGap / 2;
                break;
            }
            case LOWER: {
                yRatio += parentHeightRatio - childBound.getHeightRatio();
                break;
            }
        }

        return yRatio;
    }

    protected double getScreenRelativeWH(double parentWH, double childWH){
        return parentWH * childWH;
    }

    private double[] getScreenRelativeWH(){
        RelativeBound childBound = getBound();
        if(parent == null) return new double[]{childBound.getWidthRatio(), childBound.getHeightRatio()};

        RelativeBound parentBound = parent.getScreenRelativeBound();

        double parentWidthRatio = parentBound.getWidthRatio();
        double parentHeightRatio = parentBound.getHeightRatio();

        return new double[]{getScreenRelativeWH(parentWidthRatio, childBound.getWidthRatio()), getScreenRelativeWH(parentHeightRatio, childBound.getHeightRatio())};
    }

    private HorizontalAlignment getRefHorizontalAlignment(){
        // 부모의 Axis와 같은 방향의 축은 모든 자식요소가 부모의 정렬기준을 따라야 함
        // 단 형제가 없을 경우 자유
        GuiAxis parentAxis = parent.getAxis();
        List<GuiComponent<?, ?>> siblings = parent.getChildren();
        if(siblings.size() > 1 && parentAxis == GuiAxis.HORIZONTAL) return parent.getChildrenHorizontalAlignment();

        HorizontalAlignment horizontalAlignment = parent.getChildrenHorizontalAlignment();

        if(!selfHorizontalAlignment.equals(HorizontalAlignment.NONE)){
            horizontalAlignment = selfHorizontalAlignment;
        }

        return horizontalAlignment;
    }

    private VerticalAlignment getRefVerticalAlignment(){
        // 부모의 Axis와 같은 방향의 축은 모든 자식요소가 부모의 정렬기준을 따라야 함
        // 단 형제가 없을 경우 자유
        GuiAxis parentAxis = parent.getAxis();
        List<GuiComponent<?, ?>> siblings = parent.getChildren();
        if(siblings.size() > 1 && parentAxis == GuiAxis.VERTICAL) return parent.getChildrenVerticalAlignment();

        VerticalAlignment verticalAlignment = parent.getChildrenVerticalAlignment();
        if(!selfVerticalAlignment.equals(VerticalAlignment.NONE)){
            verticalAlignment = selfVerticalAlignment;
        }

        return verticalAlignment;
    }

    // 오버라이딩용
    public List<GuiComponent<?, ?>> getChildren(){
        return new ArrayList<>();
    }
}
