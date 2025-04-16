package net.whgkswo.tesm.gui.libgui.client_side;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import net.minecraft.client.MinecraftClient;

public class BackgroundLessScreenDesc extends LightweightGuiDescription {
    public BackgroundLessScreenDesc(){
        super();
        //rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(0x00000000));
        rootPanel.setBackgroundPainter(null);
    }

    public void close(){
        MinecraftClient.getInstance().setScreen(null);
    }
}
