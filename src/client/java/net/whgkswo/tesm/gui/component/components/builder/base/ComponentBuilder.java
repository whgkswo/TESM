package net.whgkswo.tesm.gui.component.components.builder.base;

public interface ComponentBuilder <C, B extends ComponentBuilder<C, B>>{
    B self();
    C build();
}
