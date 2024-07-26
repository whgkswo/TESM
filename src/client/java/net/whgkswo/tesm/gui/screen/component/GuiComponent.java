package net.whgkswo.tesm.gui.screen.component;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.gui.Alignment;

import java.util.HashMap;
import java.util.Map;

public abstract class GuiComponent {
    private String name;

    public GuiComponent(String name) {
        this.name = name;
    }
    public abstract void render(DrawContext context);
}
