package net.whgkswo.tesm.pathfinding.v3;

import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.whgkswo.tesm.data.ScanHelper;
import net.whgkswo.tesm.exceptions.BlockDataNotFoundExeption;
import net.whgkswo.tesm.exceptions.ChunkDataNotFoundExeption;
import net.whgkswo.tesm.exceptions.EmptyOpenListExeption;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.pathfinding.v2.*;
import net.whgkswo.tesm.helpers.BlockPosUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

import static net.whgkswo.tesm.general.GlobalVariables.scanDataMap;
import static net.whgkswo.tesm.general.GlobalVariables.world;

public class LargeSearcherV3 {
    private BlockPos endPos;
    private final int MAX_SEARCH_RADIUS;
    private PriorityQueue<JumpPoint> openList;
    private HashSet<BlockPos> openSet;
    private HashMap<BlockPos, BlockPos> closedList;

    public LargeSearcherV3(BlockPos endPos, int MAX_SEARCH_RADIUS, PriorityQueue<JumpPoint> openList,
                           HashSet<BlockPos> openSet, HashMap<BlockPos, BlockPos> closedList) {
        this.endPos = endPos;
        this.MAX_SEARCH_RADIUS = MAX_SEARCH_RADIUS;
        this.openList = openList;
        this.openSet = openSet;
        this.closedList = closedList;
    }

    public LargeSearchResult largeSearch(int searchCount, BlockPos startPos){
        searchCount++;
        // 다음 점프 포인트 선정 (F값이 최소인 걸로)
        if(openList.isEmpty()){
            throw new EmptyOpenListExeption();
        }
        // 대탐색 시작 위치 선정 및 해당 좌표를 오픈 리스트에서 제거
        JumpPoint nextJumpPoint = openList.poll();
        BlockPos refPos = nextJumpPoint.getBlockPos();
        openSet.remove(refPos);
        // 해당 좌표를 클로즈 리스트에 추가
        closedList.put(refPos,nextJumpPoint.getLargeRefPos());
        // 소탐색 방향 선정
        ArrayList<PathfindDirection> directions = DirectionSetter.setSearchDirections(startPos, nextJumpPoint);
        // 대탐색 정보 채팅창에 출력
        GlobalVariables.player.sendMessage(Text.of(String.format("(%d) (%s)를 기준으로 탐색, %s", searchCount,
                refPos.toShortString(), directions)), true);

        SearchResult result = new SearchResult(false, null);
        // 대탐색 시작 위치에 닭 소환
        /*EntityManager.summonEntity(EntityType.CHICKEN, refPos);*/
        // 좌표에 해당하는 청크 스캔 데이터 로드
        ScanDataOfChunk chunkData = loadChunkData(refPos);
        // 이 좌표의 데이터 가져오기
        ScanDataOfBlockPos scanData = chunkData.getBlockData(refPos);
        if(scanData == null){
            throw new BlockDataNotFoundExeption(refPos);
        }
        // 다음 방향들에 대해 소탐색 시작
        for(PathfindDirection direction : directions){
            // 이전 소탐색에서 사용된 갑옷 거치대와 알레이, 닭, 벌 없애기
            /*EntityManager.killEntities(EntityType.ARMOR_STAND, EntityType.CHICKEN, EntityType.BEE);*/
            // 탐색 실시
            result = search(refPos, direction, nextJumpPoint.getHValue(), scanData);
            // 목적지를 찾았으면 메서드 종료
            if(result.hasFoundDestination()){
                return new LargeSearchResult(true, refPos, result.getTime());
            }
        }
        return new LargeSearchResult();
    }
    public SearchResult search(BlockPos largeRefPos, PathfindDirection direction, int hValue, ScanDataOfBlockPos scanData){
        LinearSearcherV3 searcher = new LinearSearcherV3(largeRefPos, endPos, direction, MAX_SEARCH_RADIUS, scanData, openSet);
        // 주어진 방향에 대해 탐색 실행
        DiagSearchState diagSearchState = new DiagSearchState(0,direction);
        SearchResult result = searcher.linearSearch(BlockPosUtil.getCopyPos(largeRefPos),openList,closedList,diagSearchState,hValue);
        return result;
    }
    private static ScanDataOfChunk loadChunkData(BlockPos blockPos){
        ChunkPos chunkPos = world.getChunk(blockPos).getPos();
        if(scanDataMap.containsKey(chunkPos)){
            return scanDataMap.get(chunkPos);
        }else{
            if(ScanHelper.isChunkScanDataExist(chunkPos)){
                String filePath = "r." + chunkPos.getRegionX() + "." + chunkPos.getRegionZ()
                        + "/" + chunkPos.x + "." + chunkPos.z + ".json";
                ScanDataOfChunk chunkData = ScanHelper.readScanData(filePath);
                GlobalVariables.scanDataMap.put(chunkPos, chunkData);
                return chunkData;
            }else{
                throw new ChunkDataNotFoundExeption(chunkPos);
            }
        }
    }
}
