package net.whgkswo.tesm.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.entity.EntityHelper;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.message.MessageHelper;
import net.whgkswo.tesm.nbt.IEntityDataSaver;

public class SummonNpc {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher,registryAccess,environment) ->
                dispatcher.register(
                        CommandManager.literal("summonNpc")
                                .then(CommandManager.argument("EngName", StringArgumentType.string())
                                        .then(CommandManager.argument("Name", StringArgumentType.string())
                                                .executes(SummonNpc::executeCommand))
                                        .then(CommandManager.argument("Name", StringArgumentType.string())
                                                .then(CommandManager.argument("TempName", StringArgumentType.string())
                                                        .executes(SummonNpc::executeCommand)))
                                )
                ));
    }

    private static int executeCommand(CommandContext<ServerCommandSource> context) {

        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 필요한 인스턴스 정의 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

        BlockPos playerPos = GlobalVariables.player.getBlockPos();
        Entity entity = EntityHelper.summonEntity(EntityType.VILLAGER, playerPos.down());

        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 엔티티 스폰 및 속성 설정 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        GlobalVariables.world.spawnEntity(entity);

        entity.setCustomNameVisible(false);
        entity.setSilent(true);
        //entity.setCustomName(Text.of(StringArgumentType.getString(context,"Name")));

        NbtCompound vanillaNbt = entity.writeNbt(new NbtCompound()); // nbt 태그 읽어와서 변수에 쓰고(writeNbt)
        vanillaNbt.putBoolean("NoAI", true); // 원하는 데이터 끼워 넣은 다음에
        vanillaNbt.putBoolean("PersistenceRequired", true);
        entity.readNbt(vanillaNbt); // 다시 엔티티로 하여금 변수를 읽어들이게 하기(readNbt)

        NbtCompound customData = new NbtCompound();
        customData.putString("EngName", StringArgumentType.getString(context, "EngName"));
        customData.putString("Name", StringArgumentType.getString(context,"Name"));
        String tempName;
        try{
            tempName = StringArgumentType.getString(context,"TempName");
        }catch (IllegalArgumentException e){
            tempName = "";
        }
        customData.putString("TempName", tempName);

        customData.putBoolean("Interactable",true);
        ((IEntityDataSaver)entity).getPersistentData().put("EntityData",customData);

        // 이건 전체 nbt가 아니라 내가 추가한 tesm 안의 요소들만 가져옴
        NbtCompound nbt = ((IEntityDataSaver)entity).getPersistentData();

        String data = String.valueOf(nbt);

        MessageHelper.sendMessage(data);

        return 0;
    }
}
