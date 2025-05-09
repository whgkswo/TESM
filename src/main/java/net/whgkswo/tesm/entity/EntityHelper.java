package net.whgkswo.tesm.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.whgkswo.tesm.exceptions.EntityNotFoundExeption;
import net.whgkswo.tesm.nbt.NBTHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static net.whgkswo.tesm.general.GlobalVariables.world;

public class EntityHelper {
    public static Entity summonEntity(EntityType entityType, BlockPos blockPos){
        Entity entity = entityType.spawn(world, blockPos.up(1), SpawnReason.TRIGGERED);
        NbtCompound entityNbt = entity.writeNbt(new NbtCompound());
        entityNbt.putBoolean("NoAI", true);
        entity.readNbt(entityNbt);
        world.spawnEntity(entity);
        return entity;
    }
    public static void killEntities(EntityType entityType){
        List<Entity> entityList = world.getEntitiesByType(entityType,
                new Box(-10000, -64, -10000, 10000, 1024, 10000), entity -> true);
        entityList.forEach(entity -> entity.kill(world));
    }
    public static void killEntities(EntityType ...entityTypes){
        HashSet entityTypeSet = new HashSet<>(Arrays.asList(entityTypes));

        List<Entity> allEntities = world.getEntitiesByClass(Entity.class,
                new Box(-10000, -64, -10000, 10000, 1024, 10000), entity -> true);
        allEntities.stream()
                .filter(entity -> entityTypeSet.contains(entity.getType()))
                .forEach(entity -> entity.kill(world));
    }
    public static Entity findEntityByName(String targetName) {
        // 월드 내 이름이 일치하는 주민 찾기
        List<VillagerEntity> entityList = world.getEntitiesByType(EntityType.VILLAGER,
                new Box(-10000, -64, -10000, 10000, 1024, 10000), entity -> {
                    try{
                        NbtCompound nbt = NBTHelper.getEntityNbt(entity);
                        String name = nbt.getString("Name");
                        return name != null && name.equals(targetName);
                    }catch (NullPointerException e){
                        return false;
                    }
                });
        if(entityList.isEmpty()){
            world.getPlayers().forEach(player -> {
                player.sendMessage(Text.literal(targetName + "을(를) 찾을 수 없습니다."));
            });
            throw new EntityNotFoundExeption();
        }
        return entityList.get(0);
    }
}
