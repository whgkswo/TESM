package net.whgkswo.tesm.pathfinding.v2;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.entitymanaging.EntityManager;
import net.whgkswo.tesm.exceptions.EmptyOpenListExeption;
import net.whgkswo.tesm.general.GlobalVariables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import static net.whgkswo.tesm.general.GlobalVariables.player;

public class Pathfinder {
    private static final int MAX_SEARCH_RADIUS = 1;
    private static final int MAX_SEARCH_REPEAT_COUNT = 50000;
    private final Entity targetEntity;
    private final BlockPos startPos;    private final BlockPos endPos;
    private ArrayList<JumpPoint> openList;
    private HashMap<BlockPos, BlockPos> closedList;
    private final long startTime;


    public Pathfinder(String targetName, BlockPos endPos){
        targetEntity = EntityManager.findEntityByName(targetName);

        startPos = targetEntity.getBlockPos().down(1);
        this.endPos = endPos;
        openList = new ArrayList<>();
        closedList = new HashMap<>();
        startTime = System.currentTimeMillis();
        try{
            CompletableFuture.runAsync(this::pathfind);
        }catch (EmptyOpenListExeption e){
            player.sendMessage(Text.literal(String.format("탐색 실패, 갈 수 없는 곳입니다. (%s)", e.getClass().getSimpleName())));
        }
    }

    public ArrayList<JumpPoint> getOpenList() {
        return openList;
    }

    public void pathfind(){
        // 이전 탐색에서 사용된 갑옷 거치대와 알레이, 닭 ,벌 없애기
        GlobalVariables.pathfindEntityList.forEach(Entity::kill);
        GlobalVariables.pathfindEntityList.clear();
        //EntityManager.killEntities(EntityType.ALLAY, EntityType.ARMOR_STAND, EntityType.CHICKEN, EntityType.BEE, EntityType.FROG);
        // 시작점, 끝점 표시
        BlockPos startPos = targetEntity.getBlockPos().down(1);
        /*EntityManager.summonEntity(EntityType.ARMOR_STAND, startPos);*/
        GlobalVariables.pathfindEntityList.add(EntityManager.summonEntity(EntityType.ALLAY, endPos));
        // 시작점을 오픈리스트에 추가
        getOpenList().add(new JumpPoint(startPos,startPos, null, endPos, -1, false, false));
        // 탐색 - 탐색 결과 초기화
        LargeSearchResult result;
        int searchCount = 0;
        // 루프 돌며 대탐색 실시
        while(searchCount < MAX_SEARCH_REPEAT_COUNT){
            LargeSearcher largeSearcher = new LargeSearcher(endPos, MAX_SEARCH_RADIUS, openList, closedList);
            // 경로 탐색 완료했으면 알고리즘 전체 종료
            result = largeSearcher.largeSearch(searchCount, startPos);
            if(result.isFoundDestination()){
                double duration = result.getTime() - startTime;

                int distance = backtrack(result.getLargeRefPos());
                player.sendMessage(Text.literal("목적지 탐색 완료 ("+ (distance + 1) + "m - "
                        + duration / 1000 + "s)"));
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
            GlobalVariables.pathfindEntityList.add(EntityManager.summonEntity(EntityType.FROG, refPos));
            refPos = closedList.get(refPos);
            backtrackCount++;
        }
        return backtrackCount;
        /*GlobalVariables.player.sendMessage(Text.literal(pathList.toString()));*/
    }
}
