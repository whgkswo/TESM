package net.whgkswo.tesm.conversation.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.whgkswo.tesm.conversation.screen.ConversationScreen;

public interface DecisionRevealEvent {
    Event<DecisionRevealEvent> EVENT = EventFactory.createArrayBacked(DecisionRevealEvent.class,
            (listeners) -> (screen) -> {
                for (DecisionRevealEvent listener : listeners){
                    listener.onDecisionReveal(screen);
                }
            });

    void onDecisionReveal(ConversationScreen screen);
}
