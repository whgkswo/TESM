package net.whgkswo.tesm.gui.component.components;

import lombok.AllArgsConstructor;
import lombok.Setter;
import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.bounds.LinearBound;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.components.builder.SLineBuilder;
import net.whgkswo.tesm.gui.component.components.style.BoxStyle;
import net.whgkswo.tesm.gui.component.components.style.SLineStyle;

@Setter
public class SLine extends GuiComponent<SLine, SLineStyle> {
    private TesmColor color;
    private LinearBound bound;

    public static SLineBuilder builder(ParentComponent<?,?> parent){
        return new SLineBuilder(parent);
    }

    @Override
    public void renderSelf(DrawContext context) {
        RenderingHelper.drawLine(context, color, bound);
    }

    @Override
    public RelativeBound getBound() {
        return new RelativeBound(0, 0);
    }

    @Override
    protected Class<SLineStyle> getStyleType() {
        return SLineStyle.class;
    }
}
