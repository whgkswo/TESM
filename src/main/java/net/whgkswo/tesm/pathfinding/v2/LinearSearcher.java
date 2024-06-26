package net.whgkswo.tesm.pathfinding.v2;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;

public class LinearSearcher {
    private int cursorX;    private int cursorY;    private int cursorZ;
    private Direction direction;
    private final BlockPos refPos;  private final BlockPos endPos;
    private final int maxRepeatCount;

    public int getMaxRepeatCount() {
        return maxRepeatCount;
    }

    public LinearSearcher(BlockPos refPos, BlockPos endPos, Direction direction, int maxRepeatCount){
        this.direction = direction;
        this.refPos = BlockPosManager.getCopyPos(refPos);    this.endPos = endPos;
        this.maxRepeatCount = maxRepeatCount;
    }
    public SearchResult linearSearch(BlockPos largeRefPos, ArrayList<JumpPoint> openList, HashMap<BlockPos,BlockPos> closedList,
                                     DiagSearchState diagSearchState, int trailedDistance){
        cursorX = refPos.getX();
        cursorY = refPos.getY();
        cursorZ = refPos.getZ();
        // 소탐색 시작 지점에 닭 소환
        /*EntityManager.summonEntity(EntityType.CHICKEN, refPos);*/
        // 루프 돌며 일직선으로 진행
        for(int i = 1; i<= maxRepeatCount; i++){
            BlockPos prevPos;
            // 다음 칸에 장애물과 낭떠러지가 있으면
            if(!BlockStateHelper.isReachable(new BlockPos(cursorX, cursorY, cursorZ), direction)){
                return new SearchResult(false, null);
            }else{ // 장애물과 낭떠러지가 없으면
                prevPos = new BlockPos(cursorX, cursorY, cursorZ);
                // 탐색 방향으로 한 칸 가기
                BlockPos nextPos = getNextBlock(prevPos, direction);
                cursorX = nextPos.getX();   cursorY = nextPos.getY();   cursorZ = nextPos.getZ();
            }
            // 이동한 좌표 업데이트
            BlockPos nextPos = new BlockPos(cursorX, cursorY, cursorZ);
            /*player.sendMessage(Text.literal("좌표 업데이트 (" + nextPos.getX() + ", " + nextPos.getY() + ", " + nextPos.getZ() + ")"));*/
            // 올라가는 좌표 장애물 추가 검사
            if(prevPos.getY() < nextPos.getY()){
                // 천장 머리쿵
                if(BlockStateHelper.isObstacle(prevPos.up(3))){
                    return new SearchResult(false,null);
                }
            }

            // 목적지에 도착했으면 길찾기 종료
            if(nextPos.equals(endPos)){
                return new SearchResult(true, null);
            }
            // 직선 탐색일 때만 점프 포인트 조건 검사
            boolean leftBlocked = false;    boolean rightBlocked = false;
            if(!direction.isDiagonal()){
                TriangleTestResult leftTestResult = new TriangleTestResult(prevPos, direction, RelativeDirection.LEFT);
                TriangleTestResult rightTestResult = new TriangleTestResult(prevPos, direction, RelativeDirection.RIGHT);
                boolean finalTestLeft = BlockStateHelper.isReachable(nextPos, direction.getLeftDirection());
                boolean finalTestRight = BlockStateHelper.isReachable(nextPos, direction.getRightDirection());

                if(JumpPointTester.jumpPointTest(leftTestResult, finalTestLeft)){
                    leftBlocked = true;
                }
                if(JumpPointTester.jumpPointTest(rightTestResult, finalTestRight)){
                    rightBlocked = true;
                }
            }else{// 대각선일 때도 점프 포인트를 생성할 수 있음!!
                TriangleTestForDiag leftTest = new TriangleTestForDiag(prevPos, nextPos, direction, RelativeDirection.LEFT);
                TriangleTestForDiag rightTest = new TriangleTestForDiag(prevPos, nextPos, direction, RelativeDirection.RIGHT);
                if(!leftTest.isRefPosToNeighborPos() && leftTest.isNextPosToNeighborPos()){
                    leftBlocked = true;
                }
                if(!rightTest.isRefPosToNeighborPos() && rightTest.isNextPosToNeighborPos()){
                    rightBlocked = true;
                }
            }
            // 점프 포인트 생성 기본 조건을 만족했을 때
            if(leftBlocked || rightBlocked){
                // 점프 포인트 생성
                /*player.sendMessage(Text.literal("점프 포인트 생성 (" + nextPos.getX() + ", " + nextPos.getY() + ", " + nextPos.getZ() + ")"));*/
                JumpPoint jumpPoint = createAndRegisterJumpPoint(largeRefPos,nextPos, direction, trailedDistance,diagSearchState, endPos,leftBlocked,rightBlocked,openList, closedList);
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
                LinearSearcher xSearcher = new LinearSearcher(nextPos, endPos, Direction.getDirectionByComponent(direction.getX(), 0), branchLength);
                SearchResult xBranchResult = xSearcher.linearSearch(largeRefPos,openList, closedList,currentDiagSearchState,trailedDistance);
                if(xBranchResult.hasFoundDestination()){
                    return xBranchResult;
                }else { // 목적지를 찾지 못했다면
                    LinearSearcher zSearcher = new LinearSearcher(nextPos, endPos, Direction.getDirectionByComponent(0, direction.getZ()),branchLength);
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
    public static boolean isValidCoordForJumpPoint(BlockPos targetPos, ArrayList<JumpPoint> openList, HashMap<BlockPos, BlockPos> closedList){
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
    public JumpPoint createAndRegisterJumpPoint(BlockPos largeRefPos, BlockPos currentPos, Direction direction, int trailedDistance,DiagSearchState diagSearchState, BlockPos endPos, boolean leftBlocked, boolean rightBlocked,
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
    public static BlockPos getNextBlock(BlockPos refPos, Direction direction){
        // 탐색 방향으로 한 칸 가기
        int cursorX = refPos.getX() + direction.getX();
        int cursorY = refPos.getY();
        int cursorZ = refPos.getZ() + direction.getZ();

        BlockPos nextPos = new BlockPos(cursorX, cursorY, cursorZ);
        if(BlockStateHelper.isSolid(nextPos.up(1))){
            // 올라가기
            cursorY++;
        }else if (!BlockStateHelper.isSolid(nextPos)){
            // 내려가기
            cursorY--;
        }
        return new BlockPos(cursorX, cursorY, cursorZ);
    }
}