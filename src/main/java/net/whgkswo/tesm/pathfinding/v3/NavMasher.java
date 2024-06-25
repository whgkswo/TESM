package net.whgkswo.tesm.pathfinding.v3;

import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.Heightmap;
import net.whgkswo.tesm.customDataType.SizedStack;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.pathfinding.v2.BlockStateHelper;

import java.util.HashMap;

import static net.whgkswo.tesm.general.GlobalVariables.player;

public class NavMasher {
    private static final int NAVMESH_RADIUS = 5;
    private final SizedStack<Boolean> stack = new SizedStack<>(2);
    private int cursorX;    private int cursorY;    private int cursorZ;
    boolean prevPosIsObstacle;
    HashMap<BlockPos, Boolean> validPosMap = new HashMap<>();

    public void navMesh(){
        BlockPos playerPos = new BlockPos(player.getBlockX(), player.getBlockY(), player.getBlockZ());
        Box box = new Box(playerPos.getX() - NAVMESH_RADIUS,0, playerPos.getZ() - NAVMESH_RADIUS,
                playerPos.getX() + NAVMESH_RADIUS,0,playerPos.getZ() + NAVMESH_RADIUS);
        player.sendMessage(Text.literal(box.toString()));

        // 유효한 좌표를 찾기
        getSteppableBlocks(box);
    }
    private void getSteppableBlocks(Box box){
        cursorX = (int) box.minX;  cursorZ = (int) box.minZ;

        for(int i = 0; i< NAVMESH_RADIUS*2 + 1; i++){
            cursorZ = (int) box.minZ;
            for(int j = 0; j< NAVMESH_RADIUS*2 + 1; j++){
                // 표면부터 등록하고 시작
                addSurfaceToMap();
                prevPosIsObstacle = true;

                while(cursorY > GlobalVariables.world.getBottomY()){
                    if(prevPosIsObstacle){
                        down3Blocks();
                    }else{
                        down1Blocks();
                    }
                    if(BlockStateHelper.isObstacle(new BlockPos(cursorX,cursorY,cursorZ))){
                        prevPosIsObstacle = true;
                        // 트랩 말고 진짜로 유효한, 단단한 블럭이었다면
                        if(!BlockStateHelper.isTrapBlock(new BlockPos(cursorX,cursorY,cursorZ))){
                            if(!stack.get(0) && !stack.get(1)){
                                // 유효한 좌표
                                validPosMap.put(new BlockPos(cursorX,cursorY,cursorZ), true);
                            }
                        }
                    }
                }
                cursorZ++;
            }
            cursorX ++;
        }
        //player.sendMessage(Text.literal(validPosMap.toString()));
        player.sendMessage(Text.literal(validPosMap.size() + ""));
    }
    private void addSurfaceToMap(){
        //player.sendMessage(Text.literal(String.format("(%d, %d)", cursorX,cursorZ)));
        cursorY = GlobalVariables.world.getTopPosition(Heightmap.Type.WORLD_SURFACE, new BlockPos(cursorX,0,cursorZ)).getY();
        validPosMap.put(new BlockPos(cursorX, cursorY, cursorZ), true);
    }
    private void down3Blocks(){
        cursorY -= 3;
        stack.clear();
        stack.push(BlockStateHelper.isObstacle(new BlockPos(cursorX,cursorY,cursorZ).up(2)));
        stack.push(BlockStateHelper.isObstacle(new BlockPos(cursorX,cursorY,cursorZ).up()));
        prevPosIsObstacle = false;
    }
    private void down1Blocks(){
        cursorY -= 1;
        stack.push(BlockStateHelper.isObstacle(new BlockPos(cursorX,cursorY,cursorZ).up()));
    }
}
