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
    private int cursorX;    private int cursorY;    private int cursorZ;
    private ArrayList<JumpPoint> openList;
    private HashMap<BlockPos, SearchResult> closedList;

    public BlockPos getStartPos() {
        return startPos;
    }

    public Pathfinder(ServerWorld world, Entity targetEntity, BlockPos startPos, BlockPos endPos){
        this.targetEntity = targetEntity;
        this.startPos = startPos;   this.endPos = endPos;
        cursorX = startPos.getX();   cursorY = startPos.getY();   cursorZ = startPos.getZ();
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
        // 점프 포인트를 찾았을 때만
        if(result.hasJumpPoint()){
            openList.add(result.getJumpPoint());
        }
        // 탐색한 좌표를 클로즈리스트에 넣기
        closedList.put(refPos, result);
        return result;
    }
}
