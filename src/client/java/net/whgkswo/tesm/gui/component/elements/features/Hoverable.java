package net.whgkswo.tesm.gui.component.elements.features;

import net.whgkswo.tesm.gui.component.GuiComponent;

public interface Hoverable {
    void handleHover();
    void handleHoverExit();
    boolean isHovered();
}
