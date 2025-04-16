package net.whgkswo.tesm.gui.libgui.widgets;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.text.Text;

public class ClickableLabel extends WLabel {
    private Runnable onClickAction;

    public ClickableLabel(Text text, int color) {
        super(text, color);
    }

    public ClickableLabel(Text text) {
        super(text);
    }

    public ClickableLabel(String str){
        super(Text.literal(str).withColor(0xffffff));
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
