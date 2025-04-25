package net.whgkswo.tesm.gui.component.bounds.positions;

import net.whgkswo.tesm.gui.component.components.GuiComponent;
import net.whgkswo.tesm.gui.component.components.ParentComponent;
import net.whgkswo.tesm.gui.component.bounds.PositionType;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;

public class FixedPositionProvider extends PositionProvider {

    public FixedPositionProvider(GuiComponent<?, ?> component, ParentComponent<?, ?> parent) {
        super(component, parent);
    }

    @Override
    public PositionType getType(){
        return PositionType.FIXED;
    }

    /*@Override
    public RelativeBound getAbsoluteBound() {
        // 캐싱된 값
        if (component.getCachedAbsoluteBound() != null) return component.getCachedAbsoluteBound();

        RelativeBound childBound = component.getBound();
        if (parent == null) return childBound;

        RelativeBound parentBound = parent.getAbsoluteBoundWithUpdate();

        // 수직 정렬
        double yRatio = childBound.getYOffsetRatio();
        double siblingHeight = childBound.getHeightRatio() + component.getTopMarginRatio() + component.getBottomMarginRatio();
        yRatio = applyVerticalAlignment(getRefVerticalAlignment(), childBound, siblingHeight, yRatio);

        // 수평 정렬 적용
        double xRatio = childBound.getXOffsetRatio();
        double siblingWidth = childBound.getWidthRatio() + component.getLeftMarginRatio() + component.getRightMarginRatio();
        xRatio = applyHorizontalAlignment(getRefHorizontalAlignment(), childBound, siblingWidth, xRatio);

        return new RelativeBound(
                parentBound.getWidthRatio() * childBound.getWidthRatio(),
                parentBound.getHeightRatio() * childBound.getHeightRatio(),
                parentBound.getXOffsetRatio() + parentBound.getWidthRatio() * (xRatio + component.getRightMarginRatio()),
                parentBound.getYOffsetRatio() + parentBound.getHeightRatio() * (yRatio + component.getBottomMarginRatio())
        );
    }*/

    @Override
    public RelativeBound getAbsoluteBound() {
        // 캐싱된 값
        if (component.getCachedAbsoluteBound() != null) return component.getCachedAbsoluteBound();

        RelativeBound childBound = component.getBound();
        if (parent == null) return childBound;

        RelativeBound parentBound = parent.getAbsoluteBoundWithUpdate();

        // 컴포넌트 마진
        double topMargin = component.getTopMarginRatio();
        double leftMargin = component.getLeftMarginRatio();
        double rightMargin = component.getRightMarginRatio();
        double bottomMargin = component.getBottomMarginRatio();

        // 마진을 포함한 전체 크기 계산
        double totalWidthRatio = childBound.getWidthRatio() + leftMargin + rightMargin;
        double totalHeightRatio = childBound.getHeightRatio() + topMargin + bottomMargin;

        // 수직 정렬
        double yRatio = childBound.getYOffsetRatio();
        yRatio = applyVerticalAlignment(getRefVerticalAlignment(), childBound, totalHeightRatio, yRatio);

        // 수평 정렬
        double xRatio = childBound.getXOffsetRatio();
        xRatio = applyHorizontalAlignment(getRefHorizontalAlignment(), childBound, totalWidthRatio, xRatio);

        // 마진 적용
        double finalXOffset = parentBound.getXOffsetRatio() + parentBound.getWidthRatio() * (xRatio + leftMargin);
        double finalYOffset = parentBound.getYOffsetRatio() + parentBound.getHeightRatio() * (yRatio + topMargin);

        return new RelativeBound(
                parentBound.getWidthRatio() * childBound.getWidthRatio(),
                parentBound.getHeightRatio() * childBound.getHeightRatio(),
                finalXOffset,
                finalYOffset
        );
    }
}
