package net.whgkswo.tesm.gui.component;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SuperBuilder
@NoArgsConstructor
@Getter
public abstract class GuiComponent<T extends GuiComponent<T>> {
    private String id;
    private String className;
    private boolean shouldHide;
    @Builder.Default
    @Setter
    private HorizontalAlignment selfHorizontalAlignment = HorizontalAlignment.NONE;
    @Builder.Default
    @Setter
    private VerticalAlignment selfVerticalAlignment = VerticalAlignment.NONE;
    @Nullable
    @Builder.Default
    private ParentComponent<?> parent = null;
    @Builder.Default
    @Setter
    private RelativeBound screenRelativeBound = null;

    public GuiComponent(@Nullable ParentComponent<?> parent){
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

    public @Nullable ParentComponent<?> getParent(){
        return parent;
    }

    protected T self(){
        return (T) this;
    }

    public T setParent(ParentComponent<?> parent){
        if(!parent.getChildren().contains(this)){
            this.parent = parent;
            parent.addChild(this);
        }
        initializeBound();
        return self();
    }

    public void initializeBound(){
        initializeAlignment();
        initializeSize();
    }

    protected void initializeSize(){
        // 하위 클래스에서 선택적으로 오버라이딩해서 사용
    };

    private void initializeAlignment(){
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

    public GuiComponent<?> getSibling(int index) {
        if (parent == null) return null;
        List<GuiComponent<?>> siblings = parent.getChildren();
        return siblings.get(index);
    }

    public Optional<GuiComponent<?>> getOptionalSibling(int index){
        if(parent == null) return Optional.empty();
        List<GuiComponent<?>> siblings = parent.getChildren();
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
        GuiDirection parentAxis = parent.getAxis();

        // 기본 위치 계산
        double xRatio = parentBound.getXMarginRatio();
        double yRatio = parentBound.getYMarginRatio();
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
    private double[] calculateSiblingBound(RelativeBound childBound, GuiDirection parentAxis) {
        int childIndex = getChildIndex();
        double siblingsWidthRatio = childBound.getWidthRatio();
        double siblingsHeightRatio = childBound.getHeightRatio();
        double xOffset = 0;
        double yOffset = 0;

        for (int i = 0; i < parent.getChildren().size(); i++) {
            if (i == childIndex) continue; // 자기 자신은 초기값에 반영돼 있음

            double[] siblingWH = getSibling(i).getScreenRelativeWH();

            if (parentAxis == GuiDirection.HORIZONTAL) {
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
        GuiDirection parentAxis = parent.getAxis();
        List<GuiComponent<?>> siblings = parent.getChildren();
        if(siblings.size() > 1 && parentAxis == GuiDirection.HORIZONTAL) return parent.getChildrenHorizontalAlignment();

        HorizontalAlignment horizontalAlignment = parent.getChildrenHorizontalAlignment();

        if(!selfHorizontalAlignment.equals(HorizontalAlignment.NONE)){
            horizontalAlignment = selfHorizontalAlignment;
        }

        return horizontalAlignment;
    }

    private VerticalAlignment getRefVerticalAlignment(){
        // 부모의 Axis와 같은 방향의 축은 모든 자식요소가 부모의 정렬기준을 따라야 함
        // 단 형제가 없을 경우 자유
        GuiDirection parentAxis = parent.getAxis();
        List<GuiComponent<?>> siblings = parent.getChildren();
        if(siblings.size() > 1 && parentAxis == GuiDirection.VERTICAL) return parent.getChildrenVerticalAlignment();

        VerticalAlignment verticalAlignment = parent.getChildrenVerticalAlignment();
        if(!selfVerticalAlignment.equals(VerticalAlignment.NONE)){
            verticalAlignment = selfVerticalAlignment;
        }

        return verticalAlignment;
    }

    // 오버라이딩용
    public List<GuiComponent<?>> getChildren(){
        return new ArrayList<>();
    }
}
