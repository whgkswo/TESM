package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;

public class Pathfinder {
    private ServerWorld world;
    private static final int MAX_SEARCH_RADIUS = 10;
    private Entity targetEntity;
    private final BlockPos startPos;    private final BlockPos endPos;
    private ArrayList<JumpPoint> openList;
    private HashMap<BlockPos, SearchResult> closedList;

    public BlockPos getStartPos() {
        return startPos;
    }

    public Pathfinder(ServerWorld world, Entity targetEntity, BlockPos startPos, BlockPos endPos){
        this.targetEntity = targetEntity;
        this.startPos = startPos;   this.endPos = endPos;
        openList = new ArrayList<>();
        closedList = new HashMap<>();
        this.world = world;
    }

    public ArrayList<JumpPoint> getOpenList() {
        return openList;
    }

    public HashMap<BlockPos, SearchResult> getClosedList() {
        return closedList;
    }

    public SearchResult search(BlockPos refPos, Direction direction){
        LinearSearcher searcher = new LinearSearcher(refPos, endPos, direction);
        // 일직선 탐색 실행
        SearchResult result = searcher.linearSearch(world,openList,closedList,MAX_SEARCH_RADIUS);
        // 탐색한 좌표를 클로즈리스트에 넣기
        closedList.put(refPos, result);
        return result;
    }
}
