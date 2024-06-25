package net.whgkswo.tesm.pathfinding.v2;

public class DiagSearchState {
    private final int searchedCount;
    private final Direction direction;

    public Direction getDirection() {
        return direction;
    }

    public DiagSearchState(int searchedCount, Direction direction) {
        this.searchedCount = searchedCount;
        this.direction = direction;
    }

    public int getSearchedCount() {
        return searchedCount;
    }
}
