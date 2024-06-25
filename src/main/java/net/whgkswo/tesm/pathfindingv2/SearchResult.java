package net.whgkswo.tesm.pathfindingv2;

import java.time.LocalDateTime;

public class SearchResult {
    private final boolean foundDestination;
    private final JumpPoint jumpPoint;
    private final Long time;

    public boolean hasFoundDestination() {
        return foundDestination;
    }

    public boolean hasJumpPoint(){
        return !(this.jumpPoint == null);
    }

    public JumpPoint getJumpPoint() {
        return jumpPoint;
    }

    public Long getTime() {
        return time;
    }

    public SearchResult(boolean foundDestination, JumpPoint jumpPoint){
        this.foundDestination = foundDestination;
        this.jumpPoint = jumpPoint;
        time = foundDestination ? System.currentTimeMillis() : null;
    }

}
