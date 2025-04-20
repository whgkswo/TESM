package net.whgkswo.tesm.gui.component.bounds.providers;

import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.ParentComponent;
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

        return new RelativeBound(
                parentBound.getWidthRatio() * childBound.getWidthRatio(),
                parentBound.getHeightRatio() * childBound.getHeightRatio(),
                parentBound.getXOffsetRatio() + parentBound.getWidthRatio() * (childBound.getXOffsetRatio() + component.getRightMarginRatio()),
                parentBound.getYOffsetRatio() + parentBound.getHeightRatio() * (childBound.getYOffsetRatio() + component.getBottomMarginRatio())
        );
    }
}
