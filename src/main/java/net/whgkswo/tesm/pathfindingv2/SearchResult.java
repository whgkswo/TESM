package net.whgkswo.tesm.pathfindingv2;

import java.time.LocalDateTime;

public class SearchResult {
    private boolean foundDestination;
    private JumpPoint jumpPoint;
    private LocalDateTime time;

    public boolean hasFoundDestination() {
        return foundDestination;
    }

    public boolean hasJumpPoint(){
        return !(this.jumpPoint == null);
    }

    public JumpPoint getJumpPoint() {
        return jumpPoint;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public SearchResult(boolean foundDestination, JumpPoint jumpPoint){
        this.foundDestination = foundDestination;
        this.jumpPoint = jumpPoint;
        time = LocalDateTime.now();
    }

}
