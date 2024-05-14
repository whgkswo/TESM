package net.whgkswo.tesm.pathfindingv2;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.whgkswo.tesm.entitymanaging.EntityManager;

import java.util.List;

public class PathfindingManager {
    private static final int MAX_SEARCH_REPEAT_COUNT = 100;
    public void pathfindingStart(ServerWorld world, String targetName, BlockPos endPos){
        // 월드 내 이름이 일치하는 주민 찾기
        List<VillagerEntity> entityList = world.getEntitiesByType(EntityType.VILLAGER,
                new Box(-10000, -64, -10000, 10000, 1024, 10000), entity -> {
            try{
                Text name = entity.getCustomName();
                return name != null && name.getString().equals(targetName);
            }catch (NullPointerException e){
                return false;
            }
        });
        if(entityList.isEmpty()){
            world.getPlayers().forEach(player -> {
                player.sendMessage(Text.literal("엔티티를 찾을 수 없습니다."));
            });
            return;
        }
        // 이전 탐색에서 사용된 갑옷 거치대와 알레이, 닭 ,벌 없애기
        EntityManager.killEntities(world, EntityType.ALLAY);
        EntityManager.killEntities(world, EntityType.ARMOR_STAND);
        EntityManager.killEntities(world, EntityType.CHICKEN);
        EntityManager.killEntities(world, EntityType.BEE);
        // 길찾기 시작
        entityList.forEach(entity -> {
            BlockPos startPos = entity.getBlockPos().down(1);
            Pathfinder pathfinder = new Pathfinder(world, entity, startPos, endPos);
            // 시작점, 끝점 표시
            EntityManager.summonEntity(world, EntityType.ARMOR_STAND, startPos);
            EntityManager.summonEntity(world, EntityType.ALLAY, endPos);
            // 시작점을 오픈리스트에 추가
            pathfinder.getOpenList().add(new JumpPoint(startPos, null, endPos, -1, false, false));
            // 탐색 - 탐색 결과 초기화
            SearchResult result = new SearchResult(false, null);
            int searchCount = 0;
            // 루프 돌며 대탐색 실시
            boolean pathfindingOn = true;
            while(!result.hasFoundDestination() && searchCount < MAX_SEARCH_REPEAT_COUNT){
                largeSearch(world,searchCount,pathfinder,result,pathfindingOn);
            }
            // 끝까지 길을 찾지 못했다면
            if(pathfindingOn){
                world.getPlayers().forEach(player ->{
                    player.sendMessage(Text.literal("탐색 실패, 너무 멀거나 갈 수 없는 곳입니다."));
                });
            }
        });
    }
    public static void largeSearch(ServerWorld world, int searchCount, Pathfinder pathfinder, SearchResult result,boolean pathfindingOn){
        searchCount++;
        int searchCount2 = searchCount;
        // 다음 점프 포인트 선정 (F값이 최소인 걸로)
        int nextIndex = OpenListManager.getMinFIndex(pathfinder.getOpenList());
        JumpPoint nextJumpPoint = pathfinder.getOpenList().get(nextIndex);
        BlockPos refPos = nextJumpPoint.getBlockPos();
        world.getPlayers().forEach(player ->{
            player.sendMessage(Text.of(String.format("(%d, %d, %d)를 기준으로 탐색(%d)", refPos.getX(),
                    refPos.getY(), refPos.getZ(), searchCount2)));
        });
        // 다음 방향을 선정하고 소탐색 시작
        for(Direction direction : DirectionSetter.setSearchDirections(refPos, nextJumpPoint)){
            // 이전 소탐색에서 사용된 갑옷 거치대와 알레이, 닭, 벌 없애기
            EntityManager.killEntities(world, EntityType.ARMOR_STAND);
            EntityManager.killEntities(world, EntityType.CHICKEN);
            EntityManager.killEntities(world, EntityType.BEE);
            // 탐색 실시
            result = pathfinder.search(nextJumpPoint.getBlockPos(), direction);
            // 목적지를 찾았으면
            if(result.hasFoundDestination()){
                world.getPlayers().forEach(player -> {
                    player.sendMessage(Text.literal("목적지 탐색 완료"));
                });
                pathfindingOn = false;
                break;
            }
        }
        // 해당 좌표를 클로즈 리스트에 추가한 후 오픈 리스트에서 제거
        pathfinder.getClosedList().put(nextJumpPoint.getBlockPos(), result);
        pathfinder.getOpenList().remove(nextIndex);
    }
    public static void onServerTicks(ServerWorld world, int searchCount, Pathfinder pathfinder, SearchResult result,boolean pathfindingOn){
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if(pathfindingOn){
                largeSearch(world, searchCount,pathfinder,result,pathfindingOn);
            }
        });
    }
}
