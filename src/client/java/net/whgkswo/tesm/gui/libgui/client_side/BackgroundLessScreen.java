package net.whgkswo.tesm.gui.libgui.client_side;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;

public class BackgroundLessScreen extends CottonClientScreen {
    public BackgroundLessScreen(BackgroundLessScreenDesc description) {
        super(description);
    }

    @Override
    protected void applyBlur() {
        // 블러 안 하기
    }

}
