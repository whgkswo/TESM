package net.whgkswo.tesm.networking.receivers.c2s_general;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.text.Text;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.networking.payload.data.SimpleReq;
import net.whgkswo.tesm.networking.payload.id.SimpleTask;
import net.whgkswo.tesm.networking.receivers.c2s_general.handlers.TickFreezeHandler;

public class GeneralC2SReqReceiver {
    public static void handle(SimpleReq payload, ServerPlayNetworking.Context context){
        SimpleTask task;
        try{
            task = SimpleTask.valueOf(payload.getTaskId());
        } catch (IllegalArgumentException e) {
            GlobalVariables.player.sendMessage(Text.literal("유효하지 않은 C2S 요청 ID입니다. GeneralTaskId에 명시된 값만 사용해 주세요."), false);
            return;
        }
        // 들어온 요청에 따라 핸들러 맵핑
        switch (task){
            case TICK_FREEZE -> TickFreezeHandler.freeze();
            case TICK_UNFREEZE -> TickFreezeHandler.unfreeze();
        }
    }
}
