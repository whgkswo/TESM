package net.whgkswo.tesm.pathfinding.v2;

public class DiagSearchState {
    private final int searchedCount;
    private final PathfindDirection direction;

    public PathfindDirection getDirection() {
        return direction;
    }

    public DiagSearchState(int searchedCount, PathfindDirection direction) {
        this.searchedCount = searchedCount;
        this.direction = direction;
    }

    public int getSearchedCount() {
        return searchedCount;
    }
}
