package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.general.GlobalVariables;

import java.util.ArrayList;
import java.util.HashMap;

public class LargeSearcher {
    private BlockPos endPos;
    private final int MAX_SEARCH_RADIUS;

    private ArrayList<JumpPoint> openList;

    private HashMap<BlockPos, SearchResult> closedList;
    public LargeSearcher(BlockPos endPos, int MAX_SEARCH_RADIUS, ArrayList<JumpPoint> openList, HashMap<BlockPos, SearchResult> closedList) {
        this.endPos = endPos;
        this.MAX_SEARCH_RADIUS = MAX_SEARCH_RADIUS;
        this.openList = openList;
        this.closedList = closedList;
    }

    public SearchResult largeSearch(int searchCount, BlockPos startPos){
        searchCount++;
        int searchCount2 = searchCount;
        // 다음 점프 포인트 선정 (F값이 최소인 걸로)
        int nextIndex = OpenListManager.getMinFIndex(openList);
        JumpPoint nextJumpPoint = openList.get(nextIndex);
        BlockPos refPos = nextJumpPoint.getBlockPos();
        // 소탐색 방향 선정
        ArrayList<Direction> directions = DirectionSetter.setSearchDirections(startPos, nextJumpPoint);
        // 대탐색 정보 채팅창에 출력
        GlobalVariables.player.sendMessage(Text.of(String.format("(%d) (%d, %d, %d)를 기준으로 탐색, %s", searchCount2,
                refPos.getX(), refPos.getY(), refPos.getZ(), directions)));

        SearchResult result = new SearchResult(false, null);
        // 대탐색 시작 위치에 닭 소환
        /*EntityManager.summonEntity(EntityType.CHICKEN, refPos);*/
        // 다음 방향들에 대해 소탐색 시작
        for(Direction direction : directions){
            // 이전 소탐색에서 사용된 갑옷 거치대와 알레이, 닭, 벌 없애기
            /*EntityManager.killEntities(EntityType.ARMOR_STAND, EntityType.CHICKEN, EntityType.BEE);*/
            // 탐색 실시
            BlockPos largeRefPos = new BlockPos(refPos.getX(), refPos.getY(), refPos.getZ()); // 얕은 복사 방지
            result = search(largeRefPos, direction, nextJumpPoint.getHValue());
            // 목적지를 찾았으면 메서드 종료
            if(result.hasFoundDestination()){
                return result;
            }
        }
        // 해당 좌표를 클로즈 리스트에 추가한 후 오픈 리스트에서 제거
        BlockPos tempPos = new BlockPos(refPos.getX(), refPos.getY(), refPos.getZ()); // 얕은 복사 방지
        closedList.put(tempPos, result);
        openList.remove(nextIndex);
        return new SearchResult(false, null);
    }
    public SearchResult search(BlockPos largeRefPos, Direction direction, int hValue){
        LinearSearcher searcher = new LinearSearcher(largeRefPos, endPos, direction, MAX_SEARCH_RADIUS);
        // 주어진 방향에 대해 탐색 실행
        DiagSearchState diagSearchState = new DiagSearchState(0,direction,false,false);
        SearchResult result = searcher.linearSearch(largeRefPos,openList,closedList,diagSearchState,hValue);
        return result;
    }
}
