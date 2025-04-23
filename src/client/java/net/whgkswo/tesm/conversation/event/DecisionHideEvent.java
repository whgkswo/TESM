package net.whgkswo.tesm.conversation.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.whgkswo.tesm.conversation.screen.ConversationScreen;

public interface DecisionHideEvent {
    Event<DecisionHideEvent> EVENT = EventFactory.createArrayBacked(DecisionHideEvent.class,
            (listeners) -> (screen) -> {
                for (DecisionHideEvent listener : listeners){
                    listener.onDecisionHide(screen);
                }
            });

    void onDecisionHide(ConversationScreen screen);
}
