package net.whgkswo.tesm.gui.libgui.widgets;

import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;

public class ClickablePlainPanel extends WPlainPanel {
    private Runnable onClickAction;
    public ClickablePlainPanel() {
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
