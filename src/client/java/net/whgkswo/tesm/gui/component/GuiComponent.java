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

import java.util.Optional;

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
    private ParentComponent parent = null;
    @Builder.Default
    @Setter
    private RelativeBound screenRelativeBound = null;

    public GuiComponent(@Nullable ParentComponent parent){
        this.parent = parent;
    }

    public abstract void renderSelf(DrawContext context);

    public abstract RelativeBound getBound();

    public void render(DrawContext context){
        renderSelf(context);
    };

    public boolean shouldHide(){
        return shouldHide;
    }

    public @Nullable ParentComponent getParent(){
        return parent;
    }

    protected T self(){
        return (T) this;
    }

    public T setParent(ParentComponent parent){
        if(!parent.getChildren().contains(this)){
            this.parent = parent;
            parent.addChild(this);
        }

        initializeAlignment();
        return self();
    }

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

    public GuiComponent<?> getSibling(int index){
        if(parent == null) return null;
        return parent.getChildren().get(index);
    }

    public Optional<GuiComponent<?>> getOptionalSibling(int index){
        if(parent == null) return Optional.empty();
        return Optional.ofNullable(parent.getChildren().get(index));
    }

    public RelativeBound getScreenRelativeBoundWithUpdate(){
        screenRelativeBound = getScreenRelativeBound();
        return screenRelativeBound;
    }

    public RelativeBound getScreenRelativeBound(){
        // 캐싱된 값
        if(screenRelativeBound != null) return screenRelativeBound;

        RelativeBound childBound = getBound();
        if(parent == null) return childBound;

        RelativeBound parentBound = parent.getScreenRelativeBound();

        HorizontalAlignment horizontalAlignment = getRefHorizontalAlignment();
        VerticalAlignment verticalAlignment = getRefVerticalAlignment();

        double xRatio = parentBound.getXMarginRatio();
        double parentWidthRatio = parentBound.getWidthRatio();;
        double yRatio = parentBound.getYMarginRatio();
        double parentHeightRatio = parentBound.getHeightRatio();

        GuiDirection parentAxis = parent.getAxis();
        int childIndex = getChildIndex();
        double siblingsWidthRatio = childBound.getWidthRatio();
        double siblingsHeightRatio = childBound.getHeightRatio();
        for(int i = 0; i< parent.getChildren().size(); i++){
            if(i == childIndex) continue; // 자기 자신 재귀 호출 방지
            double[] siblingWH = getSibling(i).getScreenRelativeWH();

            if(parentAxis == GuiDirection.HORIZONTAL){
                siblingsWidthRatio += siblingWH[0];
                if(i < childIndex) xRatio += siblingWH[0]; // 수평축으로 형 요소들의 공간 더하기
            } else { // VERTICAL
                siblingsHeightRatio += siblingWH[1];
                if(i < childIndex) yRatio += siblingWH[1]; // 수직축으로 형 요소들의 공간 더하기
            }
        }

        switch (horizontalAlignment){
            case CENTER -> {
                double xGap = parentBound.getWidthRatio() - siblingsWidthRatio;
                xRatio += xGap / 2;
            }
            case RIGHT -> {
                xRatio += parentWidthRatio - childBound.getWidthRatio();
            }
        }

        switch (verticalAlignment){
            case CENTER -> {
                double yGap = parentBound.getHeightRatio() - siblingsHeightRatio;
                yRatio += yGap / 2;
            }
            case LOWER -> {
                yRatio += parentHeightRatio - childBound.getHeightRatio();
            }
        }

        return new RelativeBound(
                parentWidthRatio * childBound.getWidthRatio(),
                parentHeightRatio * childBound.getHeightRatio(),
                xRatio,
                yRatio
                );
    }

    private double[] getScreenRelativeWH(){
        RelativeBound childBound = getBound();
        if(parent == null) return new double[]{childBound.getWidthRatio(), childBound.getHeightRatio()};

        RelativeBound parentBound = parent.getScreenRelativeBound();

        double parentWidthRatio = parentBound.getWidthRatio();
        double parentHeightRatio = parentBound.getHeightRatio();

        return new double[]{parentWidthRatio * childBound.getWidthRatio(), parentHeightRatio * childBound.getHeightRatio()};
    }

    private HorizontalAlignment getRefHorizontalAlignment(){
        // 부모의 Axis와 같은 방향의 축은 모든 자식요소가 동일한 정렬기준을 따라야 함
        GuiDirection parentAxis = parent.getAxis();
        if(parentAxis == GuiDirection.HORIZONTAL) return parent.getChildrenHorizontalAlignment();

        HorizontalAlignment horizontalAlignment = parent.getChildrenHorizontalAlignment();

        if(!selfHorizontalAlignment.equals(HorizontalAlignment.NONE)){
            horizontalAlignment = selfHorizontalAlignment;
        }

        return horizontalAlignment;
    }

    private VerticalAlignment getRefVerticalAlignment(){
        // 부모의 Axis와 같은 방향의 축은 모든 자식요소가 동일한 정렬기준을 따라야 함
        GuiDirection parentAxis = parent.getAxis();
        if(parentAxis == GuiDirection.VERTICAL) return parent.getChildrenVerticalAlignment();

        VerticalAlignment verticalAlignment = parent.getChildrenVerticalAlignment();
        if(!selfVerticalAlignment.equals(VerticalAlignment.NONE)){
            verticalAlignment = selfVerticalAlignment;
        }

        return verticalAlignment;
    }
}
