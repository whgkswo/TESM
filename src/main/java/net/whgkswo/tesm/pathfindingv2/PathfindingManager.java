package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.whgkswo.tesm.entitymanaging.EntityManager;

import java.util.List;

public class PathfindingManager {
    public static void pathfindingStart(ServerWorld world, String targetName, BlockPos endPos){
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
        // 갑옷 거치대와 알레이, 벌 없애기
        EntityManager.killEntity(world, EntityType.ALLAY);
        EntityManager.killEntity(world, EntityType.ARMOR_STAND);
        EntityManager.killEntity(world, EntityType.BEE);
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
            // 다음 점프 포인트 선정 (F값이 최소인 걸로)
            int nextIndex = OpenListManager.getMinFIndex(pathfinder.getOpenList());
            JumpPoint nextJumpPoint = pathfinder.getOpenList().get(nextIndex);
            // 다음 방향을 선정하고 탐색 시작
            for(Direction direction : DirectionSetter.setSearchDirections(startPos, nextJumpPoint)){
                result = pathfinder.search(nextJumpPoint.getBlockPos(), direction);
                if(result.hasFoundDestination()){
                    break;
                }
            }
            // 해당 좌표를 클로즈 리스트에 추가한 후 오픈 리스트에서 제거
            pathfinder.getClosedList().put(nextJumpPoint.getBlockPos(), result);
            pathfinder.getOpenList().remove(nextIndex);
        });
    }
}
