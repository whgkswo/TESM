package net.whgkswo.tesm.general;

import com.mojang.brigadier.ParseResults;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;

public class GlobalVariables {
    public static ServerWorld world;
    public static PlayerEntity player;
    public static ServerCommandSource commandSource;
    public static CommandManager commandManager;
    public static ParseResults<ServerCommandSource> parseResults;
}