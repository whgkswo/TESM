package net.whgkswo.tesm.gui.libgui.widgets;

import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;

public class ClickableGridPanel extends WGridPanel {
    private Runnable onClickAction;

    public ClickableGridPanel() {
        super();
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
