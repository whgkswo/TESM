package net.whgkswo.tesm.pathfindingv2;

public class DiagSearchState {
    private final int searchedCount;
    private final Direction direction;
    private final int trailedDistance;

    public int getTrailedDistance() {
        return trailedDistance;
    }

    public Direction getDirection() {
        return direction;
    }

    public DiagSearchState(int searchedCount, Direction direction, int trailedDistance) {
        this.searchedCount = searchedCount;
        this.direction = direction;
        this.trailedDistance = trailedDistance;
    }

    public int getSearchedCount() {
        return searchedCount;
    }
}
