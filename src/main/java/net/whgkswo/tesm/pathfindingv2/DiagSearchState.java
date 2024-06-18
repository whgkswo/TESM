package net.whgkswo.tesm.pathfindingv2;

public class DiagSearchState {
    private final int searchedCount;
    private final Direction direction;
    private final boolean xBlocked;
    private final boolean zBlocked;

    public Direction getDirection() {
        return direction;
    }

    public DiagSearchState(int searchedCount, Direction direction, boolean xBlocked, boolean zBlocked) {
        this.searchedCount = searchedCount;
        this.direction = direction;
        this.xBlocked = xBlocked;
        this.zBlocked = zBlocked;
    }

    public int getSearchedCount() {
        return searchedCount;
    }

    public boolean isxBlocked() {
        return xBlocked;
    }

    public boolean iszBlocked() {
        return zBlocked;
    }
}
