package net.whgkswo.tesm.pathfinding.v3;

import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.whgkswo.tesm.data.JsonManager;
import net.whgkswo.tesm.exceptions.ChunkDataNotFoundExeption;
import net.whgkswo.tesm.exceptions.EmptyOpenListExeption;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.pathfinding.v2.*;
import net.whgkswo.tesm.util.BlockPosUtil;
import net.whgkswo.tesm.util.OpenListManager;

import java.util.ArrayList;
import java.util.HashMap;

import static net.whgkswo.tesm.general.GlobalVariables.scanDataMap;
import static net.whgkswo.tesm.general.GlobalVariables.world;

public class LargeSearcherV3 {
    private BlockPos endPos;
    private final int MAX_SEARCH_RADIUS;

    private ArrayList<JumpPoint> openList;
    private HashMap<BlockPos, BlockPos> closedList;

    public LargeSearcherV3(BlockPos endPos, int MAX_SEARCH_RADIUS, ArrayList<JumpPoint> openList,
                           HashMap<BlockPos, BlockPos> closedList) {
        this.endPos = endPos;
        this.MAX_SEARCH_RADIUS = MAX_SEARCH_RADIUS;
        this.openList = openList;
        this.closedList = closedList;
    }

    public LargeSearchResult largeSearch(int searchCount, BlockPos startPos){
        searchCount++;
        // 다음 점프 포인트 선정 (F값이 최소인 걸로)
        int nextIndex = OpenListManager.getMinFIndex(openList);
        if(openList.isEmpty()){
            throw new EmptyOpenListExeption();
        }
        JumpPoint nextJumpPoint = openList.get(nextIndex);
        // 대탐색 시작 위치 선정
        BlockPos refPos = nextJumpPoint.getBlockPos();
        // 소탐색 방향 선정
        ArrayList<Direction> directions = DirectionSetter.setSearchDirections(startPos, nextJumpPoint);
        // 대탐색 정보 채팅창에 출력
        GlobalVariables.player.sendMessage(Text.of(String.format("(%d) (%d, %d, %d)를 기준으로 탐색, %s", searchCount,
                refPos.getX(), refPos.getY(), refPos.getZ(), directions)));

        SearchResult result = new SearchResult(false, null);
        // 대탐색 시작 위치에 닭 소환
        /*EntityManager.summonEntity(EntityType.CHICKEN, refPos);*/
        // 해당 좌표를 클로즈 리스트에 추가한 후 오픈 리스트에서 제거
        closedList.put(refPos,nextJumpPoint.getLargeRefPos());
        openList.remove(nextIndex);
        // 좌표에 해당하는 청크 스캔 데이터 로드
        ScanDataOfChunk chunkData = loadChunkData(refPos);
        // 이 좌표의 데이터 가져오기
        ScanDataOfBlockPos scanData = chunkData.getBlockData(refPos);
        // 다음 방향들에 대해 소탐색 시작
        for(Direction direction : directions){
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
    public SearchResult search(BlockPos largeRefPos, Direction direction, int hValue, ScanDataOfBlockPos scanData){
        LinearSearcherV3 searcher = new LinearSearcherV3(largeRefPos, endPos, direction, MAX_SEARCH_RADIUS, scanData);
        // 주어진 방향에 대해 탐색 실행
        DiagSearchState diagSearchState = new DiagSearchState(0,direction);
        SearchResult result = searcher.linearSearch(BlockPosUtil.getCopyPos(largeRefPos),openList, closedList,diagSearchState,hValue);
        return result;
    }
    private static ScanDataOfChunk loadChunkData(BlockPos blockPos){
        ChunkPos chunkPos = world.getChunk(blockPos).getPos();
        if(scanDataMap.containsKey(chunkPos)){
            return scanDataMap.get(chunkPos);
        }else{
            if(JsonManager.isChunkScanDataExist(chunkPos)){
                String filePath = "r." + chunkPos.getRegionX() + "." + chunkPos.getRegionZ()
                        + "/" + chunkPos.x + "." + chunkPos.z + ".json";
                ScanDataOfChunk chunkData = JsonManager.readJson(filePath);
                GlobalVariables.scanDataMap.put(chunkPos, chunkData);
                return chunkData;
            }else{
                throw new ChunkDataNotFoundExeption();
            }
        }
    }
}
