package net.whgkswo.tesm.entitymanaging;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.List;

public class EntityManager {
    public static Entity summonEntity(ServerWorld world, EntityType entityType, BlockPos blockPos){
        Entity entity = entityType.spawn(world, blockPos.up(1), SpawnReason.TRIGGERED);
        NbtCompound entityNbt = entity.writeNbt(new NbtCompound());
        entityNbt.putBoolean("NoAI", true);
        entity.readNbt(entityNbt);
        world.spawnEntity(entity);
        return entity;
    }
    public static void killEntity(ServerWorld world, EntityType entityType){
        List<Entity> entityList = world.getEntitiesByType(entityType,
                new Box(-10000, -64, -10000, 10000, 1024, 10000), entity -> true);

        entityList.forEach(Entity::kill);
    }
}
