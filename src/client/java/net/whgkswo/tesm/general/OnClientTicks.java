package net.whgkswo.tesm.general;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.whgkswo.tesm.conversation.ConversationScreen;
import net.whgkswo.tesm.gui.screen.JournalScreen;

public class OnClientTicks {

    public static int arrowCounter = 0;
    public static boolean arrowState = false;
    public static void getArrowState(){
        ClientTickEvents.END_CLIENT_TICK.register(client ->{
            if (ConversationScreen.upArrowOn()||ConversationScreen.downArrowOn()
            || JournalScreen.upArrowOn() || JournalScreen.downArrowOn()){
                arrowCounter++;
                if (arrowCounter>=15){
                    arrowState = !arrowState;
                    arrowCounter = 0;
                }
            }
        });
    }
}
