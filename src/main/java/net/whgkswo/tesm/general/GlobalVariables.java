package net.whgkswo.tesm.general;

import com.mojang.brigadier.ParseResults;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.whgkswo.tesm.calendar.Time;
import net.whgkswo.tesm.data.dto.ChunkPosDto;
import net.whgkswo.tesm.pathfinding.v3.ScanDataOfChunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GlobalVariables {
    public static ServerWorld world;
    public static PlayerEntity player;
    public static HashMap<ChunkPos, ScanDataOfChunk> scanDataMap = new HashMap<>();
    public static ServerCommandSource commandSource;
    public static CommandManager commandManager;
    public static ParseResults<ServerCommandSource> parseResults;
    public static ArrayList<Entity> pathfindEntityList = new ArrayList<>();
    public static HashSet<ChunkPosDto> updatedChunkSet = new HashSet<>();
    public static final double LOW_BLOCKHEIGHT_REF = 0.25;
    public static Time currentTime = new Time(0,0,0);
}
