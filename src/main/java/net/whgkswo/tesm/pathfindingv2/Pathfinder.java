package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;

public class Pathfinder {
    private ServerWorld world;
    private static final int MAX_SEARCH_RADIUS = 10;
    private Entity targetEntity;
    private final BlockPos startPos;    private final BlockPos endPos;
    private int cursorX;    private int cursorY;    private int cursorZ;
    private ArrayList<JumpPoint> openList;
    private HashMap<BlockPos, JumpPoint> closedList;
    public Pathfinder(ServerWorld world, Entity targetEntity, BlockPos startPos, BlockPos endPos){
        this.targetEntity = targetEntity;
        this.startPos = startPos;   this.endPos = endPos;
        cursorX = startPos.getX();   cursorY = startPos.getY();   cursorZ = startPos.getZ();
        openList = new ArrayList<>();
        closedList = new HashMap<>();
        this.world = world;
    }
    public SearchResult search(BlockPos refPos, Direction direction){
        LinearSearcher searcher = new LinearSearcher(refPos, endPos, direction);
        return searcher.linearSearch(world,  MAX_SEARCH_RADIUS);
    }
}
