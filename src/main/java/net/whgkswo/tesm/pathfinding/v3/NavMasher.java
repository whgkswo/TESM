package net.whgkswo.tesm.pathfinding.v3;

import net.minecraft.entity.EntityType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.whgkswo.tesm.data.JsonManager;
import net.whgkswo.tesm.data.SizedStack;
import net.whgkswo.tesm.entitymanaging.EntityManager;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.pathfinding.v2.Direction;
import net.whgkswo.tesm.pathfinding.v2.JumpPointTestResult;
import net.whgkswo.tesm.pathfinding.v2.LinearSearcher;
import net.whgkswo.tesm.util.BlockPosUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static net.whgkswo.tesm.general.GlobalVariables.player;
import static net.whgkswo.tesm.general.GlobalVariables.world;

public class NavMasher {
    private static final int NAVMESH_CHUNK_RADIUS = 1;
    private final SizedStack<Boolean> stack = new SizedStack<>(2);
    private int cursorX;    private int cursorY;    private int cursorZ;
    boolean prevPosIsObstacle;
    public enum NavMeshMethod{
        MISSING,
        ALL
        ;
        public static NavMeshMethod getMethod(String str){
            if(str.equals("missing")){
                return MISSING;
            }else if (str.equals("all")){
                return ALL;
            }else{
                throw null;
            }
        }
    }
    public void navMesh(NavMeshMethod navMeshMethod, int chunkRadius){
        ChunkPos refChunkPos = player.getChunkPos();
        ArrayList<ChunkPos> targetChunkList = getTargetChunkPosList(refChunkPos, chunkRadius);
        /*player.sendMessage(Text.literal(targetChunkList.toString()));*/
        for(ChunkPos chunkPos : targetChunkList){
            scanChunk(chunkPos);
        }
    }
    private ArrayList<ChunkPos> getTargetChunkPosList(ChunkPos refChunkPos, int chunkRadius){
        ArrayList<ChunkPos> targetChunkList = new ArrayList<>();
        int startZ = refChunkPos.z - chunkRadius;
        int cursorX = refChunkPos.x - chunkRadius;
        int cursorZ = startZ;
        for(int i = 0; i< chunkRadius * 2 + 1; i++){
            for(int j = 0; j< chunkRadius * 2 + 1; j++){
                targetChunkList.add(new ChunkPos(cursorX,cursorZ));
                cursorZ++;
            }
            cursorX++;
            cursorZ = startZ;
        }
        return targetChunkList;
    }

    private void scanChunk(ChunkPos chunkPos){
        // 네비메쉬 범위 정하기
        Box box = createBox(chunkPos);
        // 유효한 좌표를 찾기
        HashMap<BlockPos, Boolean> validPosMap = getSteppableBlocks(box);
        /*for(BlockPos blockPos : validPosMap.keySet()){
            EntityManager.summonEntity(EntityType.FROG, blockPos);
        }*/
        // 내비메쉬 시작
        NavMeshDataOfChunk chunkData = new NavMeshDataOfChunk();
        for(BlockPos blockPos : validPosMap.keySet()){
            NavMeshDataOfBlockPos blockData = blockTest(blockPos);
            chunkData.putBlockData(blockPos, blockData);
        }
        // 청크 스캔 데이터를 파일로 저장하기
        /*ChunkPos chunkPos = world.getChunk(playerPos).getPos();*/
        String fileName = "r." + chunkPos.getRegionX() + "." + chunkPos.getRegionZ() + "/" +
                chunkPos.x + "." + chunkPos.z + ".json";

        long time = JsonManager.createJson(chunkData, fileName);
        player.sendMessage(Text.literal(fileName.substring(0, fileName.length()-5) + "청크에 대한 "
                + validPosMap.size() + "좌표 스캔 데이터 저장 완료 (" + time + "ms)"));
    }
    private NavMeshDataOfBlockPos blockTest(BlockPos blockPos){
        NavMeshDataOfBlockPos blockData = new NavMeshDataOfBlockPos();
        // 8방향에 대해서
        for(Direction direction : Direction.getAllDirections()){
            boolean obstacleFound = LinearSearcher.isObstacleFound(blockPos, direction);
            BlockPos nextPos = BlockPosUtil.getNextBlock(blockPos, direction);

            NavMeshDataOfDirection directionData;
            if(obstacleFound){
                directionData = new NavMeshDataOfDirection(true, null, null);
            }else{
                JumpPointTestResult jpTestResult = LinearSearcher.jumpPointTest(blockPos, nextPos,direction);
                directionData = new NavMeshDataOfDirection(
                        obstacleFound, jpTestResult.isLeftBlocked(), jpTestResult.isRightBlocked());
            }
            blockData.putDirectionData(direction, directionData);
        }
        return blockData;
    }
    private Box createBox(ChunkPos chunkPos){
        Box box = new Box(chunkPos.getStartX(), 0,chunkPos.getStartZ(), chunkPos.getEndX(), 0, chunkPos.getEndZ());
        /*player.sendMessage(Text.literal(box.toString()));*/
        return box;
    }
    private HashMap<BlockPos, Boolean> getSteppableBlocks(Box box){
        HashMap<BlockPos, Boolean> validPosMap = new HashMap<>();
        cursorX = (int) box.minX;
        for(int i = 0; i< 16; i++){
            cursorZ = (int) box.minZ;
            for(int j = 0; j< 16; j++){
                // 표면부터 등록하고 시작
                addSurfaceToMap(validPosMap);
                prevPosIsObstacle = true;

                while(cursorY > GlobalVariables.world.getBottomY()){
                    if(prevPosIsObstacle){
                        down3Blocks();
                    }else{
                        down1Blocks();
                    }
                    if(BlockPosUtil.isObstacle(new BlockPos(cursorX,cursorY,cursorZ))){
                        prevPosIsObstacle = true;
                        // 트랩 말고 진짜로 유효한, 단단한 블럭이었다면
                        if(!BlockPosUtil.isTrapBlock(new BlockPos(cursorX,cursorY,cursorZ))){
                            if(!stack.get(0) && !stack.get(1)){
                                // 유효한 좌표
                                validPosMap.put(new BlockPos(cursorX,cursorY,cursorZ), true);
                            }
                        }
                    }
                }
                cursorZ++;
            }
            cursorX ++;
        }
        return validPosMap;
    }
    private void addSurfaceToMap(HashMap<BlockPos, Boolean> validPosMap){
        //player.sendMessage(Text.literal(String.format("(%d, %d)", cursorX,cursorZ)));
        cursorY = GlobalVariables.world.
                getTopPosition(Heightmap.Type.WORLD_SURFACE, new BlockPos(cursorX,0,cursorZ)).getY() - 1 ;
        validPosMap.put(new BlockPos(cursorX, cursorY, cursorZ), true);
    }
    private void down3Blocks(){
        cursorY -= 3;
        stack.clear();
        stack.push(BlockPosUtil.isObstacle(new BlockPos(cursorX,cursorY,cursorZ).up(2)));
        stack.push(BlockPosUtil.isObstacle(new BlockPos(cursorX,cursorY,cursorZ).up()));
        prevPosIsObstacle = false;
    }
    private void down1Blocks(){
        cursorY -= 1;
        stack.push(BlockPosUtil.isObstacle(new BlockPos(cursorX,cursorY,cursorZ).up()));
    }
}
