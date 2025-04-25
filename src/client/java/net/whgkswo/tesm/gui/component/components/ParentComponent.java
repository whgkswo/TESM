package net.whgkswo.tesm.gui.component.components;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.component.GuiAxis;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.components.style.GuiStyle;
import net.whgkswo.tesm.gui.exceptions.GuiException;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
// 스타일 요소가 아닌 필드는 여기에 초기값 명시
public abstract class ParentComponent<T extends GuiComponent<T, S>, S extends GuiStyle> extends GuiComponent<T, S>{
    @Setter
    private GuiAxis axis;
    @Setter
    private HorizontalAlignment childrenHorizontalAlignment;
    private VerticalAlignment childrenVerticalAlignment;
    private Double horizontalGap;
    private Double verticalGap;
    private final List<GuiComponent<?, ?>> children = new ArrayList<>();

    public ParentComponent(ParentComponent<?, ?> parent, RelativeBound bound, GuiAxis axis, HorizontalAlignment childrenHorizontalAlignment) {
        super(parent);
        this.axis = axis;
        this.childrenHorizontalAlignment = childrenHorizontalAlignment;
    }

    @Override
    public void render(DrawContext context){
        // 자신 렌더링
        renderSelfWithScissor(context);
        // 자식 렌더링
        for (GuiComponent<?, ?> child : children){
            child.tryRender(context);
        }
    }

    public void addChild(GuiComponent<?, ?> child){
        if(!children.contains(child)) children.add(child);
        if(child.getParent() == null || child.getParent() != this) child.setParentAndMotherScreen(this);
    }

    @Override
    public List<GuiComponent<?, ?>> getChildren(){
        // 자식 요소 임의 추가, 제거 불가(메서드 사용 강제)
        return Collections.unmodifiableList(children);
    }

    public double getSumOfChildrenRelativeHeight(){
        double result = 0;
        int childrenCount = 0;
        for(GuiComponent<?, ?> child : children){
            if(child.doOccupySpace()){
                result += child.getBound().getHeightRatio() + child.getTopMarginRatio() + child.getBottomMarginRatio();
                childrenCount++;
            }
        }
        if(children.size() > 1) result += this.verticalGap * (childrenCount - 1);
        return result;
    }

    public void removeChild(Integer index){
        if(index < 0 || index >= children.size()){
            new GuiException(getMotherScreen(), String.format("자식 요소 제거 중 오류: %s 컴포넌트에 %d번째 자식이 없습니다.", getId(), index)).handle();
            return;
        }
        GuiComponent<?, ?> child = children.get(index);
        getMotherScreen().getComponentIdSet().remove(child.getId());
        children.remove(child);
    }

    public void removeChildren(){
        for (GuiComponent<?,?> child : children){
            getMotherScreen().getComponentIdSet().remove(child.getId());
            if(child.hasChildren()) ((ParentComponent<?, ?>) child).removeChildren();
        }
        children.clear();
    }

    // Lombok setter가 동작이 안됨;; 상속땜에 그런가
    public void setChildrenHorizontalAlignment(HorizontalAlignment childrenHorizontalAlignment){
        this.childrenHorizontalAlignment = childrenHorizontalAlignment;
    }

    public void setChildrenVerticalAlignment(VerticalAlignment childrenVerticalAlignment){
        this.childrenVerticalAlignment = childrenVerticalAlignment;
    }

    public void setHorizontalGap(double horizontalGap){
        this.horizontalGap = horizontalGap;
    }

    public void setVerticalGap(double verticalGap){
        this.verticalGap = verticalGap;
    }
}
