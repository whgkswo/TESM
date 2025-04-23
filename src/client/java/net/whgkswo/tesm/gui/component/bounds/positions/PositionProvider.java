package net.whgkswo.tesm.gui.component.bounds.positions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.ParentComponent;
import net.whgkswo.tesm.gui.component.bounds.PositionType;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;

@AllArgsConstructor
@Getter
public abstract class PositionProvider {
    protected final GuiComponent<?, ?> component;
    protected final ParentComponent<?, ?> parent;

    public abstract RelativeBound getAbsoluteBound();

    public abstract PositionType getType();
}
