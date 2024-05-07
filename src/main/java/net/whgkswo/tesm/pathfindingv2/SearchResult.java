package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.util.math.BlockPos;

public class SearchResult {
    private boolean foundDestination;
    private JumpPoint jumpPoint;

    public boolean hasFoundDestination() {
        return foundDestination;
    }

    public SearchResult(boolean foundDestination, JumpPoint jumpPoint){
        this.foundDestination = foundDestination;
        this.jumpPoint = jumpPoint;
    }

}
