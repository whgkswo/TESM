package net.whgkswo.tesm.pathfinding.v1;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.whgkswo.tesm.nbt.IEntityDataSaver;

import java.util.ArrayList;
import java.util.List;

public class PathFinder {
    static int startX; static int startY; static int startZ;    static BlockPos startBlockPos;
    static int endX;   static int endY;   static int endZ;  static BlockPos endBlockPos;
    static int cursorX; static int cursorY; static int cursorZ;
    static final int MAX_SEARCH_RADIUS = 15;
    static int MAX_SEARCH_REPEATCOUNT = 1500; // 1700언저리 넘어가면 서버 측에서 stack overflow 발생하는듯
    static final int REPEAT_DELAY = 0;
    static ArrayList<int[]> openList;
    static ArrayList<int[]> closedList;
    static ArrayList<Integer> pathIndexList = new ArrayList<>();
    static ServerWorld world2;
    static boolean pathFindingOn = false;
    static int currentSearchRepeatCount;
    static boolean serverTickOn;
    static int serverTickCount;
    static int straightCount;
    static int diagonalCount;
    static int trailedLength;
    static boolean diagonalBlockedL = false;
    static boolean diagonalBlockedR = false;
    static int yMoveCount_Diagonal;
    static int yMoveCount_Straight;
    static Entity targetEntity;
    static int movingRepeatCount;
    static ArrayList<int[]> movingList = new ArrayList<>();
    static boolean entityMovingOn = false;
    static int movingTimer = 0;
    static int movingDirectionX = 0;
    static int movingDirectionZ;
    static int movingStage = 0;
    static int movingCounter = 0;
    static final int MOVING_DELAY = 10; // 한 칸 이동하는 데 걸리는 시간
    static final int MOVING_DIVIDER = 5; // 한 칸을 몇 번 나눠서 갈지 결정. DELAY/DIVIDER는 정수여야 함

    static double movingVecX;   static double movingVecZ;
    static int movingRefX;    static int movingRefZ;

    // TODO:
    //  f값 계산 시 y좌표 고려하기
    //  동굴 속 경로 못 찾는 문제 확인
    //  엔티티의 실제 이동 구현하기
    //  사전 연산 구현하기

