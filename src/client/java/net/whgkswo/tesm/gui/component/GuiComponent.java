package net.whgkswo.tesm.gui.component;

import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;
import org.jetbrains.annotations.Nullable;

public abstract class GuiComponent {
    private String id;
    private String className;
    private boolean shouldHide;
    private HorizontalAlignment selfHorizontalAlignment = HorizontalAlignment.LEFT;
    private VerticalAlignment selfVerticalAlignment = VerticalAlignment.UPPER;
    @Nullable
    private ParentComponent parent;

    public GuiComponent(){
        this(null);
    }

    public GuiComponent(@Nullable ParentComponent parent){
        this.parent = parent;
    }

    public abstract void renderSelf(DrawContext context);


    public void render(DrawContext context){
        renderSelf(context);
    };

    public String getId(){
        return id;
    }

    public boolean shouldHide(){
        return shouldHide;
    }

    public @Nullable ParentComponent getParent(){
        return parent;
    }

    public void setParent(ParentComponent parent){
        this.parent = parent;
    }
}
