package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.entitymanaging.EntityManager;
import net.whgkswo.tesm.exceptions.EmptyOpenListExeption;
import net.whgkswo.tesm.exceptions.EntityNotFoundExeption;
import net.whgkswo.tesm.general.GlobalVariables;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static net.whgkswo.tesm.general.GlobalVariables.player;

public class Pathfinder {
    private static final int MAX_SEARCH_RADIUS = 1;
    private static final int MAX_SEARCH_REPEAT_COUNT = 500;
    private final Entity targetEntity;
    private final BlockPos startPos;    private final BlockPos endPos;
    private ArrayList<JumpPoint> openList;
    private HashMap<BlockPos, SearchResult> closedList;
    private HashMap<BlockPos, BlockPos> newClosedList;
    private final LocalDateTime startTime;


    public BlockPos getStartPos() {
        return startPos;
    }

    public Pathfinder(String targetName, BlockPos endPos){
        targetEntity = EntityManager.findEntityByName(targetName);

        startPos = targetEntity.getBlockPos().down(1);
        this.endPos = endPos;
        openList = new ArrayList<>();
        closedList = new HashMap<>();
        newClosedList = new HashMap<>();
        startTime = LocalDateTime.now();
        try{
            pathfind();
        }catch (EmptyOpenListExeption e){
            player.sendMessage(Text.literal(String.format("탐색 실패, 갈 수 없는 곳입니다. (%s)", e.getClass().getSimpleName())));
        }
    }

    public ArrayList<JumpPoint> getOpenList() {
        return openList;
    }
    public HashMap<BlockPos, SearchResult> getClosedList() {
        return closedList;
    }
    public void pathfind(){
        // 이전 탐색에서 사용된 갑옷 거치대와 알레이, 닭 ,벌 없애기
        EntityManager.killEntities(EntityType.ALLAY, EntityType.ARMOR_STAND, EntityType.CHICKEN, EntityType.BEE, EntityType.FROG);
        // 시작점, 끝점 표시
        BlockPos startPos = targetEntity.getBlockPos().down(1);
        /*EntityManager.summonEntity(EntityType.ARMOR_STAND, startPos);*/
        EntityManager.summonEntity(EntityType.ALLAY, endPos);
        // 시작점을 오픈리스트에 추가
        getOpenList().add(new JumpPoint(startPos,startPos, null, endPos, -1, false, false));
        // 탐색 - 탐색 결과 초기화
        LargeSearchResult result;
        int searchCount = 0;
        // 루프 돌며 대탐색 실시
        while(searchCount < MAX_SEARCH_REPEAT_COUNT){
            LargeSearcher largeSearcher = new LargeSearcher(endPos, MAX_SEARCH_RADIUS, openList,closedList, newClosedList);
            // 경로 탐색 완료했으면 알고리즘 전체 종료
            result = largeSearcher.largeSearch(searchCount, startPos);
            if(result.isFoundDestination()){
                String duration = Duration.between(startTime, result.getTime()).toString();

                int distance = backtrack(result.getLargeRefPos());
                player.sendMessage(Text.literal("목적지 탐색 완료 ("+ (distance + 1) + "m - "
                        + duration.substring(2) + "s)"));
                return;
            }
            searchCount++;
        }
        // 끝까지 길을 찾지 못했다면
        player.sendMessage(Text.literal("탐색 실패, 너무 멀거나 갈 수 없는 곳입니다."));
    }
    private int backtrack(BlockPos lastRefPos){
        BlockPos refPos = lastRefPos; // 마지막 원점 필요
        int backtrackCount = 0;
        ArrayList pathList = new ArrayList();
        while(!refPos.equals(startPos)){
            pathList.add(0, refPos);
            EntityManager.summonEntity(EntityType.FROG, refPos);
            refPos = newClosedList.get(refPos);
            backtrackCount++;
        }
        return backtrackCount;
        /*GlobalVariables.player.sendMessage(Text.literal(pathList.toString()));*/
    }
}
