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
import net.minecraft.util.math.ChunkPos;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.pathfinding.v3.ChunkScanner;

import java.util.ArrayList;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import static net.whgkswo.tesm.general.GlobalVariables.player;

public class ScanCommand {
    private static final SuggestionProvider<ServerCommandSource> METHOD_SUGGESTIONS = (context, builder) ->
            CommandSource.suggestMatching(new String[]{"missing", "all", "update"}, builder);
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LiteralArgumentBuilder<ServerCommandSource> scanCommand = literal("scan")
                    .then(argument("method", StringArgumentType.word())
                            .executes(ScanCommand::executeScanCommand)
                            .suggests(METHOD_SUGGESTIONS)
                            .then(argument("chunk_radius", IntegerArgumentType.integer(0))
                                    .executes(ScanCommand::executeScanCommand))
                            .then(argument("chunk_radius", IntegerArgumentType.integer())
                                    .then(argument("source_chunk_x", IntegerArgumentType.integer())
                                            .then(argument("source_chunk_z", IntegerArgumentType.integer(0))
                                                    .executes(ScanCommand::executeScanCommand)))));
            dispatcher.register(scanCommand);
        });
    }

    private static int executeScanCommand(CommandContext<ServerCommandSource> context) {
        String methodInput = StringArgumentType.getString(context, "method");
        ChunkScanner.ScanMethod scanMethod = ChunkScanner.ScanMethod.getMethod(methodInput);

        if(scanMethod == ChunkScanner.ScanMethod.UPDATE){
            ChunkScanner chunkScanner = new ChunkScanner(scanMethod);
            chunkScanner.scan();
            GlobalVariables.updatedChunkSet.clear();
            return 1;
        }
        int chunkRadius = IntegerArgumentType.getInteger(context, "chunk_radius");

        ChunkPos chunkPos;
        try{
            chunkPos = new ChunkPos(IntegerArgumentType.getInteger(context,"source_chunk_x"),
                    IntegerArgumentType.getInteger(context, "source_chunk_z"));
        }catch (IllegalArgumentException e){
            chunkPos = player.getChunkPos();
        }
        ChunkScanner chunkScanner = new ChunkScanner(scanMethod,chunkPos,chunkRadius);
        chunkScanner.scan();
        return 1;
    }
}