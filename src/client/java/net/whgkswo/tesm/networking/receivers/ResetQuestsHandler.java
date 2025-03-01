package net.whgkswo.tesm.networking.receivers;

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
