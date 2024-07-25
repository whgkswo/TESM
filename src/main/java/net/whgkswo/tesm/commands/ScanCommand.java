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

        // 업데이트는 뒤의 다른 인자들을 받을 필요가 없음
        if(scanMethod == ChunkScanner.ScanMethod.UPDATE){
            // 스캐너 생성과 함께 스캔(생성자에서 호출)
            new ChunkScanner(scanMethod).scan();
            // 스캔 후 업데이트 청크 목록 비우기
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
        // 스캐너 생성과 함께 스캔(생성자에서 호출)
        new ChunkScanner(scanMethod,chunkPos,chunkRadius).scan();
        return 1;
    }
}