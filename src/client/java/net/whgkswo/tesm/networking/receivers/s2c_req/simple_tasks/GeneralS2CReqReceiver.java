package net.whgkswo.tesm.networking.receivers.s2c_req.simple_tasks;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.networking.payload.data.SimpleReq;
import net.whgkswo.tesm.networking.payload.id.SimpleTask;

public class GeneralS2CReqReceiver {
    public static void handle(SimpleReq payload, ClientPlayNetworking.Context context){
        SimpleTask task;
        try{
            task = SimpleTask.valueOf(payload.getTaskId());
        } catch (IllegalArgumentException e) {
            GlobalVariables.player.sendMessage(Text.literal("유효하지 않은 S2C 요청 ID입니다. GeneralTaskId에 명시된 값만 사용해 주세요."), false);
            return;
        }
        // 들어온 요청에 따라 핸들러 맵핑
        switch (task){
            //case RESET_QUESTS -> ResetQuestsHandler.handle();
        }
    }
}
