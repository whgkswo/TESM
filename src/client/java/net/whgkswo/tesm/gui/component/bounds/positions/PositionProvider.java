package net.whgkswo.tesm.gui.component.bounds.positions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.component.GuiAxis;
import net.whgkswo.tesm.gui.component.components.GuiComponent;
import net.whgkswo.tesm.gui.component.components.ParentComponent;
import net.whgkswo.tesm.gui.component.bounds.PositionType;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;

import java.util.List;

@AllArgsConstructor
@Getter
public abstract class PositionProvider {
    protected final GuiComponent<?, ?> component;
    protected final ParentComponent<?, ?> parent;

    public abstract RelativeBound getAbsoluteBound();

    public abstract PositionType getType();

    protected double applyHorizontalAlignment(HorizontalAlignment alignment,
                                            RelativeBound childBound,
                                            double siblingsWidthRatio,
                                            double xRatio) {
        switch (alignment) {
            case LEFT:
                // 왼쪽 정렬은 기본값이므로 아무것도 하지 않음
                break;
            case CENTER: {
                double xGap = 1 - siblingsWidthRatio; // 1은 부모의 전체
                xRatio += xGap / 2;
                break;
            }
            case RIGHT: {
                xRatio += 1 - childBound.getWidthRatio(); // 1은 부모의 전체
                break;
            }
        }

        return xRatio;
    }

    protected double applyVerticalAlignment(VerticalAlignment alignment,
                                          RelativeBound childBound,
                                          double siblingsHeightRatio,
                                          double yRatio) {
        switch (alignment) {
            case TOP:
                // 위쪽 정렬은 기본값이므로 아무것도 하지 않음
                break;
            case CENTER: {
                double yGap = 1 - siblingsHeightRatio; // 1은 부모의 전체
                yRatio += yGap / 2;
                break;
            }
            case BOTTOM: {
                yRatio += 1 - childBound.getHeightRatio(); // 1은 부모의 전체
                break;
            }
        }

        return yRatio;
    }

    protected HorizontalAlignment getRefHorizontalAlignment(){
        // 자식의 정렬기준이 NONE일 경우 부모의 정렬기준을 따름
        if(component.getSelfHorizontalAlignment().equals(HorizontalAlignment.NONE)) return parent.getChildrenHorizontalAlignment();
        // 부모의 Axis와 같은 방향의 축은 모든 자식요소가 부모의 정렬기준을 따라야 함
        // 단 형제가 없거나 고정 타입일 경우 자유
        GuiAxis parentAxis = parent.getAxis();
        List<GuiComponent<?, ?>> siblings = parent.getChildren();
        if(siblings.size() > 1 && parentAxis == GuiAxis.HORIZONTAL && getType().equals(PositionType.FLOW)) return parent.getChildrenHorizontalAlignment();

        HorizontalAlignment horizontalAlignment = parent.getChildrenHorizontalAlignment();

        if(!component.getSelfHorizontalAlignment().equals(HorizontalAlignment.NONE)){
            horizontalAlignment = component.getSelfHorizontalAlignment();
        }

        return horizontalAlignment;
    }

    protected VerticalAlignment getRefVerticalAlignment(){
        // 자식의 정렬기준이 NONE일 경우 부모의 정렬기준을 따름
        if(component.getSelfVerticalAlignment().equals(VerticalAlignment.NONE)) return parent.getChildrenVerticalAlignment();
        // 부모의 Axis와 같은 방향의 축은 모든 자식요소가 부모의 정렬기준을 따라야 함
        GuiAxis parentAxis = parent.getAxis();
        List<GuiComponent<?, ?>> siblings = parent.getChildren();
        if(siblings.size() > 1 && parentAxis == GuiAxis.VERTICAL && getType().equals(PositionType.FLOW)) return parent.getChildrenVerticalAlignment();
        // 단 형제가 없거나 고정 타입일 경우 자유
        VerticalAlignment verticalAlignment = parent.getChildrenVerticalAlignment();
        if(!component.getSelfVerticalAlignment().equals(VerticalAlignment.NONE)){
            verticalAlignment = component.getSelfVerticalAlignment();
        }

        return verticalAlignment;
    }
}
