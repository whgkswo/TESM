package net.whgkswo.tesm.conversationv2.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.whgkswo.tesm.conversationv2.screen.ConversationScreenV2;

public interface DecisionRevealEvent {
    Event<DecisionRevealEvent> EVENT = EventFactory.createArrayBacked(DecisionRevealEvent.class,
            (listeners) -> (screen) -> {
                for (DecisionRevealEvent listener : listeners){
                    listener.onDecisionReveal(screen);
                }
            });

    void onDecisionReveal(ConversationScreenV2 screen);
}
