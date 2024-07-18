package net.whgkswo.tesm.general;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.whgkswo.tesm.conversation.ConversationScreen;
import net.whgkswo.tesm.gui.screen.JournalScreen;

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
        });
    }
}
