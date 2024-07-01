package net.whgkswo.tesm.pathfinding.v3;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.entitymanaging.EntityManager;
import net.whgkswo.tesm.exceptions.ChunkDataNotFoundExeption;
import net.whgkswo.tesm.exceptions.EmptyOpenListExeption;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.pathfinding.v2.JumpPoint;
import net.whgkswo.tesm.pathfinding.v2.LargeSearchResult;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static net.whgkswo.tesm.general.GlobalVariables.player;

public class PathfinderV3 {
    private static final int MAX_SEARCH_RADIUS = 1;
    private static final int MAX_SEARCH_REPEAT_COUNT = 50000;
    private final Entity targetEntity;
    private final BlockPos startPos;    private final BlockPos endPos;
    private PriorityQueue<JumpPoint> openList;
    private HashSet<BlockPos> openSet;
    private HashMap<BlockPos, BlockPos> closedList;
    private final long startTime;

    public PathfinderV3(String targetName, BlockPos endPos){
        targetEntity = EntityManager.findEntityByName(targetName);
        startPos = targetEntity.getBlockPos().down(1);
        this.endPos = endPos;
        openList = new PriorityQueue<>(Comparator.comparingInt(JumpPoint::getFValue));
        openSet = new HashSet<>();
        closedList = new HashMap<>();
        startTime = System.currentTimeMillis();
        /*try{
            //pathfind();
            CompletableFuture.runAsync(this::pathfind);
        }catch (EmptyOpenListExeption e){
            player.sendMessage(Text.literal(String.format("탐색 실패, 갈 수 없는 곳입니다. (%s)", e.getClass().getSimpleName())));
        }catch (ChunkDataNotFoundExeption e){
            player.sendMessage(Text.literal(String.format("탐색 실패, [%s] 청크 스캔 데이터가 누락되었습니다.", e.getChunkPos())));
        }catch (Exception e){
            player.sendMessage(Text.literal("탐색 실패, 기타 오류가 발생했습니다."));
            e.printStackTrace();
        }*/
        CompletableFuture.runAsync(this::pathfind)
                .exceptionally(e -> {
                    handleException(e);
                    return null;
                });
    }
    private void handleException(Throwable e) {
        if (e.getCause() instanceof EmptyOpenListExeption) {
            player.sendMessage(Text.literal(String.format("탐색 실패, 갈 수 없는 곳입니다. (%s)", e.getClass().getSimpleName())));
        } else if (e.getCause() instanceof ChunkDataNotFoundExeption ex) {
            player.sendMessage(Text.literal(String.format("탐색 실패, %s 청크의 스캔 데이터가 누락되었습니다.", ex.getChunkPos())));
        } else {
            player.sendMessage(Text.literal("탐색 실패, 기타 오류가 발생했습니다."));
            e.printStackTrace();
        }
    }

    public PriorityQueue<JumpPoint> getOpenList() {
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
            LargeSearcherV3 largeSearcherV3 = new LargeSearcherV3(endPos, MAX_SEARCH_RADIUS, openList,openSet, closedList);
            // 경로 탐색 완료했으면 알고리즘 전체 종료
            result = largeSearcherV3.largeSearch(searchCount, startPos);
            if(result.isFoundDestination()){
                double duration = result.getTime() - startTime;

                int distance = backtrack(result.getLargeRefPos());
                player.sendMessage(Text.literal("목적지 탐색 완료 ("+ (distance + 1) + "m -> "
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
