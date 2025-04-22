package net.whgkswo.tesm.conversationv2.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.whgkswo.tesm.conversationv2.screen.ConversationScreenV2;

public interface DecisionHideEvent {
    Event<DecisionHideEvent> EVENT = EventFactory.createArrayBacked(DecisionHideEvent.class,
            (listeners) -> (screen) -> {
                for (DecisionHideEvent listener : listeners){
                    listener.onDecisionHide(screen);
                }
            });

    void onDecisionHide(ConversationScreenV2 screen);
}
