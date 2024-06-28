package net.whgkswo.tesm.pathfinding.v3;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.whgkswo.tesm.data.JsonManager;
import net.whgkswo.tesm.exceptions.ChunkDataNotFoundExeption;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.pathfinding.v2.*;
import net.whgkswo.tesm.util.BlockPosUtil;
import net.whgkswo.tesm.util.JumpPointTester;

import java.util.ArrayList;
import java.util.HashMap;

import static net.whgkswo.tesm.general.GlobalVariables.scanDataMap;
import static net.whgkswo.tesm.general.GlobalVariables.world;
import static net.whgkswo.tesm.util.BlockPosUtil.getNextBlock;

public class LinearSearcherV3 {
    private int cursorX;    private int cursorY;    private int cursorZ;
    private Direction direction;
    private final BlockPos refPos;  private final BlockPos endPos;
    private final int maxRepeatCount;
    private final ScanDataOfBlockPos scanData;

    public int getMaxRepeatCount() {
        return maxRepeatCount;
    }

    public LinearSearcherV3(BlockPos refPos, BlockPos endPos, Direction direction, int maxRepeatCount, ScanDataOfBlockPos scanData){
        this.direction = direction;
        this.refPos = BlockPosUtil.getCopyPos(refPos);    this.endPos = endPos;
        this.maxRepeatCount = maxRepeatCount;
        this.scanData = scanData;
    }
    public SearchResult linearSearch(BlockPos largeRefPos, ArrayList<JumpPoint> openList, HashMap<BlockPos,BlockPos> closedList,
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
                        diagSearchState, endPos,directionData.getLeftBlocked(),directionData.getRightBlocked(),openList, closedList);
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
                Direction xDirection = Direction.getDirectionByComponent(direction.getX(), 0);
                LinearSearcherV3 xSearcher = new LinearSearcherV3(nextPos, endPos, xDirection, branchLength, scanData);
                SearchResult xBranchResult = xSearcher.linearSearch(largeRefPos,openList, closedList,currentDiagSearchState,trailedDistance);
                if(xBranchResult.hasFoundDestination()){
                    return xBranchResult;
                }else { // 목적지를 찾지 못했다면
                    LinearSearcherV3 zSearcher = new LinearSearcherV3(nextPos, endPos, Direction.getDirectionByComponent(0, direction.getZ()),branchLength, scanData);
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

        createAndRegisterJumpPoint(largeRefPos,resultPos, direction,trailedDistance,diagSearchState,endPos,false,false,openList, closedList);
        // 결과 반환
        return new SearchResult(false, null);
    }

    public static JumpPointTestResult jumpPointTest(BlockPos blockPos, BlockPos nextPos, Direction direction){
        boolean leftBlocked = false;    boolean rightBlocked = false;
        if(!direction.isDiagonal()){
            TriangleTestResult leftTestResult = new TriangleTestResult(blockPos, direction, RelativeDirection.LEFT);
            TriangleTestResult rightTestResult = new TriangleTestResult(blockPos, direction, RelativeDirection.RIGHT);
            boolean finalTestLeft = BlockPosUtil.isReachable(nextPos, direction.getLeftDirection());
            boolean finalTestRight = BlockPosUtil.isReachable(nextPos, direction.getRightDirection());

            if(JumpPointTester.jumpPointTest(leftTestResult, finalTestLeft)){
                leftBlocked = true;
            }
            if(JumpPointTester.jumpPointTest(rightTestResult, finalTestRight)){
                rightBlocked = true;
            }
        }else{// 대각선일 때도 점프 포인트를 생성할 수 있음!!
            TriangleTestForDiag leftTest = new TriangleTestForDiag(blockPos, nextPos, direction, RelativeDirection.LEFT);
            TriangleTestForDiag rightTest = new TriangleTestForDiag(blockPos, nextPos, direction, RelativeDirection.RIGHT);
            if(!leftTest.isRefPosToNeighborPos() && leftTest.isNextPosToNeighborPos()){
                leftBlocked = true;
            }
            if(!rightTest.isRefPosToNeighborPos() && rightTest.isNextPosToNeighborPos()){
                rightBlocked = true;
            }
        }
        return new JumpPointTestResult(leftBlocked, rightBlocked);
    }
    public static boolean isObstacleFound(BlockPos prevPos, Direction direction){
        // 다음 칸에 장애물과 낭떠러지가 있으면
        if(!BlockPosUtil.isReachable(prevPos, direction)){
            return true;
        }else{ // 장애물과 낭떠러지가 없으면
            // 탐색 방향으로 한 칸 가기
            BlockPos nextPos = getNextBlock(prevPos, direction);
            // 올라가는 좌표 장애물 추가 검사
            if(prevPos.getY() < nextPos.getY()){
                // 천장 머리쿵
                if(BlockPosUtil.isObstacle(prevPos.up(3))){
                    return true;
                }
            }
        }
        return false;
    }
    private static boolean isValidCoordForJumpPoint(BlockPos targetPos, ArrayList<JumpPoint> openList, HashMap<BlockPos, BlockPos> closedList){
        boolean duplicatedInOL = false;
        for (JumpPoint jumpPoint : openList) {
            BlockPos jpPos = jumpPoint.getBlockPos();
            if (jpPos.getX() == targetPos.getX() && jpPos.getY() == targetPos.getY() && jpPos.getZ() == targetPos.getZ()) {
                duplicatedInOL = true;
                break;
            }
        }
        boolean duplicatedInCL = false;
        for(BlockPos blockPos : closedList.keySet()){
            if(targetPos.getX() == blockPos.getX() && targetPos.getY() == blockPos.getY() && targetPos.getZ() == blockPos.getZ()){
                duplicatedInCL = true;
                break;
            }
        }
        return !duplicatedInOL && !duplicatedInCL;
    }
    private JumpPoint createAndRegisterJumpPoint(BlockPos largeRefPos, BlockPos currentPos, Direction direction, int trailedDistance,DiagSearchState diagSearchState, BlockPos endPos, boolean leftBlocked, boolean rightBlocked,
                                                ArrayList<JumpPoint> openList, HashMap<BlockPos, BlockPos> closedList){

        int nextTrailedDistance = trailedDistance + diagSearchState.getSearchedCount()*14 + this.getMaxRepeatCount()*10;
        // 점프 포인트 생성
        JumpPoint jumpPoint = new JumpPoint(largeRefPos,currentPos, direction,endPos,nextTrailedDistance,leftBlocked,rightBlocked);
        if(isValidCoordForJumpPoint(currentPos, openList, closedList)){
            // 갑옷 거치대 소환
            /*EntityManager.summonEntity(EntityType.ARMOR_STAND, currentPos);*/
            // 오픈 리스트에 점프 포인트 추가
            openList.add(jumpPoint);
        }
        return jumpPoint;
    }
}