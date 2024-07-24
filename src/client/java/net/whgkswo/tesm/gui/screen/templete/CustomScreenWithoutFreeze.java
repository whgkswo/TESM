package net.whgkswo.tesm.gui.screen.templete;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class CustomScreenWithoutFreeze extends Screen {
    public CustomScreenWithoutFreeze(){
        super(Text.literal("GUI 템플릿 (No Freeze)"));
    }
    @Override
    public boolean shouldPause(){
        return false;
    }
}
