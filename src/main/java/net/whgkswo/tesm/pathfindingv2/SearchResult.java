package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.util.math.BlockPos;

public class SearchResult {
    private boolean foundDestination;

    public boolean hasFoundDestination() {
        return foundDestination;
    }

    public SearchResult(boolean foundDestination){

        this.foundDestination = foundDestination;
    }

}
