package net.whgkswo.tesm.gui.libgui.widgets;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.data.Axis;

import java.util.List;

public class WList extends WBox {
    public WList(List<ClickableLabel> items, GuiDescription gui) {
        super(Axis.VERTICAL);
        this.setSize(200, 200);

        setList(items, gui);
    }

    public void setList(List<ClickableLabel> items, GuiDescription gui){
        this.children.clear();
        for (ClickableLabel item : items){
            this.add(item);
        }
        this.validate(gui);
    }
}
