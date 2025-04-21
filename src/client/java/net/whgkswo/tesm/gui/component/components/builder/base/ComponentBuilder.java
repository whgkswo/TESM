package net.whgkswo.tesm.gui.component.components.builder.base;

import net.whgkswo.tesm.gui.screen.base.TesmScreen;

public interface ComponentBuilder <C, B extends ComponentBuilder<C, B>>{
    B self();
    C build();
}
