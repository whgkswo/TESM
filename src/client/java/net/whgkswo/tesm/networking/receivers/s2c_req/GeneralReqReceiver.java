package net.whgkswo.tesm.networking.receivers.s2c_req;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.networking.payload.data.GeneralReq;
import net.whgkswo.tesm.networking.payload.id.GeneralTaskId;
import net.whgkswo.tesm.networking.receivers.s2c_req.handlers.ResetQuestsHandler;

public class GeneralReqReceiver {
    public static void handle(GeneralReq payload, ClientPlayNetworking.Context context){
        GeneralTaskId task;
        try{
            task = GeneralTaskId.valueOf(payload.getTaskId());
        } catch (IllegalArgumentException e) {
            GlobalVariables.player.sendMessage(Text.literal("유효하지 않은 S2C 요청 ID입니다. GeneralTaskId에 명시된 값만 사용해 주세요."), false);
            return;
        }
        // 들어온 요청에 따라 핸들러 맵핑
        switch (task){
            case GeneralTaskId.RESET_QUESTS -> ResetQuestsHandler.handle();
        }
    }
}
