package net.whgkswo.tesm.pathfinding.v2;

import net.minecraft.util.math.BlockPos;

public class LargeSearchResult {
    private boolean foundDestination;
    private BlockPos largeRefPos;
    private long time;

    public LargeSearchResult() {
    }

    public boolean isFoundDestination() {
        return foundDestination;
    }

    public BlockPos getLargeRefPos() {
        return largeRefPos;
    }

    public long getTime() {
        return time;
    }

    public LargeSearchResult(boolean foundDestination, BlockPos largeRefPos, long time) {
        this.foundDestination = foundDestination;
        this.largeRefPos = largeRefPos;
        this.time = time;
    }
}
