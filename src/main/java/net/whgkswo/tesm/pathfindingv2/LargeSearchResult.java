package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.util.math.BlockPos;

import java.time.LocalDateTime;

public class LargeSearchResult {
    private boolean foundDestination;
    private BlockPos largeRefPos;
    private LocalDateTime time;

    public LargeSearchResult() {
    }

    public boolean isFoundDestination() {
        return foundDestination;
    }

    public BlockPos getLargeRefPos() {
        return largeRefPos;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public LargeSearchResult(boolean foundDestination, BlockPos largeRefPos, LocalDateTime time) {
        this.foundDestination = foundDestination;
        this.largeRefPos = largeRefPos;
        this.time = time;
    }
}
