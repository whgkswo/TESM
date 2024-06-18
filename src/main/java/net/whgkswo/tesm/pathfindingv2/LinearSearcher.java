package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.entitymanaging.EntityManager;

import java.util.ArrayList;
import java.util.HashMap;

public class LinearSearcher {
    private int cursorX;    private int cursorY;    private int cursorZ;
    private Direction direction;
    private final BlockPos refPos;  private final BlockPos endPos;
    private final int maxRepeatCount;

    public LinearSearcher(BlockPos refPos, BlockPos endPos, Direction direction, int maxRepeatCount){
        this.direction = direction;
        this.refPos = refPos;    this.endPos = endPos;
        this.maxRepeatCount = maxRepeatCount;
    }
    public SearchResult linearSearch(ServerWorld world, BlockPos largeRefPos, ArrayList<JumpPoint> openList, HashMap<BlockPos,SearchResult> closedList,
                                     DiagSearchState diagSearchState, int trailedDistance){
        cursorX = refPos.getX();
        cursorY = refPos.getY();
        cursorZ = refPos.getZ();
        // 소탐색 시작 지점에 닭 소환
        /*EntityManager.summonEntity(world,EntityType.CHICKEN, refPos);*/
        // 루프 돌며 일직선으로 진행
        boolean metObstacle = false;
        for(int i = 1; i<= maxRepeatCount; i++){
            BlockPos prevPos;
            // 다음 칸에 장애물과 낭떠러지가 있으면
            if(!BlockStateTester.isReachable(world, new BlockPos(cursorX, cursorY, cursorZ), direction)){
                return new SearchResult(false, null);
            }else{ // 장애물과 낭떠러지가 없으면
                prevPos = new BlockPos(cursorX, cursorY, cursorZ);
                // 탐색 방향으로 한 칸 가기
                BlockPos nextPos = moveOneBlock(world, prevPos, direction);
                cursorX = nextPos.getX();   cursorY = nextPos.getY();   cursorZ = nextPos.getZ();
            }
            // 이동한 좌표 업데이트
            BlockPos nextPos = new BlockPos(cursorX, cursorY, cursorZ);
            /*world.getPlayers().forEach(player -> {
                player.sendMessage(Text.literal("좌표 업데이트 (" + nextPos.getX() + ", " + nextPos.getY() + ", " + nextPos.getZ() + ")"));
            });*/
            // 목적지에 도착했으면 길찾기 종료
            if(nextPos.equals(endPos)){
                return new SearchResult(true, null);
            }
            // 직선 탐색일 때만 점프 포인트 조건 검사
            if(!direction.isDiagonal()){
                boolean leftBlocked = false;    boolean rightBlocked = false;
                // x방향 추가탐색이고 z방향이 막혔었다면 점프 포인트 생성
                 if(direction.getX() != 0 && diagSearchState.iszBlocked()){
                    // z방향
                    Direction targetDirection = Direction.getDirectionByComponent(0,diagSearchState.getDirection().getZ());
                    // x방향에 대해 z방향의 상대방향 구하기
                    RelativeDirection blockedDirection = direction.getRelativeDirection(targetDirection);
                    leftBlocked = (blockedDirection == RelativeDirection.LEFT);
                    rightBlocked = (blockedDirection == RelativeDirection.RIGHT);
                }
                if(direction.getZ() != 0 && diagSearchState.isxBlocked()){
                    // x방향
                    Direction targetDirection = Direction.getDirectionByComponent(diagSearchState.getDirection().getX(),0);
                    // z방향에 대해 x방향의 상대방향 구하기
                    RelativeDirection blockedDirection = direction.getRelativeDirection(targetDirection);
                    leftBlocked = (blockedDirection == RelativeDirection.LEFT);
                    rightBlocked = (blockedDirection == RelativeDirection.RIGHT);
                }
                TriangleTestResult leftTestResult = new TriangleTestResult(world, prevPos, direction, RelativeDirection.LEFT);
                TriangleTestResult rightTestResult = new TriangleTestResult(world, prevPos, direction, RelativeDirection.RIGHT);
                boolean finalTestLeft = BlockStateTester.isReachable(world, nextPos, direction.getLeftDirection());
                boolean finalTestRight = BlockStateTester.isReachable(world, nextPos, direction.getRightDirection());
                /*boolean leftBlocked = false;    boolean rightBlocked = false;*/
                if(!leftBlocked && JumpPointTester.jumpPointTest(leftTestResult, finalTestLeft)){
                    leftBlocked = true;
                }
                if(!rightBlocked && JumpPointTester.jumpPointTest(rightTestResult, finalTestRight)){
                    rightBlocked = true;
                }
                // 점프 포인트 생성 기본 조건을 만족했을 때
                if(leftBlocked || rightBlocked){
                    // 점프 포인트 생성
                    int nextTrailedDistance = trailedDistance + diagSearchState.getSearchedCount()*10 + i*10;
                        /*world.getPlayers().forEach(player -> {
                            player.sendMessage(Text.literal("점프 포인트 생성 (" + nextPos.getX() + ", " + nextPos.getY() + ", " + nextPos.getZ() + ")"));
                        });*/
                    JumpPoint jumpPoint = createAndRegisterJumpPoint(largeRefPos,nextPos, direction, nextTrailedDistance, endPos,leftBlocked,rightBlocked,openList,closedList,world);
                    // 결과 리턴
                    return new SearchResult(false, jumpPoint);
                }
            }
            // 이동한 위치에 벌 소환
            /*EntityManager.summonEntity(world, EntityType.BEE, nextPos);*/
            // 대각선 방향일 때 양옆으로 추가 탐색
            int branchLength = maxRepeatCount-i;
            if(direction.isDiagonal() && maxRepeatCount-i > 0){
                // 한 칸 앞의 y좌표를 검사
                BlockPos nextTempPos = moveOneBlock(world, nextPos, direction);
                int dy = nextTempPos.getY() - nextPos.getY();
                // 올라가는 칸의 경우 검사 기준 좌표를 한 칸 올려 줘야 함
                int offset = (dy == 1) ? 2:1;

                BlockPos xRefPos = new BlockPos(nextPos.getX() + direction.getX(), nextPos.getY(), nextPos.getZ()).up(offset);
                BlockPos zRefPos = new BlockPos(nextPos.getX(), nextPos.getY(), nextPos.getZ() + direction.getZ()).up(offset);
                boolean xBlocked = BlockStateTester.isSolid(world, xRefPos) || BlockStateTester.isSolid(world, xRefPos.up(1));
                boolean zBlocked = BlockStateTester.isSolid(world, zRefPos) || BlockStateTester.isSolid(world, zRefPos.up(1));

                DiagSearchState currentDiagSearchState = new DiagSearchState(i, direction,xBlocked, zBlocked);

                // x방향 탐색
                LinearSearcher xSearcher = new LinearSearcher(nextPos, endPos, Direction.getDirectionByComponent(direction.getX(), 0), branchLength);
                SearchResult xBranchResult = xSearcher.linearSearch(world,largeRefPos,openList,closedList,currentDiagSearchState,trailedDistance);
                if(xBranchResult.hasFoundDestination()){
                    return xBranchResult;
                }else { // 목적지를 찾지 못했다면
                    LinearSearcher zSearcher = new LinearSearcher(nextPos, endPos, Direction.getDirectionByComponent(0, direction.getZ()),branchLength);
                    // z방향 탐색
                    SearchResult zBranchResult = zSearcher.linearSearch(world,largeRefPos,openList,closedList,currentDiagSearchState,trailedDistance);
                    if(zBranchResult.hasFoundDestination()){
                        return zBranchResult;
                    }
                }
                if(xBlocked || zBlocked){
                    return new SearchResult(false, null);
                }
            }
        }
        // 정상적으로 탐색을 마쳤으면 점프 포인트 추가
        BlockPos resultPos = new BlockPos(cursorX,cursorY,cursorZ);
        /*world.getPlayers().forEach(player -> {
            player.sendMessage(Text.literal("탐색 종료 포인트 생성 (" + resultPos.getX() + ", " + resultPos.getY() + ", " + resultPos.getZ() + ")"));
        });*/
        int nextTrailDistance = trailedDistance + diagSearchState.getSearchedCount()*10 + maxRepeatCount*10;
        createAndRegisterJumpPoint(largeRefPos,resultPos, direction,nextTrailDistance,endPos,false,false,openList,closedList,world);
        // 결과 반환
        return new SearchResult(false, null);
    }
    public static boolean isValidCoordForJumpPoint(BlockPos targetPos, ArrayList<JumpPoint> openList, HashMap<BlockPos, SearchResult> closedList){
        boolean duplicatedInOL = false;
        for (JumpPoint jumpPoint : openList) {
            BlockPos jpPos = jumpPoint.getBlockPos();
            if (jpPos.getX() == targetPos.getX() && jpPos.getY() == targetPos.getY() && jpPos.getZ() == targetPos.getZ()) {
                duplicatedInOL = true;
                break;
            }
        }
        boolean duplicatedInCL = false;
        for(BlockPos blockPos :closedList.keySet()){
            if(targetPos.getX() == blockPos.getX() && targetPos.getY() == blockPos.getY() && targetPos.getZ() == blockPos.getZ()){
                duplicatedInCL = true;
                break;
            }
        }
        return !duplicatedInOL && !duplicatedInCL;
    }
    public static JumpPoint createAndRegisterJumpPoint(BlockPos largeRefPos,BlockPos currentPos, Direction direction, int trailedDistance,BlockPos endPos, boolean leftBlocked, boolean rightBlocked,
                                                       ArrayList<JumpPoint> openList, HashMap<BlockPos, SearchResult> closedList, ServerWorld world){
        // 점프 포인트 생성
        JumpPoint jumpPoint = new JumpPoint(largeRefPos,currentPos, direction,endPos,trailedDistance,leftBlocked,rightBlocked);
        if(isValidCoordForJumpPoint(currentPos, openList, closedList)){
            // 갑옷 거치대 소환
            EntityManager.summonEntity(world, EntityType.ARMOR_STAND, currentPos);
            // 오픈 리스트에 점프 포인트 추가
            openList.add(jumpPoint);
        }
        return jumpPoint;
    }
    public static BlockPos moveOneBlock(ServerWorld world, BlockPos refPos, Direction direction){
        // 탐색 방향으로 한 칸 가기
        int cursorX = refPos.getX() + direction.getX();
        int cursorY = refPos.getY();
        int cursorZ = refPos.getZ() + direction.getZ();

        BlockPos nextPos = new BlockPos(cursorX, cursorY, cursorZ);
        if(BlockStateTester.isSolid(world, nextPos.up(1))){
            // 올라가기
            cursorY++;
        }else if (!BlockStateTester.isSolid(world, nextPos)){
            // 내려가기
            cursorY--;
        }
        return new BlockPos(cursorX, cursorY, cursorZ);
    }
}
