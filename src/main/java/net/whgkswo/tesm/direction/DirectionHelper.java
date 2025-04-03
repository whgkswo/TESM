package net.whgkswo.tesm.direction;

import net.minecraft.util.math.Direction;
import net.whgkswo.tesm.pathfinding.v2.PathfindDirection;

public class DirectionHelper {
    public static boolean isInSameHalfPlane(Direction direction, float yaw){
          // yaw값은 작은 쪽을 포함해야 함 (마인크래프트에서 북쪽은 180이 아닌 -180)
        switch (direction){
            case NORTH -> {
                return yaw >= 90 || yaw < -90;
            }
            case EAST -> {
                return yaw < 0;
            }
            case SOUTH -> {
                return yaw >= -90 && yaw < 90;
            }
            case WEST -> {
                return yaw >= 0;
            }
            default -> {
                return false;
            }
        }
    }

    public static float getOppositeYaw(float yaw){
        if(yaw >= 0){
            return yaw - 180;
        }else{
            return yaw + 180;
        }
    }
}
