package net.whgkswo.tesm.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.networking.payload.data.GeneralReq;
import net.whgkswo.tesm.networking.payload.id.GeneralTask;

import static net.minecraft.server.command.CommandManager.literal;

public class ResetQuests {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LiteralArgumentBuilder<ServerCommandSource> scanCommand = literal("reset_quests")
                    .executes(ResetQuests::execute);
            dispatcher.register(scanCommand);
        });
    }
    private static int execute(CommandContext<ServerCommandSource> context){
        ServerPlayNetworking.send((ServerPlayerEntity) GlobalVariables.player, new GeneralReq(GeneralTask.RESET_QUESTS));
        return 1;
    }
}
