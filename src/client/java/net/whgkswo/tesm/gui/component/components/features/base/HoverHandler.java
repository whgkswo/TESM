package net.whgkswo.tesm.gui.component.components.features.base;

import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.components.features.BackgroundHoverHandler;

public abstract class HoverHandler {
    protected boolean isHovered;

    public abstract void handleHover();
    public abstract void handleHoverExit();
}
