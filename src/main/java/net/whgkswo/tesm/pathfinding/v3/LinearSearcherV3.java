package net.whgkswo.tesm.pathfinding.v3;

import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.pathfinding.v2.*;
import net.whgkswo.tesm.util.BlockPosUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

import static net.whgkswo.tesm.util.BlockPosUtil.getNextBlock;

public class LinearSearcherV3 {
    private int cursorX;    private int cursorY;    private int cursorZ;
    private PathfindDirection direction;
    private final BlockPos refPos;  private final BlockPos endPos;
    private final int maxRepeatCount;
    private final ScanDataOfBlockPos scanData;
    private HashSet<BlockPos> openSet;

    public int getMaxRepeatCount() {
        return maxRepeatCount;
    }

    public LinearSearcherV3(BlockPos refPos, BlockPos endPos, PathfindDirection direction, int maxRepeatCount, ScanDataOfBlockPos scanData,
                            HashSet<BlockPos> openSet){
        this.direction = direction;
        this.refPos = BlockPosUtil.getCopyPos(refPos);    this.endPos = endPos;
        this.maxRepeatCount = maxRepeatCount;
        this.scanData = scanData;
        this.openSet = openSet;
    }
    public SearchResult linearSearch(BlockPos largeRefPos, PriorityQueue<JumpPoint> openList, HashMap<BlockPos,BlockPos> closedList,
                                     DiagSearchState diagSearchState, int trailedDistance){
        cursorX = refPos.getX();
        cursorY = refPos.getY();
        cursorZ = refPos.getZ();
        // 소탐색 시작 지점에 닭 소환
        /*EntityManager.summonEntity(EntityType.CHICKEN, refPos);*/
        ScanDataOfDirection directionData = scanData.getDirectionData(direction);
        // 루프 돌며 일직선으로 진행
        for(int i = 1; i<= maxRepeatCount; i++){
            BlockPos prevPos;
            prevPos = new BlockPos(cursorX,cursorY,cursorZ);
            BlockPos nextPos;

            // 장애물 검사
            if(directionData.isObstacleFound()){
                return new SearchResult(false, null);
            }
            nextPos = getNextBlock(prevPos,direction);
            cursorX = nextPos.getX();   cursorY = nextPos.getY(); cursorZ = nextPos.getZ();
            // 목적지에 도착했으면 길찾기 종료
            if(nextPos.equals(endPos)){
                return new SearchResult(true, null);
            }
            // 점프 포인트 생성 기본 조건을 만족했을 때
            if(directionData.getLeftBlocked() || directionData.getRightBlocked()){
                // 점프 포인트 생성
                /*player.sendMessage(Text.literal("점프 포인트 생성 (" + nextPos.getX() + ", " + nextPos.getY() + ", " + nextPos.getZ() + ")"));*/
                JumpPoint jumpPoint = createAndRegisterJumpPoint(largeRefPos,nextPos, direction, trailedDistance,
                        diagSearchState, endPos,directionData.getLeftBlocked(),directionData.getRightBlocked(),openList, openSet,closedList);
                // 결과 리턴
                return new SearchResult(false, jumpPoint);
            }
            // 이동한 위치에 벌 소환
            /*EntityManager.summonEntity(EntityType.BEE, nextPos);*/
            // 대각선 방향일 때 양옆으로 추가 탐색
            int branchLength = maxRepeatCount-i;
            if(direction.isDiagonal() && maxRepeatCount-i > 0){
                DiagSearchState currentDiagSearchState = new DiagSearchState(i, direction);
                // x방향 탐색
                PathfindDirection xDirection = PathfindDirection.getDirectionByComponent(direction.getX(), 0);
                LinearSearcherV3 xSearcher = new LinearSearcherV3(nextPos, endPos, xDirection, branchLength, scanData, openSet);
                SearchResult xBranchResult = xSearcher.linearSearch(largeRefPos,openList, closedList,currentDiagSearchState,trailedDistance);
                if(xBranchResult.hasFoundDestination()){
                    return xBranchResult;
                }else { // 목적지를 찾지 못했다면
                    LinearSearcherV3 zSearcher = new LinearSearcherV3(nextPos, endPos, PathfindDirection.getDirectionByComponent(0, direction.getZ()),branchLength, scanData, openSet);
                    // z방향 탐색
                    SearchResult zBranchResult = zSearcher.linearSearch(largeRefPos,openList, closedList,currentDiagSearchState,trailedDistance);
                    if(zBranchResult.hasFoundDestination()){
                        return zBranchResult;
                    }
                }
            }
        }
        // 정상적으로 탐색을 마쳤으면 점프 포인트 추가
        BlockPos resultPos = new BlockPos(cursorX,cursorY,cursorZ);
        /*player.sendMessage(Text.literal("탐색 종료 포인트 생성 (" + resultPos.getX() + ", " + resultPos.getY() + ", " + resultPos.getZ() + ")"));*/

        createAndRegisterJumpPoint(largeRefPos,resultPos, direction,trailedDistance,diagSearchState,endPos,false,false,openList,openSet,closedList);
        // 결과 반환
        return new SearchResult(false, null);
    }

    private static boolean isValidCoordForJumpPoint(BlockPos targetPos, HashSet<BlockPos> openSet, HashMap<BlockPos, BlockPos> closedList){
        return !openSet.contains(targetPos) && !closedList.containsKey(targetPos);
    }
    private JumpPoint createAndRegisterJumpPoint(BlockPos largeRefPos, BlockPos currentPos, PathfindDirection direction, int trailedDistance, DiagSearchState diagSearchState, BlockPos endPos, boolean leftBlocked, boolean rightBlocked,
                                                 PriorityQueue<JumpPoint> openList, HashSet<BlockPos> openSet, HashMap<BlockPos, BlockPos> closedList){

        int nextTrailedDistance = trailedDistance + diagSearchState.getSearchedCount()*14 + this.getMaxRepeatCount()*10;
        // 점프 포인트 생성
        JumpPoint jumpPoint = new JumpPoint(largeRefPos,currentPos, direction,endPos,nextTrailedDistance,leftBlocked,rightBlocked);
        if(isValidCoordForJumpPoint(currentPos, openSet, closedList)){
            // 갑옷 거치대 소환
            /*EntityManager.summonEntity(EntityType.ARMOR_STAND, currentPos);*/
            // 오픈 리스트에 점프 포인트 추가
            openList.add(jumpPoint);
            openSet.add(jumpPoint.getBlockPos());
        }
        return jumpPoint;
    }
}