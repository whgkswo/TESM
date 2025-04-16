package net.whgkswo.tesm.gui.component;

import lombok.Builder;
import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.component.bounds.Boundary;
import net.whgkswo.tesm.gui.component.bounds.RectangularBound;

import java.util.ArrayList;
import java.util.List;


public abstract class ParentComponent extends GuiComponent{
    private GuiDirection axis;
    private HorizontalAlignment childrenAlignment = HorizontalAlignment.LEFT;
    private final List<GuiComponent> children = new ArrayList<>();

    public ParentComponent(ParentComponent parent, RectangularBound bound, GuiDirection axis, HorizontalAlignment childrenAlignment) {
        super(parent);
        this.axis = axis;
        this.childrenAlignment = childrenAlignment;
    }

    public abstract Boundary getBound();

    public void render(DrawContext context){
        // 자신 렌더링
        renderSelf(context);
        // 자식 렌더링
        for (GuiComponent child : children){
            child.render(context);
        }
    }

    public void addChild(GuiComponent child){
        children.add(child);
        child.setParent(this);
    }
}
