package net.whgkswo.tesm.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.whgkswo.tesm.pathfinding.v3.NavMasher;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;


public class NavMeshCommand {
    private static final SuggestionProvider<ServerCommandSource> METHOD_SUGGESTIONS = (context, builder) ->
            CommandSource.suggestMatching(new String[]{"missing", "all"}, builder);
    public static void register(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LiteralArgumentBuilder<ServerCommandSource> navMeshCommand = literal("navmesh")
                    .executes(NavMeshCommand::executeNavMeshCommand)
                    .then(argument("method", StringArgumentType.string())
                            .suggests(METHOD_SUGGESTIONS)
                            .executes(NavMeshCommand::executeNavMeshCommand));

            dispatcher.register(navMeshCommand);
        });
    }
    private static int executeNavMeshCommand(CommandContext<ServerCommandSource> context) {
        String methodInput;
        try{
            methodInput = StringArgumentType.getString(context, "method");
        }catch (IllegalArgumentException e){
            methodInput = "missing";
        }
        NavMasher.NavMeshMethod navMeshMethod;
        NavMasher navMasher = new NavMasher();
        try{
            navMeshMethod = NavMasher.NavMeshMethod.getMethod(methodInput);
        }catch (NullPointerException e){
            context.getSource().sendError(Text.literal("잘못된 네비메쉬 방식 입력입니다."));
            return 0;
        }
        navMasher.navMesh(navMeshMethod);
        return 1;
    }
}
