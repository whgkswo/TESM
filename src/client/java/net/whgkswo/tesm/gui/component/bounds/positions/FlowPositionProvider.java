package net.whgkswo.tesm.gui.component.bounds.positions;

import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.component.GuiAxis;
import net.whgkswo.tesm.gui.component.components.GuiComponent;
import net.whgkswo.tesm.gui.component.components.ParentComponent;
import net.whgkswo.tesm.gui.component.bounds.PositionType;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.components.features.base.Scrollable;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;

import java.util.List;

public class FlowPositionProvider extends PositionProvider {

    public FlowPositionProvider(GuiComponent<?, ?> component, ParentComponent<?, ?> parent) {
        super(component, parent);
    }

    @Override
    public PositionType getType(){
        return PositionType.FLOW;
    }

    @Override
    public RelativeBound getAbsoluteBound() {
        // 캐싱된 값
        if (component.getCachedAbsoluteBound() != null) return component.getCachedAbsoluteBound();

        RelativeBound childBound = component.getBound();
        if (parent == null) return childBound;

        RelativeBound parentBound = parent.getAbsoluteBoundWithUpdate();
        GuiAxis parentAxis = parent.getAxis();

        // 기본 위치 계산
        double xRatio = 0;
        double yRatio = 0;
        double parentWidthRatio = parentBound.getWidthRatio();
        double parentHeightRatio = parentBound.getHeightRatio();

        // 형제 요소들 덩어리의 오프셋, 사이즈 계산
        double[] siblingBound = calculateSiblingBound(childBound, parentAxis);
        double siblingsWidthRatio = siblingBound[0];
        double siblingsHeightRatio = siblingBound[1];
        // 손윗 형제들의 너비, 마진 합산
        xRatio += siblingBound[2];
        yRatio += siblingBound[3];
        // 자신의 이전 마진 합산
        xRatio += component.getLeftMarginRatio();
        yRatio += component.getTopMarginRatio();

        // 부모에 스크롤 핸들러가 있다면 오프셋 적용
        if(parent instanceof Scrollable && ((Scrollable)parent).getScrollHandler() != null) {
            // 양의 오프셋을 가졌다 -> 위에 공간이 생겼다 -> 스크롤 업 -> 고로 합산이 아닌 차감
            yRatio -= ((Scrollable) parent).getScrollHandler().getOffset();
        }else {
            // 수직 정렬은 스크롤이 없을 때만
            yRatio = applyVerticalAlignment(getRefVerticalAlignment(), childBound, siblingsHeightRatio, yRatio);
        }

        // 수평 정렬 적용
        xRatio = applyHorizontalAlignment(getRefHorizontalAlignment(), childBound, siblingsWidthRatio, xRatio);

        // 위까지는 부모에 대한 상대 크기, 여기서 전체 스크린에 대한 상대크기로 변환
        return new RelativeBound(
                component.getAbsoluteWHRatio(parentWidthRatio, childBound.getWidthRatio()),
                component.getAbsoluteWHRatio(parentHeightRatio, childBound.getHeightRatio()),
                parentBound.getXOffsetRatio() + parentWidthRatio * xRatio,
                parentBound.getYOffsetRatio() + parentHeightRatio * yRatio
        );
    }

    // 모든 형제 요소들을 하나의 덩어리로 하여 크기 계산
    // 손윗 형제들의 너비와 마진을 합산
    // 비율은 스크린이 아닌 부모 기준
    private double[] calculateSiblingBound(RelativeBound childBound, GuiAxis parentAxis) {
        int childIndex = component.getChildIndex();

        double siblingsWidthRatio = childBound.getWidthRatio() + component.getLeftMarginRatio() + component.getRightMarginRatio();
        double siblingsHeightRatio = childBound.getHeightRatio() + component.getTopMarginRatio() + component.getBottomMarginRatio();
        double xOffset = 0;
        double yOffset = 0;

        for (int i = 0; i < parent.getChildren().size(); i++) {
            if (i == childIndex) continue; // 자기 자신은 초기값에 반영돼 있음

            GuiComponent<?, ?> sibling = component.getSibling(i);
            // 계산에서 빠질 컴포넌트는 스킵
            if(!sibling.doOccupySpace()) continue;

            RelativeBound siblingBound = sibling.getBound();

            if (parentAxis == GuiAxis.HORIZONTAL) {
                siblingsWidthRatio += siblingsWidthRatio + sibling.getLeftMarginRatio() + sibling.getRightMarginRatio();
                // 부모의 갭 더하기 (반복문은 자기자신 제외니까 형제수 -1 됨)
                siblingsWidthRatio += parent.getHorizontalGap();
                if (i < childIndex) {
                    // 수평축으로 형 요소들의 공간 더하기
                    xOffset += siblingBound.getWidthRatio();
                    // 마진도 더하기
                    xOffset += sibling.getLeftMarginRatio() + sibling.getRightMarginRatio();
                    // 부모 갭 더하기(손윗 형제마다 하나씩)
                    xOffset += parent.getHorizontalGap();
                }
            } else { // VERTICAL
                siblingsHeightRatio += siblingBound.getHeightRatio() + sibling.getTopMarginRatio() + sibling.getBottomMarginRatio();
                // 부모의 갭 더하기 (반복문은 자기자신 제외니까 형제수 -1 됨)
                siblingsHeightRatio += parent.getVerticalGap();
                if (i < childIndex) {
                    // 수직축으로 형 요소들의 공간 더하기
                    yOffset += siblingBound.getHeightRatio();
                    // 마진도 더하기
                    yOffset += sibling.getTopMarginRatio() + sibling.getBottomMarginRatio();
                    // 부모 갭 더하기(손윗 형제마다 하나씩)
                    yOffset += parent.getVerticalGap();
                }
            }
        }

        return new double[] {siblingsWidthRatio, siblingsHeightRatio, xOffset, yOffset};
    }
}