    public static void pathFindingStart(ServerWorld world, String targetName, int destX, int destY, int destZ) {
        /*world.getPlayers().forEach(player -> {
            player.sendMessage(Text.literal("pathFindingStart 실행"));
        });*/
        world2 = world;
        pathFindingOn = true;
        currentSearchRepeatCount = 1;
        trailedLength = 0;
        resetCounters();
        /*List<AllayEntity> alleyList = world2.getEntitiesByType(EntityType.ALLAY,new Box(-1000,-64,-1000,1000,1024,1000), AllayEntity -> true);
        alleyList.forEach(AllayEntity::kill);
        List<BeeEntity> beeList = world2.getEntitiesByType(EntityType.BEE, new Box(-1000,-64,-1000,1000,1024,1000), BeeEntity -> true);
        beeList.forEach(BeeEntity::kill);
        List<ArmorStandEntity> armorStandList = world2.getEntitiesByType(EntityType.ARMOR_STAND,new Box(-1000,-64,-1000,1000,1024,1000), ArmorStandEntity -> true);
        armorStandList.forEach(ArmorStandEntity::kill);
        List<ChickenEntity> chickenList = world2.getEntitiesByType(EntityType.CHICKEN,new Box(-1000,-64,-1000,1000,1024,1000), ChickenEntity -> true);
        chickenList.forEach(ChickenEntity::kill);*/
        // 월드 내에서 targetName 이름을 가진 엔티티 가져오기 (entityList에 저장, 동명이인이 있는 경우가 아니면 하나만 저장됨)
        List<VillagerEntity> entityList = world2.getEntitiesByType(EntityType.VILLAGER, new Box(-10000, -64, -10000, 10000, 1024, 10000), entity -> {
            try{
                Text name = entity.getCustomName();
                return name != null && name.getString().equals(targetName);
            }catch (NullPointerException e){
                return false;
            }
        });
        if(entityList.isEmpty()){
            world2.getPlayers().forEach(player -> {
                player.sendMessage(Text.literal("엔티티를 찾을 수 없습니다."));
            });
            return;
        }
        // 시작점 설정 및 표시
        entityList.forEach(entity -> {
            targetEntity = entity;
            startX = targetEntity.getBlockX();    startY = targetEntity.getBlockY();  startZ = targetEntity.getBlockZ();
            startBlockPos = new BlockPos(startX, startY ,startZ);
        });
        // 목적지 설정 및 표시
        endX = destX;   endY = destY+1;   endZ = destZ;
        endBlockPos = new BlockPos(endX, endY, endZ);
        Entity entity = EntityType.ALLAY.spawn(world2, endBlockPos, SpawnReason.TRIGGERED);
        NbtCompound allayNbt = entity.writeNbt(new NbtCompound());
        allayNbt.putBoolean("NoAI",true);
        entity.readNbt(allayNbt);

        world2.spawnEntity(entity);

        // 오픈리스트와 클로즈리스트 초기화
        openList = new ArrayList<>();
        closedList = new ArrayList<>(); // addOpenList에서 closedList를 참조하므로 위치에 주의!!
        addOpenList(startBlockPos,0,0,0,0);
        // 초기 탐색 시작
        cursorX = startX;   cursorY = startY;   cursorZ = startZ;
        setSearchDirection();
    }
    static int getMinFOpenList(){
        if(!openList.isEmpty()){
            int fValue = openList.get(0)[3];
            int index = 0;
            for (int i = 0; i < openList.size(); i++){
                if(openList.get(i)[3] < fValue){
                    fValue = openList.get(i)[3];
                    index = i;
                }
            }
            // 오픈 리스트 디버그 메시지
            /*world2.getPlayers().forEach(player -> {
                StringBuilder tempOL = new StringBuilder("["+openList.get(0)[0]+", "+openList.get(0)[1]+", "+openList.get(0)[2]+"]");
                for(int i=1; i<openList.size(); i++){
                    tempOL.append(", ").append("[").append(openList.get(i)[0]).append(", ").append(openList.get(i)[1]).append(", ").append(openList.get(i)[2]).append("]");
                }
                player.sendMessage(Text.literal(String.valueOf(tempOL)));
            });*/
            closedList.add(openList.get(index));
            openList.remove(index);
            return index;
        }
        return -1; // 오픈 리스트가 동났음
    }
    static void setSearchDirection(){
        if(getMinFOpenList() == -1){
            pathFindingOn = false;
            world2.getPlayers().forEach(player -> {
                player.sendMessage(Text.literal("탐색 실패: 오픈 리스트가 비었습니다."));
            });
            return;
        }
        int[] lastClosedList = closedList.get(closedList.size()-1);
        // 탐색 시작점 표시 (닭 소환)
        /*Entity entity = EntityType.CHICKEN.spawn(world2, new BlockPos(lastClosedList[0],lastClosedList[1],lastClosedList[2]), SpawnReason.TRIGGERED);
        world2.spawnEntity(entity);
        NbtCompound entityData = entity.writeNbt(new NbtCompound());
        entityData.putBoolean("NoAI",true);
        entity.readNbt(entityData);*/

        int directionX = lastClosedList[4];
        int directionZ = lastClosedList[5];
        /*world2.getPlayers().forEach(player -> {
            player.sendMessage(Text.literal("("+ lastClosedList[0]+","+ lastClosedList[1]+","+ lastClosedList[2]+") 를 기준으로 탐색 ("+currentSearchRepeatCount+")"));
        });*/
        if(currentSearchRepeatCount==1){ // 초기 탐색
            int[][] directions = {{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}};
            trailedLength = 0;
            for (int[] directionPair : directions){
                if(pathFindingOn){
                    resetCounters();
                    search(directionPair[0], directionPair[1], startX,startY,startZ,0);
                }
            }
        }else{
            trailedLength = closedList.get(closedList.size()-1)[9];
            resetCounters();
            search(directionX,directionZ, lastClosedList[0],lastClosedList[1],lastClosedList[2],0);// 가던 방향
            int[] tempLR = getLRDirection(directionX,directionZ);
            if(closedList.get(closedList.size()-1)[6] == 1){ // 왼쪽이 막혔으면
                if(pathFindingOn){
                    resetCounters();
                    search(tempLR[0],tempLR[1], lastClosedList[0],lastClosedList[1],lastClosedList[2],0); // 왼쪽
                }
                if(pathFindingOn){
                    resetCounters();
                    search(tempLR[2],tempLR[3], lastClosedList[0],lastClosedList[1],lastClosedList[2],0); // 왼쪽 대각
                }
            }
            if(closedList.get(closedList.size()-1)[7] == 1){ // 오른쪽이 막혔으면
                if(pathFindingOn){
                    resetCounters();
                    search(tempLR[4],tempLR[5], lastClosedList[0],lastClosedList[1],lastClosedList[2],0); // 오른쪽
                }
                if(pathFindingOn){
                    resetCounters();
                    search(tempLR[6],tempLR[7], lastClosedList[0],lastClosedList[1],lastClosedList[2],0); // 오른쪽 대각
                }
            }
        }
        if(pathFindingOn){
            // 다음 탐색 반복
            if(currentSearchRepeatCount < MAX_SEARCH_REPEATCOUNT){
                diagonalBlockedL = false;
                diagonalBlockedR = false;
                if(REPEAT_DELAY > 0){ // 딜레이 있는 반복
                    serverTickCount = 0;
                    serverTickOn = true;
                }else{ // 딜레이 없는 반복
                    repeatSearch();
                }
            }else{ // 목적지를 찾지 못했다면
                pathFindingOn = false;
                world2.getPlayers().forEach(player -> {
                    player.sendMessage(Text.literal("탐색 실패: 너무 멀거나 갈 수 없는 곳입니다."));
                });
            }
        }
    }
    public static void onServerTicks(){
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            // 탐색 반복 로직
            if(serverTickOn){
                serverTickCount++;
                if(serverTickCount >= REPEAT_DELAY){
                    serverTickOn = false;
                    repeatSearch();
                }
            }
            // 엔티티 이동 로직
            if(entityMovingOn){
                movingTimer++;
                if(movingTimer >= MOVING_DELAY / MOVING_DIVIDER){
                    entityMoving();
                }
            }
        });
    }
    static void entityMoving(){
        movingTimer = 0;
        // 1블록 이동할 때마다 (MOVING_DIVIDE 횟수만큼 텔레포트했을 때)
        if(movingCounter % MOVING_DIVIDER == 0){
            // 텔레포트 기준점 설정
            movingRefX = targetEntity.getBlockX();    movingRefZ = targetEntity.getBlockZ();
            /*world2.getPlayers().forEach(player -> {
                player.sendMessage(Text.literal("텔레포트 기준점 설정"));
            });*/
            // 방향 세팅
            if(movingCounter == 0){ // movingStage가 바뀌었을 때
                movingDirectionX = movingList.get(movingStage)[0];
                movingDirectionZ = movingList.get(movingStage)[1];
                targetEntity.setYaw(getYaw());
                targetEntity.setHeadYaw(getYaw());
                /*world2.getPlayers().forEach(player -> {
                    player.sendMessage(Text.literal("setYaw (" + movingDirectionX + ", " + movingDirectionZ + ")"));
                });*/
                // 1회 텔레포트 벡터 설정
                movingVecX = (double) movingDirectionX / MOVING_DIVIDER;   movingVecZ = (double) movingDirectionZ / MOVING_DIVIDER;
            }
        }
        // 주어진 방향으로 이동
        // TODO: 포팅
        //targetEntity.teleport(movingRefX + (movingCounter%MOVING_DIVIDER+1)* movingVecX +0.5, targetEntity.getBlockY(), movingRefZ + (movingCounter%MOVING_DIVIDER+1)* movingVecZ +0.5);

        /*world2.getPlayers().forEach(player -> {
            player.sendMessage(Text.literal("이동 ("+movingCounter+")"));
        });*/
        // 올라가기
        if(world2.getBlockState(targetEntity.getBlockPos()).isSolidBlock(world2,targetEntity.getBlockPos())){
            // TODO: 포팅
            //targetEntity.teleport(targetEntity.getBlockX()+0.5,targetEntity.getBlockY()+1,targetEntity.getBlockZ()+0.5);
            // 내려가기
        }else if(!world2.getBlockState(targetEntity.getBlockPos().down(1)).isSolidBlock(world2, targetEntity.getBlockPos().down(1))){
            // TODO: 포팅
            //targetEntity.teleport(targetEntity.getBlockX()+0.5,targetEntity.getBlockY()-1,targetEntity.getBlockZ()+0.5);
        }
        movingCounter++;
        if(movingCounter >= movingList.get(movingStage)[2]*MOVING_DIVIDER){
            movingCounter = 0;
            add1MovingStage();
        }
    }
    static void add1MovingStage(){
        movingStage++;
        // 이동 끝내기
        if(movingStage >= movingList.size()){
            movingStage = 0;
            entityMovingOn = false;
            world2.getPlayers().forEach(player -> {
                player.sendMessage(Text.literal("목적지 도착"));
                List<AllayEntity> alleyList = world2.getEntitiesByType(EntityType.ALLAY,new Box(-1000,-64,-1000,1000,1024,1000), AllayEntity -> true);
                alleyList.forEach(allay -> allay.kill(world2));
                List<ArmorStandEntity> armorStandList = world2.getEntitiesByType(EntityType.ARMOR_STAND,new Box(-1000,-64,-1000,1000,1024,1000), ArmorStandEntity -> true);
                armorStandList.forEach(armorStand -> armorStand.kill(world2));
            });
        }
    }
    static int getYaw(){
        if(Math.abs(movingDirectionX) == 1){
            switch (movingDirectionZ){
                case 1:
                    return (-movingDirectionX)*45;
                case 0:
                    return (-movingDirectionX)*90;
                case -1:
                    return (-movingDirectionX)*135;
            }
        }else{
            if(movingDirectionZ == 1){
                return 0;
            }else{
                return 180;
            }
        }
        return 0;
    }
    static void repeatSearch(){
        /*List<BeeEntity> beeList = world2.getEntitiesByType(EntityType.BEE, new Box(-1000,-64,-1000,1000,1024,1000), BeeEntity -> true);
        beeList.forEach(BeeEntity::kill);
        List<ArmorStandEntity> armorStandList = world2.getEntitiesByType(EntityType.ARMOR_STAND,new Box(-1000,-64,-1000,1000,1024,1000), ArmorStandEntity -> true);
        armorStandList.forEach(ArmorStandEntity::kill);
        List<ChickenEntity> chickenList = world2.getEntitiesByType(EntityType.CHICKEN,new Box(-1000,-64,-1000,1000,1024,1000), ChickenEntity -> true);
        chickenList.forEach(ChickenEntity::kill);*/
        currentSearchRepeatCount++;
        setSearchDirection();
    }
    public static void search(int directionX, int directionZ, int refX, int refY, int refZ, int searchedCount){
        /*world.getPlayers().forEach(player -> {
            player.sendMessage(Text.literal("search 실행"));
        });*/
        // 커서를 기준점으로 초기화
        cursorX = refX;  cursorY = refY;  cursorZ = refZ;
        BlockPos cursorBlockPos = new BlockPos(cursorX,cursorY,cursorZ);
        boolean diagonalDirection = Math.abs(directionX) == Math.abs(directionZ);

        if(!diagonalDirection){ // 직선 탐색인 경우
            straightCount = 0;
            yMoveCount_Straight = 0;
        }else{
            resetCounters();
        }

        boolean loopOn = true;
        for(int i = 1; i <= MAX_SEARCH_RADIUS - searchedCount; i++){
            if(loopOn){
                if(!pathFindingOn){
                    break;
                }
                boolean leftJumpPointReady = false;
                boolean rightJumpPointReady = false;
                // 직선 탐색일 때만 실행 ( 점프 포인트 삼각 조건 검사 )
                if(!diagonalDirection){
                    // 왼쪽 점프 포인트 검사
                    if(diagonalBlockedL && straightCount == 0){ // 밑변 검사 false인 것으로 취급
                        leftJumpPointReady = true;
                    }else{
                        if(!isReachable(cursorBlockPos, getLRDirection(directionX,directionZ)[0],getLRDirection(directionX,directionZ)[1])){ // 삼각형 밑변 검사
                            leftJumpPointReady = true;
                        }else{
                            if(!isReachable(moveOneBlock(cursorBlockPos,getLRDirection(directionX,directionZ)[0],getLRDirection(directionX,directionZ)[1]),directionX,directionZ)// 삼각형 옆변 검사
                                    || !isReachable(cursorBlockPos, getLRDirection(directionX,directionZ)[2],getLRDirection(directionX,directionZ)[3])// 대각선 검사
                            ){
                                leftJumpPointReady = true;
                            }
                        }
                    }
                    // 오른쪽 점프 포인트 검사
                    if(diagonalBlockedR && straightCount == 0){
                        rightJumpPointReady = true;
                    }else{
                        if(!isReachable(cursorBlockPos, getLRDirection(directionX,directionZ)[4],getLRDirection(directionX,directionZ)[5])){ // 삼각형 밑변 검사
                            rightJumpPointReady = true;
                        }else{
                            if(!isReachable(moveOneBlock(cursorBlockPos,getLRDirection(directionX,directionZ)[4],getLRDirection(directionX,directionZ)[5]),directionX,directionZ) // 삼각형 옆변 검사
                                    || !isReachable(cursorBlockPos, getLRDirection(directionX,directionZ)[6],getLRDirection(directionX,directionZ)[7]) // 대각선 검사
                            ){
                                rightJumpPointReady = true;
                            }
                        }
                    }
                }else{ // 대각선 방향일 때 (전)
                    if(getObstacleForDSearch(cursorBlockPos, directionX,directionZ)){
                        int diagSearchRefX = cursorX;   int diagSearchRefY = cursorY;   int diagSearchRefZ = cursorZ;
                        if(pathFindingOn){
                            straightCount = 0;
                            yMoveCount_Straight = 0;
                            search(directionX,0, diagSearchRefX,diagSearchRefY,diagSearchRefZ,i);
                        }
                        if(pathFindingOn){
                            straightCount = 0;
                            yMoveCount_Straight = 0;
                            search(0,directionZ, diagSearchRefX,diagSearchRefY,diagSearchRefZ,i);
                        }
                        diagonalBlockedL = false;
                        diagonalBlockedR = false;
                        break;
                    }
                }
                // 입력된 방향으로 한 칸 이동 및 오르내림
                BlockPos preCursorPos = cursorBlockPos;
                cursorBlockPos = moveOneBlock(preCursorPos,directionX,directionZ);
                cursorX = cursorBlockPos.getX();    cursorY = cursorBlockPos.getY();    cursorZ = cursorBlockPos.getZ();
                // y좌표 높이 변동 검사
                if(preCursorPos.getY() != cursorY){
                    if(diagonalDirection){
                        yMoveCount_Diagonal++;
                    }else{
                        yMoveCount_Straight++;
                    }
                }
                if(!diagonalDirection){ // 직선 탐색인 경우
                    straightCount++;
                }else{ // 대각선 탐색인 경우
                    diagonalCount++;
                }
                // straightCount, diagonalCount 디버그 메시지
            /*world2.getPlayers().forEach(player -> {
                player.sendMessage(Text.literal(
                        "(" + cursorX + "," + cursorY + "," + cursorZ + ") tLength: " + trailedLength + ", sCount:" + straightCount + ", dCount: " + diagonalCount));
            });*/

                // 대각선 방향일 때만 실행 ( 전방 양 쪽 장애물 검사 , 후)
                if(getObstacleForDSearch(cursorBlockPos, directionX, directionZ)){
                    loopOn = false;
                    // 벌 소환으로 위치 표시
                    /*Entity bee = EntityType.BEE.spawn(world2,cursorBlockPos,SpawnReason.TRIGGERED);
                    NbtCompound beeData = bee.writeNbt(new NbtCompound());
                    beeData.putBoolean("NoAI",true);
                    bee.readNbt(beeData);
                    world2.spawnEntity(bee);*/
                }
                // 목적지에 도착했으면 탐색 종료
                if(cursorBlockPos.equals(endBlockPos)){
                    world2.getPlayers().forEach(player -> {
                        player.sendMessage(Text.literal("목적지 탐색 완료"));
                    });
                    backTracking();
                    pathFindingOn = false;
                    break;
                }

                // 이동한 좌표가 유효한지 검사
                if(isSteppable(cursorBlockPos)){
                    // 탐색 - 벌 소환
                    /*Entity bee = EntityType.BEE.spawn(world2,cursorBlockPos,SpawnReason.TRIGGERED);
                    NbtCompound entityData = bee.writeNbt(new NbtCompound());
                    entityData.putBoolean("NoAI",true);
                    bee.readNbt(entityData);
                    world2.spawnEntity(bee);*/

                    // 점프 포인트 삼각 검사 - 마지막 4번
                    boolean leftBlocked = false;    boolean rightBlocked = false;
                    if((leftJumpPointReady && isReachable(cursorBlockPos,getLRDirection(directionX,directionZ)[0],getLRDirection(directionX,directionZ)[1]))){
                        leftBlocked = true;
                    }
                    if ((rightJumpPointReady && isReachable(cursorBlockPos,getLRDirection(directionX,directionZ)[4],getLRDirection(directionX,directionZ)[5]))) {
                        rightBlocked = true;
                    }
                    if(leftBlocked || rightBlocked){ // 점프 포인트 생성
                        addOpenList(cursorBlockPos,directionX,directionZ,leftBlocked?1:0,rightBlocked?1:0);
                        break;
                    }
                }else{ // 장애물을 만나면 루프 종료
                    break;
                }
                if(i == MAX_SEARCH_RADIUS - searchedCount){ // 탐색 반경 끝에 오픈리스트 추가
                    addOpenList(cursorBlockPos,directionX,directionZ,0,0);
                }else{
                    // 대각선 탐색인 경우에만 실행
                    if(Math.abs(directionX) == Math.abs(directionZ)){
                        int diagSearchRefX = cursorX;   int diagSearchRefY = cursorY;   int diagSearchRefZ = cursorZ;
                        if(pathFindingOn){
                            straightCount = 0;
                            yMoveCount_Straight = 0;
                            search(directionX,0, diagSearchRefX,diagSearchRefY,diagSearchRefZ,i);
                        }
                        if(pathFindingOn){
                            straightCount = 0;
                            yMoveCount_Straight = 0;
                            search(0,directionZ, diagSearchRefX,diagSearchRefY,diagSearchRefZ,i);
                        }
                        diagonalBlockedL = false;
                        diagonalBlockedR = false;
                    }
                }
            }
        }
    }
    // 올라가기 & 내려가기
    static BlockPos climbAndDescend(BlockPos refPos){
        if(world2.getBlockState(refPos).isSolidBlock(world2,refPos)){ // 해당 칸이 막혔으면
            if(!world2.getBlockState(refPos.up(1)).isSolidBlock(world2,refPos.up(1))
                    && !world2.getBlockState(refPos.up(2)).isSolidBlock(world2,refPos.up(2))){ // 한칸 위로 올라갈 수 있는지 검사
                return refPos.up(1);
            }
        }else{ // 해당 칸이 뚫렸다면
            if(!world2.getBlockState(refPos.down(1)).isSolidBlock(world2,refPos.down(1))
                    && world2.getBlockState(refPos.down(2)).isSolidBlock(world2,refPos.down(2))) { // 한칸 밑으로 내려갈 수 있는지 검사
                return refPos.down(1);
            }
        }
        return refPos;
    }
    public static BlockPos moveOneBlock(BlockPos refPos, int directionX, int directionZ){
        int xPos = refPos.getX() + directionX;   int yPos = refPos.getY();   int zPos = refPos.getZ() + directionZ;
        BlockPos tempPos = new BlockPos(xPos,yPos,zPos);
        tempPos = climbAndDescend(tempPos);
        return tempPos;
    }
    // 밟고 올라설 수 있는지
    public static boolean isSteppable(BlockPos blockPos){
        if(!world2.getBlockState(blockPos).isSolidBlock(world2,blockPos) // 해당 칸과
        && !world2.getBlockState(blockPos.up(1)).isSolidBlock(world2,blockPos.up(1)) // 한 칸 위가 뚫려 있고
                && world2.getBlockState(blockPos.down(1)).isSolidBlock(world2,blockPos.down(1))){ // 바닥이 단단하면
            return true;
        }
        return false;
    }
    // 도달할 수 있는지
    public static boolean isReachable(BlockPos refPos, int directionX, int directionZ){
        BlockPos targetPos = moveOneBlock(refPos,directionX,directionZ);
        return isSteppable(targetPos);
    }
    // 이 메소드는 직선 탐색을 전제로 함 ( = 두 입력 중 하나는 0일 때). 탐색 방향 기준 왼쪽 방향과 오른쪽 방향, 대각선을 리턴
    // 리턴값의 [0],[1]번이 왼쪽 방향, [2],[3]번이 왼쪽 대각, [4],[5]번이 오른쪽 방향, [6],[7]번이 오른쪽 대각을 나타냄
    public static int[] getLRDirection(int directionInputX, int directionInputZ){
        if(directionInputX != 0){
            return new int[]{0,directionInputX*-1,  directionInputX,directionInputX*-1, 0,directionInputX,  directionInputX,directionInputX};
        }else{
            return new int[]{directionInputZ,0, directionInputZ,directionInputZ,    directionInputZ*-1,0,   directionInputZ*-1,directionInputZ};
        }
    }

    static boolean getObstacleForDSearch(BlockPos refPos, int directionX, int directionZ){
        if(Math.abs(directionX) == Math.abs(directionZ)){
            BlockPos tempPosLeft;
            BlockPos tempPosRight;
            if(directionX == directionZ){ // x방향 z방향 부호가 같으면: (1,1), (-1,-1)
                tempPosLeft = new BlockPos(refPos.getX()+directionX, refPos.getY(), refPos.getZ());
                tempPosRight = new BlockPos(refPos.getX(), refPos.getY(), refPos.getZ()+directionZ);
            }else{ // 부호 다르면: (1,-1), (-1,1)
                tempPosLeft = new BlockPos(refPos.getX(), refPos.getY(), refPos.getZ()+directionZ);
                tempPosRight = new BlockPos(refPos.getX()+directionX, refPos.getY(), refPos.getZ());
            }
            BlockPos frontPos = new BlockPos(refPos.getX()+directionX,refPos.getY(),refPos.getZ()+directionZ);
            frontPos = climbAndDescend(frontPos);
            int jumpCount = frontPos.getY() - refPos.getY();

            if(jumpCount >= 1){ // 올라가야 하는 좌표일 때
                if(world2.getBlockState(tempPosLeft.up(1)).isSolidBlock(world2,tempPosLeft.up(1))){
                    diagonalBlockedL = true;
                }
                if(world2.getBlockState(tempPosRight.up(1)).isSolidBlock(world2,tempPosRight.up(1))){
                    diagonalBlockedR = true;
                }
                return diagonalBlockedL || diagonalBlockedR;
            }else{ // 높이가 같거나 내려가는 좌표일 때
                if(world2.getBlockState(tempPosLeft).isSolidBlock(world2,tempPosLeft) || world2.getBlockState(tempPosLeft.up(1)).isSolidBlock(world2,tempPosLeft.up(1))){
                    diagonalBlockedL = true;
                }
                if(world2.getBlockState(tempPosRight).isSolidBlock(world2,tempPosRight) || world2.getBlockState(tempPosRight.up(1)).isSolidBlock(world2,tempPosRight.up(1))){
                    diagonalBlockedR = true;
                }
                return diagonalBlockedL || diagonalBlockedR;
            }
        }
        return false; // 직선 탐색이면 항상 false
    }
    // 점프 포인트를 오픈리스트에 추가
    public static void addOpenList(BlockPos blockPos, int directionX, int directionZ, int leftBlocked, int rightBlocked){
        boolean isAlreadyMarkedPos = false;
        // 주어진 좌표가 기존에 이미 오픈/클로즈 리스트에 등록된 좌표인지 확인
        if(openList != null){
            for (int[] openListElement : openList) {
                if (blockPos.getX() == openListElement[0]
                        && blockPos.getY() == openListElement[1]
                        && blockPos.getZ() == openListElement[2]) {
                    isAlreadyMarkedPos = true;
                }
            }
        }
        if(closedList != null && !isAlreadyMarkedPos){
            for (int[] closedListElement : closedList) {
                if (blockPos.getX() == closedListElement[0]
                        && blockPos.getY() == closedListElement[1]
                        && blockPos.getZ() == closedListElement[2]) {
                    isAlreadyMarkedPos = true;
                }
            }
        }
        if(!isAlreadyMarkedPos){ // 오픈리스트 요소 추가
            int gValue = trailedLength + straightCount + (int)(1.4*diagonalCount) + yMoveCount_Straight + yMoveCount_Diagonal;
            int fValue = gValue
                    + (int)(Math.sqrt(
                            Math.pow(endX - blockPos.getX(),2) + Math.pow(endY - blockPos.getY(),2) + Math.pow(endZ - blockPos.getZ(),2)
            ));
            openList.add(new int[]{blockPos.getX(), blockPos.getY(), blockPos.getZ(), fValue, directionX, directionZ, leftBlocked, rightBlocked, closedList.size()-1, gValue});
            // 오픈 리스트 갑옷 거치대 소환
            /*Entity armor_stand = EntityType.ARMOR_STAND.spawn(world2,blockPos,SpawnReason.TRIGGERED);
            world2.spawnEntity(armor_stand);*/
        }
    }
    static void backTracking(){
        // pathIndexList는 경유지들의 클로즈리스트 인덱스 번호를 모아 놓은 리스트
        pathIndexList.clear();
        pathIndexList.add(closedList.size()-1);
        // tempPos는 어떤 클로즈리스트 좌표의 이전 소스 좌표를 나타냄
        BlockPos tempPos = new BlockPos(closedList.get(pathIndexList.get(0))[0],closedList.get(pathIndexList.get(0))[1],closedList.get(pathIndexList.get(0))[2]);

        while(!tempPos.equals(startBlockPos)){
            pathIndexList.add(closedList.get(pathIndexList.get(pathIndexList.size()-1))[8]);
            tempPos = new BlockPos(closedList.get(pathIndexList.get(pathIndexList.size()-1))[0],closedList.get(pathIndexList.get(pathIndexList.size()-1))[1],closedList.get(pathIndexList.get(pathIndexList.size()-1))[2]);
        }

        StringBuilder tempString = new StringBuilder();
        for(int i=0; i < pathIndexList.size(); i++){
            int tempX = closedList.get(pathIndexList.get(i))[0];    int tempY = closedList.get(pathIndexList.get(i))[1];    int tempZ = closedList.get(pathIndexList.get(i))[2];
            if(i!=0){
                tempString.append(",");
            }
            // tempString은 좌표 목록
            tempString.append("(").append(tempX).append(",").append(tempY).append(",").append(tempZ).append(")");
            // 백 트래킹 갑옷 거치대 소환
            Entity armor_stand = EntityType.ARMOR_STAND.spawn(world2,new BlockPos(tempX,tempY,tempZ),SpawnReason.TRIGGERED);
            world2.spawnEntity(armor_stand);
            NbtCompound customData = new NbtCompound();
            customData.putBoolean("leftBlocked",(closedList.get(pathIndexList.get(i))[6] != 0));
            customData.putBoolean("rightBlocked",(closedList.get(pathIndexList.get(i))[7] != 0));
            ((IEntityDataSaver)armor_stand).getPersistentData().put("EntityData",customData);

        }

        /*world2.getPlayers().forEach(player -> {
            player.sendMessage(Text.literal(pathIndexList.toString()));
            player.sendMessage(Text.literal(String.valueOf(tempString)));
        });*/
        entityMovingMain();
    }
    static void entityMovingMain(){
        movingRepeatCount = 0;
        movingList = new ArrayList<>();
        int refX = 0;   int refZ = 0;
        int targetX = 0;     int targetZ = 0;

        // 경로 계산 본체
        callCalculating(refX, refZ, targetX, targetZ);

        // 계산 종료 이후
        /*world2.getPlayers().forEach(player -> {
            StringBuilder text = new StringBuilder();
            for (int k = 0; k < movingList.size(); k++){
                text.append(Arrays.toString(movingList.get(k)));
            }
            player.sendMessage(Text.literal(text.toString()));
        });*/

        // 엔티티 이동 준비 (초기 위치로 딱 맞추기)
        if(movingTimer == 0){
            // TODO: 포팅
            //targetEntity.teleport(startX + 0.5, startY, startZ +0.5);
        }
        // 엔티티 이동 시작
        entityMoving(); // 딜레이 없이 첫 번째 이동
        entityMovingOn = true; // 나머지는 onServerTicks() 로 이관
    }
    static void callCalculating(int refX, int refZ, int targetX, int targetZ){
        // 파라미터 설정
        refX = closedList.get(pathIndexList.get(pathIndexList.size()-1-movingRepeatCount))[0];
        refZ = closedList.get(pathIndexList.get(pathIndexList.size()-1-movingRepeatCount))[2];
        if(movingRepeatCount < pathIndexList.size()-1){
            targetX = closedList.get(pathIndexList.get(pathIndexList.size()-2-movingRepeatCount))[0];
            targetZ = closedList.get(pathIndexList.get(pathIndexList.size()-2-movingRepeatCount))[2];
        }else { //마지막엔 target좌표를 목적지로 설정 (목적지는 closedList에 없음)
            targetX = endX;
            targetZ = endZ;
        }
        // 메소드 반복
        calculateMovements(refX,refZ,targetX,targetZ);
    }
    static void calculateMovements(int refX, int refZ, int targetX, int targetZ){

        int directionX = (targetX > refX)?1:-1; int directionZ = (targetZ > refZ)?1:-1;
        int currentX = refX;    int currentY;    int currentZ = refZ;
        int diagonalCount = 0;  int straightCount = 0;

        while(targetX != currentX && targetZ != currentZ){ // 대각선 이동 입력
            currentX += directionX;
            currentZ += directionZ;
            diagonalCount++;
        }
        if(diagonalCount > 0){
            movingList.add(new int []{directionX,directionZ,diagonalCount});
        }
        if(targetX != currentX){
            directionZ = 0;
            while(targetX != currentX){ // x축 이동
                currentX += directionX;
                straightCount++;
            }
        }
        if(targetZ != currentZ){
            directionX = 0;
            while(targetZ != currentZ){ // z축 이동
                currentZ += directionZ;
                straightCount++;
            }
        }
        if(straightCount > 0){
            movingList.add(new int []{directionX,directionZ,straightCount});
        }

        // 목적지까지 반복
        if(movingRepeatCount < pathIndexList.size()-1){
            movingRepeatCount++;
            callCalculating(refX, refZ, targetX, targetZ);
        }
    }
    static void resetCounters(){
        straightCount = 0;
        diagonalCount = 0;
        yMoveCount_Straight = 0;
        yMoveCount_Diagonal = 0;
    }
}
