package net.whgkswo.tesm.pathfinding.v3;

import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.whgkswo.tesm.data.JsonManager;
import net.whgkswo.tesm.data.SizedStack;
import net.whgkswo.tesm.data.dto.ChunkPosDto;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.pathfinding.v2.Direction;
import net.whgkswo.tesm.pathfinding.v2.JumpPointTestResult;
import net.whgkswo.tesm.pathfinding.v2.LinearSearcher;
import net.whgkswo.tesm.util.BlockPosUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static net.whgkswo.tesm.data.JsonManager.isChunkScanDataExist;
import static net.whgkswo.tesm.general.GlobalVariables.*;

public class ChunkScanner {
    private final ScanMethod method;
    private ArrayList<ChunkPos> targetChunkList;
    private ChunkPos refChunkPos;
    private int chunkRadius;
    private final SizedStack<Boolean> stack = new SizedStack<>(2);
    private int cursorX;    private int cursorY;    private int cursorZ;
    boolean prevPosIsObstacle;
    private long startTime;
    public enum ScanMethod {
        MISSING,
        ALL,
        UPDATE
        ;
        public static ScanMethod getMethod(String str){
            if(str.equals("missing")){
                return MISSING;
            }else if (str.equals("all")){
                return ALL;
            } else if (str.equals("update")) {
                return UPDATE;
            } else{
                throw null;
            }
        }
    }
    public ChunkScanner(ScanMethod method){
        this.method = method;
        startTime = System.currentTimeMillis();
        scan();
    }
    public ChunkScanner(ScanMethod method, ChunkPos refChunkPos, int chunkRadius){
        this.method = method;
        this.refChunkPos = refChunkPos;
        this.chunkRadius = chunkRadius;
        scan();
    }
    public void scan(){
        if(method == ScanMethod.UPDATE){
            targetChunkList = new ArrayList<>(GlobalVariables.updatedChunkSet);
            // 업데이트 청크 대기열 삭제
            updatedChunkSet.clear();
            JsonManager.deleteJson("/updatedChunkSet.json");
        }else{
            targetChunkList = getTargetChunkPosList(method, refChunkPos, chunkRadius);
        }

        if(targetChunkList.isEmpty()){
            if(method == ScanMethod.UPDATE) {
                player.sendMessage(Text.literal("청크 스캔 데이터가 최신 상태입니다."));
            }else{
                player.sendMessage(Text.literal("누락된 청크 스캔 데이터가 없습니다."));
            }
            return;
        }
        // 청크 리스트 순회하며 스캔
        for(int i = 0; i<= targetChunkList.size()-1; i++){
            scanChunk(targetChunkList.get(i), i+1, targetChunkList.size());
        }
        long finishedTime = System.currentTimeMillis();
        double timeInterval = (double) (finishedTime - startTime) /1000;
        player.sendMessage(Text.literal("스캔 완료 (" + timeInterval + "s)"));
    }
    public ArrayList<ChunkPos> getTargetChunkPosList(ScanMethod method, ChunkPos refChunkPos, int chunkRadius){
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


    private void scanChunk(ChunkPos chunkPos, int progress, int goal){
        // 스캔 범위 정하기
        Box box = createBox(chunkPos);
        // 유효한 좌표를 찾기
        HashSet<BlockPos> validPosSet = getSteppableBlocks(box);
        /*for(BlockPos blockPos : validPosSet.keySet()){
            EntityManager.summonEntity(EntityType.FROG, blockPos);
        }*/
        // 스캔 시작
        ScanDataOfChunk chunkData = new ScanDataOfChunk(new HashMap<>());
        for(BlockPos blockPos : validPosSet){
            ScanDataOfBlockPos blockData = blockTest(blockPos);
            chunkData.putBlockData(blockPos, blockData);
        }
        // 청크 스캔 데이터를 파일로 저장하기
        /*ChunkPos chunkPos = world.getChunk(playerPos).getPos();*/
        String chunkName = "/" + chunkPos.x + "." + chunkPos.z + ".json";
        String fileName = "r." + chunkPos.getRegionX() + "." + chunkPos.getRegionZ() + chunkName;
        JsonManager.createJson(chunkData, fileName);
        if(GlobalVariables.scanDataMap.containsKey(chunkPos)){
            scanDataMap.put(chunkPos,chunkData);
        }
        player.sendMessage(Text.literal("[" + progress + "/" + goal + "] "
                + chunkName.substring(1, chunkName.length()-5) + "청크에 대한 "
                + validPosSet.size() + "좌표 스캔 데이터 저장 완료"));
    }
    private ScanDataOfBlockPos blockTest(BlockPos blockPos){
        ScanDataOfBlockPos blockData = new ScanDataOfBlockPos(new HashMap<>());
        // 8방향에 대해서
        for(Direction direction : Direction.getAllDirections()){
            boolean obstacleFound = LinearSearcher.isObstacleFound(blockPos, direction);

            ScanDataOfDirection directionData;
            if(obstacleFound){
                directionData = new ScanDataOfDirection(true, null, null);
            }else{
                BlockPos nextPos = BlockPosUtil.getNextBlock(blockPos, direction);
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
    private HashSet<BlockPos> getSteppableBlocks(Box box){
        HashSet<BlockPos> validPosSet = new HashSet<>();
        cursorX = (int) box.minX;
        for(int i = 0; i< 16; i++){
            cursorZ = (int) box.minZ;
            for(int j = 0; j< 16; j++){
                // 표면부터 등록하고 시작
                addSurfaceToMap(validPosSet);
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
                                validPosSet.add(new BlockPos(cursorX,cursorY,cursorZ));
                            }
                        }
                    }
                }
                cursorZ++;
            }
            cursorX ++;
        }
        return validPosSet;
    }
    private void addSurfaceToMap(HashSet<BlockPos> validPosSet){
        //player.sendMessage(Text.literal(String.format("(%d, %d)", cursorX,cursorZ)));
        cursorY = GlobalVariables.world.
                getTopPosition(Heightmap.Type.WORLD_SURFACE, new BlockPos(cursorX,0,cursorZ)).getY() - 1 ;
        BlockPos blockPos = new BlockPos(cursorX,cursorY,cursorZ);
        // 해당 좌표의 블럭 높이가 낮으면 기준을 한 칸 내리기
        if(BlockPosUtil.getBlockHeight(blockPos) < 0.25){
            blockPos = blockPos.down();
        }
        validPosSet.add(blockPos);
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
