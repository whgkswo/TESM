package net.whgkswo.tesm.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.whgkswo.tesm.pathfinding.v3.ChunkScanner;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;


public class ScanCommand {
    private static final SuggestionProvider<ServerCommandSource> METHOD_SUGGESTIONS = (context, builder) ->
            CommandSource.suggestMatching(new String[]{"missing", "all"}, builder);
    public static void register(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LiteralArgumentBuilder<ServerCommandSource> scanCommand = literal("scan")
                    .executes(ScanCommand::executeScanCommand)
                    .then(argument("method", StringArgumentType.string())
                            .suggests(METHOD_SUGGESTIONS)
                            /*.executes(ScanCommand::executeNavMeshCommand)*/
                            .then(argument("chunk_radius", IntegerArgumentType.integer())
                                    .executes(ScanCommand::executeScanCommand)));

            dispatcher.register(scanCommand);
        });
    }
    private static int executeScanCommand(CommandContext<ServerCommandSource> context) {
        String methodInput;
        int chunkRadius;
        try{
            methodInput = StringArgumentType.getString(context, "method");
        }catch (IllegalArgumentException e){
            methodInput = "missing";
        }
        try{
            chunkRadius = IntegerArgumentType.getInteger(context, "chunk_radius");
        }catch (IllegalArgumentException e){
            chunkRadius = 0;
        }
        ChunkScanner.ScanMethod scanMethod;
        ChunkScanner chunkScanner = new ChunkScanner();
        try{
            scanMethod = ChunkScanner.ScanMethod.getMethod(methodInput);
        }catch (NullPointerException e){
            context.getSource().sendError(Text.literal("잘못된 스캔 방식입니다."));
            return 0;
        }
        chunkScanner.scan(scanMethod, chunkRadius);
        return 1;
    }
}
