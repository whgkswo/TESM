package net.whgkswo.tesm.gui.component;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.elements.style.GuiStyle;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;

import java.util.ArrayList;
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
            child.render(context);
        }
    }

    public void addChild(GuiComponent<?, ?> child){
        if(!children.contains(child)) children.add(child);
        if(child.getParent() == null || child.getParent() != this) child.setParent(this);
    }

    @Override
    public List<GuiComponent<?, ?>> getChildren(){
        return children;
    }

    // Lombok setter가 동작이 안됨;; 상속땜에 그런가
    public void setChildrenHorizontalAlignment(HorizontalAlignment childrenHorizontalAlignment){
        this.childrenHorizontalAlignment = childrenHorizontalAlignment;
    }

    public void setChildrenVerticalAlignment(VerticalAlignment childrenVerticalAlignment){
        this.childrenVerticalAlignment = childrenVerticalAlignment;
    }
}
