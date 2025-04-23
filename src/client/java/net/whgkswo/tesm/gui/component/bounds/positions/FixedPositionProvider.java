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

    @Override
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
    }
}
