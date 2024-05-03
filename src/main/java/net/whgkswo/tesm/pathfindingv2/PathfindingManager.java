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
            Pathfinder pathfinder = new Pathfinder(world, startPos, endPos);

            EntityManager.summonEntity(world, EntityType.ARMOR_STAND, startPos);
            EntityManager.summonEntity(world, EntityType.ALLAY, endPos);

            // 초기 8방향 탐색
            SearchResult result = new SearchResult(false);
            /*int[][] directions = new int[][]{{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}};*/

            int i = 1;
            while(!result.hasFoundDestination() && i <= 8){
                /*result = pathfinder.search(startPos, new Direction(directions[i][0], directions[i][1]));*/
                result = pathfinder.search(startPos, Directions.getDirectionByNumber(i).getDirection());
                i++;
            }
        });
    }
}
