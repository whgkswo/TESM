package net.whgkswo.tesm.gui.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class InventoryScreen extends Screen {
    protected InventoryScreen(Text title) {
        super(title);
    }
    /*@Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }*/
    @Override
    public boolean shouldPause() {
        return false;
    }
}
