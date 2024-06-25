package net.whgkswo.tesm.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.whgkswo.tesm.util.IEntityDataSaver;

public class SummonVillager {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher,registryAccess,environment) ->
                dispatcher.register(
                        CommandManager.literal("summonVillager")
                                .then(CommandManager.argument("name", StringArgumentType.string())
                                        .executes(context -> {
                                            String name = StringArgumentType.getString(context, "name");
                                            return executeCommand(context.getSource(), name);
                                        }))
                )
        );
    }

    private static int executeCommand(ServerCommandSource source, String name) {

        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 필요한 인스턴스 정의 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

        ServerWorld world = source.getWorld();
        ServerPlayerEntity player = source.getPlayer();
        BlockPos playerPos = player.getBlockPos();
        SpawnReason reason = SpawnReason.COMMAND;
        Entity entity = EntityType.VILLAGER.spawn(world, playerPos, reason);
        //ServerPlayerEntity playerEntity = world.getServer().getPlayerManager().getPlayer(player.getUuid());


        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 엔티티 스폰 및 속성 설정 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        world.spawnEntity(entity);

        entity.setCustomNameVisible(false);
        entity.setSilent(true);
        entity.setCustomName(Text.of(name));

        NbtCompound vanillaNbt = entity.writeNbt(new NbtCompound()); // nbt 태그 읽어와서 변수에 쓰고(writeNbt)
        vanillaNbt.putBoolean("NoAI", true); // 원하는 데이터 끼워 넣은 다음에
        vanillaNbt.putBoolean("PersistenceRequired", true);
        entity.readNbt(vanillaNbt); // 다시 엔티티로 하여금 변수를 읽어들이게 하기(readNbt)

        NbtCompound customData = new NbtCompound();

        customData.putString("custom_key", "Custom Value");
        customData.putBoolean("interactable",true);
        ((IEntityDataSaver)entity).getPersistentData().put("EntityData",customData);


        // 이건 전체 nbt가 아니라 내가 추가한 TESMData안의 요소들만 가져옴
        NbtCompound nbt = ((IEntityDataSaver)entity).getPersistentData();

        String data = String.valueOf(nbt);
        String data2 = nbt.getCompound("EntityData").getString("custom_key");
        Boolean data3 = nbt.getCompound("EntityData").getBoolean("interactable");

        //player.sendMessage(Text.literal(data));
        //player.sendMessage(Text.literal(data2));
        //player.sendMessage(Text.literal(String.valueOf(data3)));

        return 0;
    }
}
