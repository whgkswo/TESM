package net.whgkswo.tesm.gui.libgui.widgets;

import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;

public class ClickableBox extends WBox {
    private Runnable onClickAction;

    public ClickableBox(Axis axis) {
        super(axis);
    }

    @Override
    public InputResult onClick(int x, int y, int button){
        if(onClickAction != null) onClickAction.run();
        return InputResult.PROCESSED;
    }

    public void setOnClick(Runnable runnable){
        onClickAction = runnable;
    }
}
