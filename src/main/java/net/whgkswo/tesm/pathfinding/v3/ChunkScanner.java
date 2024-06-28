package net.whgkswo.tesm.pathfinding.v3;

import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.whgkswo.tesm.data.JsonManager;
import net.whgkswo.tesm.data.SizedStack;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.pathfinding.v2.Direction;
import net.whgkswo.tesm.pathfinding.v2.JumpPointTestResult;
import net.whgkswo.tesm.pathfinding.v2.LinearSearcher;
import net.whgkswo.tesm.util.BlockPosUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static net.whgkswo.tesm.general.GlobalVariables.player;

public class ChunkScanner {
    private final SizedStack<Boolean> stack = new SizedStack<>(2);
    private int cursorX;    private int cursorY;    private int cursorZ;
    boolean prevPosIsObstacle;
    private long startTime;
    public enum ScanMethod {
        MISSING,
        ALL
        ;
        public static ScanMethod getMethod(String str){
            if(str.equals("missing")){
                return MISSING;
            }else if (str.equals("all")){
                return ALL;
            }else{
                throw null;
            }
        }
    }
    public ChunkScanner(){
        startTime = System.currentTimeMillis();
    }
    public void scan(ScanMethod method, int chunkRadius){
        ChunkPos refChunkPos = player.getChunkPos();
        ArrayList<ChunkPos> targetChunkList = getTargetChunkPosList(method, refChunkPos, chunkRadius);
        /*player.sendMessage(Text.literal(targetChunkList.toString()));*/
        if(targetChunkList.isEmpty()){
            player.sendMessage(Text.literal("누락된 청크 스캔 데이터가 없습니다."));
            return;
        }
        //long startTime = System.currentTimeMillis();
        for(int i = 0; i<= targetChunkList.size()-1; i++){
            scanChunk(targetChunkList.get(i), i+1, targetChunkList.size());
        }
        long finishedTime = System.currentTimeMillis();
        double timeInterval = (double) (finishedTime - startTime) /1000;
        player.sendMessage(Text.literal("스캔 완료 (" + timeInterval + "s)"));
    }
    private ArrayList<ChunkPos> getTargetChunkPosList(ScanMethod method, ChunkPos refChunkPos, int chunkRadius){
        ArrayList<ChunkPos> targetChunkList = new ArrayList<>();
        int startZ = refChunkPos.z - chunkRadius;
        int cursorX = refChunkPos.x - chunkRadius;
        int cursorZ = startZ;
        for(int i = 0; i< chunkRadius * 2 + 1; i++){
            for(int j = 0; j< chunkRadius * 2 + 1; j++){
                ChunkPos chunkPos = new ChunkPos(cursorX,cursorZ);
                if (method == ScanMethod.ALL
                        || (method == ScanMethod.MISSING && !isChunkScanDataExist(chunkPos))) {
                    targetChunkList.add(chunkPos);
                }
                cursorZ++;
            }
            cursorX++;
            cursorZ = startZ;
        }
        return targetChunkList;
    }
    private static boolean isChunkScanDataExist(ChunkPos chunkPos){
        String region = "r." + chunkPos.getRegionX() + "." + chunkPos.getRegionZ();
        String filePath = "/" + chunkPos.x + "." + chunkPos.z + ".json";
        File file = new File("config/tesm/scandata/" + region + filePath);
        return file.exists();
    }


    private void scanChunk(ChunkPos chunkPos, int progress, int total){
        // 네비메쉬 범위 정하기
        Box box = createBox(chunkPos);
        // 유효한 좌표를 찾기
        HashMap<BlockPos, Boolean> validPosMap = getSteppableBlocks(box);
        /*for(BlockPos blockPos : validPosMap.keySet()){
            EntityManager.summonEntity(EntityType.FROG, blockPos);
        }*/
        // 내비메쉬 시작
        ScanDataOfChunk chunkData = new ScanDataOfChunk(new HashMap<>());
        for(BlockPos blockPos : validPosMap.keySet()){
            ScanDataOfBlockPos blockData = blockTest(blockPos);
            chunkData.putBlockData(blockPos, blockData);
        }
        // 청크 스캔 데이터를 파일로 저장하기
        /*ChunkPos chunkPos = world.getChunk(playerPos).getPos();*/
        String fileName = "r." + chunkPos.getRegionX() + "." + chunkPos.getRegionZ() + "/" +
                chunkPos.x + "." + chunkPos.z + ".json";
        JsonManager.createJson(chunkData, fileName);
        //long time = System.currentTimeMillis() - startTime;
        player.sendMessage(Text.literal("[" + progress + "/" + total + "] "
                + fileName.substring(0, fileName.length()-5) + "청크에 대한 "
                + validPosMap.size() + "좌표 스캔 데이터 저장 완료"));
        /*(" + time + "ms)*/
    }
    private ScanDataOfBlockPos blockTest(BlockPos blockPos){
        ScanDataOfBlockPos blockData = new ScanDataOfBlockPos(new HashMap<>());
        // 8방향에 대해서
        for(Direction direction : Direction.getAllDirections()){
            boolean obstacleFound = LinearSearcher.isObstacleFound(blockPos, direction);
            BlockPos nextPos = BlockPosUtil.getNextBlock(blockPos, direction);

            ScanDataOfDirection directionData;
            if(obstacleFound){
                directionData = new ScanDataOfDirection(true, null, null);
            }else{
                JumpPointTestResult jpTestResult = LinearSearcher.jumpPointTest(blockPos, nextPos,direction);
                directionData = new ScanDataOfDirection(
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
