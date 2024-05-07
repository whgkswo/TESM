package net.whgkswo.tesm.pathfindingv2;

import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.entitymanaging.EntityManager;

public class LinearSearcher {
    private int cursorX;    private int cursorY;    private int cursorZ;
    private Direction direction;
    private final BlockPos refPos;  private final BlockPos endPos;

    public LinearSearcher(BlockPos refPos, BlockPos endPos, Direction direction){
        this.direction = direction;
        this.refPos = refPos;    this.endPos = endPos;
    }
    public SearchResult linearSearch(ServerWorld world, int maxReapetCount){
        cursorX = refPos.getX();
        cursorY = refPos.getY();
        cursorZ = refPos.getZ();
        // 루프 돌며 일직선으로 진행
        for(int i = 0; i< maxReapetCount; i++){
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

            // 목적지에 도착했으면 길찾기 종료
            BlockPos nextPos = new BlockPos(cursorX, cursorY, cursorZ);
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
                    rightBlocked = false;
                }
                if(leftBlocked || rightBlocked){
                    JumpPoint jumpPoint = new JumpPoint(nextPos, endPos,0,leftBlocked,rightBlocked);
                    return new SearchResult(false, jumpPoint);
                }
            }
            // 이동한 위치에 벌 소환
            EntityManager.summonEntity(world, EntityType.BEE, nextPos);
            // 대각선 방향일 때 양옆으로 추가 탐색
            if(direction.isDiagonal()){
                SearchResult result;
                LinearSearcher xSearcher = new LinearSearcher(nextPos, endPos, Direction.getDirectionByComponent(direction.getX(), 0));
                result = xSearcher.linearSearch(world, maxReapetCount-i-1);
                if(result.hasFoundDestination()){
                    return result;
                }else{
                    LinearSearcher zSearcher = new LinearSearcher(nextPos, endPos, Direction.getDirectionByComponent(0, direction.getZ()));
                    result = zSearcher.linearSearch(world, maxReapetCount-i-1);
                    if(result.hasFoundDestination()){
                        return result;
                    }
                }
            }
        }
        // 정상적으로 탐색을 마쳤으면 결과 리턴
        return new SearchResult(false, null);
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
