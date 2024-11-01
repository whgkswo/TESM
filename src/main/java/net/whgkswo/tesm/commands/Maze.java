package net.whgkswo.tesm.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.Blocks;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.pathfinding.maze.MazeGenerator;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Maze {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(literal("maze")
                        .then(argument("size", IntegerArgumentType.integer(3, 99))  // 11~99까지 가능
                                .executes(Maze::execute))));
    }

    private static int execute(CommandContext<ServerCommandSource> context){
        int size = IntegerArgumentType.getInteger(context, "size");

        // 미로 데이터 생성
        boolean[][] maze = new MazeGenerator().generate(size, size);
        // 미로 실제로 생성
        BlockPos playerPos = GlobalVariables.player.getBlockPos(); // 시작 좌표
        World world = GlobalVariables.world;

        int surfaceY = world.getTopY(Heightmap.Type.WORLD_SURFACE, playerPos.getX(), playerPos.getZ());
        for(int i = 0; i< maze.length; i++){
            for(int j = 0; j < maze[0].length; j++){
                if(!maze[i][j]){ // false인 경우가 벽
                    BlockPos blockPos = new BlockPos(playerPos.getX() + j + 1, surfaceY, playerPos.getZ() + i + 1);
                    world.setBlockState(blockPos, Blocks.STONE.getDefaultState());
                    world.setBlockState(blockPos.up(), Blocks.STONE.getDefaultState());
                }
            }
        }
        return 1;
    }
}
