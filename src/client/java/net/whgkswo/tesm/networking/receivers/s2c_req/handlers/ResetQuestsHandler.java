package net.whgkswo.tesm.networking.receivers.s2c_req.handlers;

import net.whgkswo.tesm.conversation.quest.Quest;
import net.whgkswo.tesm.conversation.quest.QuestStatus;

public class ResetQuestsHandler {
    public static void handle(){
        Quest.QUESTS.forEach(
                (key, value) -> {
                    value.setStatus(QuestStatus.AVAILABLE);
                    value.setCurrentStage(value.getStartingStage());
                }
        );
    }
}
