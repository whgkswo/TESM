package net.whgkswo.tesm.general;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.player.PlayerEntity;

public class InitializeTasks {

    /*이곳에서 수행되지 못한 작업들은 OnServerTick에서 runTimeCounter=0일 때 이루어지고 있음.
    변수들은 게임을 종료하면 초기화되지만, 메인화면으로 나갔다 들어오는 경우에는 초기화되지 않기 때문에 이곳에서 직접 초기화해야 함*/
    public static void registerPlayer(){
        ServerEntityEvents.ENTITY_LOAD.register((entity, serverWorld) -> {
            if(entity.isPlayer()){
                GlobalVariables.player = (PlayerEntity) entity;
            }
        });
    }
}
