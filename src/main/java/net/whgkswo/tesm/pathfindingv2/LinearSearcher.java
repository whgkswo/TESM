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

    public LinearSearcher(BlockPos refPos, BlockPos endPos, Direction direction){
        this.direction = direction;
        this.refPos = refPos;    this.endPos = endPos;
    }
    public SearchResult linearSearch(ServerWorld world, ArrayList<JumpPoint> openList, HashMap<BlockPos,SearchResult> closedList,
                                     int maxReapetCount, int trailedDistance, int diagonalCount){
        cursorX = refPos.getX();
        cursorY = refPos.getY();
        cursorZ = refPos.getZ();
        // 시작 지점에 닭 소환
        EntityManager.summonEntity(world,EntityType.CHICKEN, refPos);
        // 루프 돌며 일직선으로 진행
        for(int i = 1; i<= maxReapetCount; i++){
            BlockPos prevPos;
            // 다음 칸에 장애물과 낭떠러지가 있으면
            if(!BlockStateTester.isReachable(world, new BlockPos(cursorX, cursorY, cursorZ), direction)){
                break;
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
                TriangleTest leftTestResult = new TriangleTest(world, prevPos, direction, TestDirection.LEFT);
                TriangleTest rightTestResult = new TriangleTest(world, prevPos, direction, TestDirection.RIGHT);
                boolean finalTestLeft = BlockStateTester.isReachable(world, nextPos, direction.getLeftDirection());
                boolean finalTestRight = BlockStateTester.isReachable(world, nextPos, direction.getRightDirection());
                boolean leftBlocked = false;    boolean rightBlocked = false;
                if(JumpPointTester.jumpPointTest(leftTestResult, finalTestLeft)){
                    leftBlocked = true;
                }
                if(JumpPointTester.jumpPointTest(rightTestResult, finalTestRight)){
                    rightBlocked = true;
                }
                // 점프 포인트 생성 기본 조건을 만족했을 때
                if(leftBlocked || rightBlocked){
                    // 점프 포인트 생성
                    trailedDistance += diagonalCount*10 + i*10;
                        /*world.getPlayers().forEach(player -> {
                            player.sendMessage(Text.literal("점프 포인트 생성 (" + nextPos.getX() + ", " + nextPos.getY() + ", " + nextPos.getZ() + ")"));
                        });*/
                    JumpPoint jumpPoint = createAndRegisterJumpPoint(nextPos,direction, trailedDistance, endPos,leftBlocked,rightBlocked,openList,closedList,world);
                    // 결과 리턴
                    return new SearchResult(false, jumpPoint);
                }
            }
            // 이동한 위치에 벌 소환
            /*EntityManager.summonEntity(world, EntityType.BEE, nextPos);*/
            // 대각선 방향일 때 양옆으로 추가 탐색
            if(direction.isDiagonal() && maxReapetCount-i-1 > 0){
                SearchResult result;
                LinearSearcher xSearcher = new LinearSearcher(nextPos, endPos, Direction.getDirectionByComponent(direction.getX(), 0));
                // x방향 탐색
                result = xSearcher.linearSearch(world,openList,closedList,maxReapetCount-i-1,trailedDistance,i);
                if(result.hasFoundDestination()){
                    return result;
                }else{ // 목적지를 찾지 못했다면
                    LinearSearcher zSearcher = new LinearSearcher(nextPos, endPos, Direction.getDirectionByComponent(0, direction.getZ()));
                    // z방향 탐색
                    result = zSearcher.linearSearch(world,openList,closedList,maxReapetCount-i-1,trailedDistance,i);
                    if(result.hasFoundDestination()){
                        return result;
                    }
                }
            }
        }
        // 정상적으로 탐색을 마쳤으면
        // 점프 포인트 추가
        BlockPos resultPos = new BlockPos(cursorX,cursorY,cursorZ);
        /*world.getPlayers().forEach(player -> {
            player.sendMessage(Text.literal("탐색 종료 포인트 생성 (" + resultPos.getX() + ", " + resultPos.getY() + ", " + resultPos.getZ() + ")"));
        });*/
        createAndRegisterJumpPoint(resultPos, direction,trailedDistance,endPos,false,false,openList,closedList,world);
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
    public static JumpPoint createAndRegisterJumpPoint(BlockPos refPos, Direction direction, int trailedDistance,BlockPos endPos, boolean leftBlocked, boolean rightBlocked,
                                                       ArrayList<JumpPoint> openList, HashMap<BlockPos, SearchResult> closedList, ServerWorld world){
        // 점프 포인트 생성
        JumpPoint jumpPoint = new JumpPoint(refPos, direction,endPos,trailedDistance,leftBlocked,rightBlocked);
        if(isValidCoordForJumpPoint(refPos, openList, closedList)){
            // 갑옷 거치대 소환
            /*EntityManager.summonEntity(world, EntityType.ARMOR_STAND, refPos);*/
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
