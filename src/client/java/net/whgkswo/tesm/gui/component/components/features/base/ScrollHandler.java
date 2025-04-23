package net.whgkswo.tesm.gui.component.components.features.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.whgkswo.tesm.gui.component.components.BoxPanel;
import net.whgkswo.tesm.message.MessageHelper;

@Setter
@AllArgsConstructor
public class ScrollHandler {
    private static final double BASE_SCROLL_SPEED = 0.2;
    @Getter
    private double offset;
    private double scrollSpeed = BASE_SCROLL_SPEED;
    private BoxPanel master;

    public ScrollHandler(BoxPanel master){
        this.master = master;
    }

    public void onScrollUp(){
        double offsetBackup = offset;
        offset -= scrollSpeed;
        offset = Math.max(offset, 0);
        // 오프셋 값이 바뀔 때만 캐시 초기화
        if(offsetBackup != offset) master.clearCachedBounds();
        //MessageHelper.sendMessage("스크롤 업, offset: " + offset);
    }

    public void onScrollDown(){
        double childrenHeight = master.getSumOfChildrenRelativeHeight();
        double offsetBackup = offset;

        offset += scrollSpeed;
        // 자식 요소들이 부모보다 커야 스크롤 가능
        double maxOffset = Math.max(0, childrenHeight - 1);
        offset = Math.min(offset, maxOffset);
        // 오프셋 값이 바뀔 때만 캐시 초기화
        if(offsetBackup != offset) master.clearCachedBounds();
        //MessageHelper.sendMessage("스크롤 다운, offset: " + offset);
    }
}
