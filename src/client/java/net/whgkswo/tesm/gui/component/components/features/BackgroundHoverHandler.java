package net.whgkswo.tesm.gui.component.components.features;

import lombok.AllArgsConstructor;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.components.features.base.BackgroundComponent;
import net.whgkswo.tesm.gui.component.components.features.base.HoverHandler;

public class BackgroundHoverHandler extends HoverHandler {
    private final GuiComponent<?, ?> master;
    private final TesmColor backgroundColorBackup;

    public BackgroundHoverHandler(GuiComponent<?, ?> master){
        if (!(master instanceof BackgroundComponent)) {
            throw new IllegalArgumentException(master.getClass().getSimpleName() + "은 BackgroundComponent의 구현체가 아닙니다.");
        }
        this.master = master;
        this.backgroundColorBackup = ((BackgroundComponent) master).getBackgroundColor();
    }

    @Override
    public void handleHover() {
        if(backgroundColorBackup.equals(TesmColor.TRANSPARENT)) return;
        onHover(backgroundColorBackup);
        isHovered = true;
    }

    @Override
    public void handleHoverExit(){
        if(backgroundColorBackup.equals(TesmColor.TRANSPARENT)) return;
        onHoverExit();
        isHovered = false;
    }

    public void onHover(TesmColor originalColor) {
        ((BackgroundComponent) master).setBackgroundColor(backgroundColorBackup.withAlpha(100));
        //MessageHelper.sendMessage("호버: " + getId());
    }

    public void onHoverExit() {
        // 원래 색상으로 복원
        ((BackgroundComponent) master).setBackgroundColor(backgroundColorBackup);
        //MessageHelper.sendMessage("호버 아웃: " + getId());
    }
}
