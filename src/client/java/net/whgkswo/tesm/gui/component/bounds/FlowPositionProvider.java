package net.whgkswo.tesm.gui.component.bounds;

import lombok.AllArgsConstructor;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.component.GuiAxis;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.ParentComponent;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;

import java.util.List;

@AllArgsConstructor
public class FlowPositionProvider implements PositionProvider {
    private final GuiComponent<?, ?> component;
    private final ParentComponent<?, ?> parent;

    @Override
    public RelativeBound getAbsoluteBound() {
        // 캐싱된 값
        if (component.getCachedAbsoluteBound() != null) return component.getCachedAbsoluteBound();

        RelativeBound childBound = component.getBound();
        if (parent == null) return childBound;

        RelativeBound parentBound = parent.getAbsoluteBoundWithUpdate();
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

        return new RelativeBound(
                component.getAbsoluteWHRatio(parentWidthRatio, childBound.getWidthRatio()),
                component.getAbsoluteWHRatio(parentHeightRatio, childBound.getHeightRatio()),
                xRatio,
                yRatio
        );
    }

    /**
     * 모든 형제 요소들을 하나의 덩어리로 하여 크기 계산
     * @return double[4] {siblingsWidthRatio, siblingsHeightRatio, xOffset, yOffset}
     */
    private double[] calculateSiblingBound(RelativeBound childBound, GuiAxis parentAxis) {
        int childIndex = component.getChildIndex();
        double siblingsWidthRatio = childBound.getWidthRatio();
        double siblingsHeightRatio = childBound.getHeightRatio();
        double xOffset = 0;
        double yOffset = 0;

        for (int i = 0; i < parent.getChildren().size(); i++) {
            if (i == childIndex) continue; // 자기 자신은 초기값에 반영돼 있음

            double[] siblingWH = component.getSibling(i).getAbsoluteWHRatio();

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

    private HorizontalAlignment getRefHorizontalAlignment(){
        // 부모의 Axis와 같은 방향의 축은 모든 자식요소가 부모의 정렬기준을 따라야 함
        // 단 형제가 없을 경우 자유
        GuiAxis parentAxis = parent.getAxis();
        List<GuiComponent<?, ?>> siblings = parent.getChildren();
        if(siblings.size() > 1 && parentAxis == GuiAxis.HORIZONTAL) return parent.getChildrenHorizontalAlignment();

        HorizontalAlignment horizontalAlignment = parent.getChildrenHorizontalAlignment();

        if(!component.getSelfHorizontalAlignment().equals(HorizontalAlignment.NONE)){
            horizontalAlignment = component.getSelfHorizontalAlignment();
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
        if(!component.getSelfVerticalAlignment().equals(VerticalAlignment.NONE)){
            verticalAlignment = component.getSelfVerticalAlignment();
        }

        return verticalAlignment;
    }
}