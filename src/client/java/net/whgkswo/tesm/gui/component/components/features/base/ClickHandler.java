package net.whgkswo.tesm.gui.component.components.features.base;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.whgkswo.tesm.message.MessageHelper;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClickHandler {
    private Runnable onClick;

    public static final ClickHandler TEST = new ClickHandler(() -> MessageHelper.sendMessage("클릭!"));

    public void run(){
        onClick.run();
    }

    public static ClickHandler of(Runnable onClick){
        return new ClickHandler(onClick);
    }
}

