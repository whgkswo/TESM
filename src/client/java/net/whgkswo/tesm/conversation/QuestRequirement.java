package net.whgkswo.tesm.conversation;

import net.whgkswo.tesm.conversation.quest.QuestStatus;

public class QuestRequirement {
    private String questName;
    private QuestStatus status;

    public QuestRequirement(String questName, QuestStatus status) {
        this.questName = questName;
        this.status = status;
    }

    public String getQuestName() {
        return questName;
    }

    public QuestStatus getStatus() {
        return status;
    }
}
