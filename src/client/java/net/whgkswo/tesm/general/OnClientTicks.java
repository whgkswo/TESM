package net.whgkswo.tesm.general;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import static net.whgkswo.tesm.general.GlobalVariablesClient.arrowCounter;
import static net.whgkswo.tesm.general.GlobalVariablesClient.arrowState;

public class OnClientTicks {
    public static void getArrowState(){
        ClientTickEvents.END_CLIENT_TICK.register(client ->{
            arrowCounter++;
            if (arrowCounter>=15){
                arrowState = !arrowState;
                arrowCounter = 0;
            }

            // 화면 사이즈 업데이트
            GlobalVariablesClient.screenWidth = client.getWindow().getScaledWidth();
            GlobalVariablesClient.screenHeight = client.getWindow().getScaledHeight();
        });
    }
}
