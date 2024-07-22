package net.whgkswo.tesm.conversation;

import net.whgkswo.tesm.conversation.quest.QuestStatus;

public class QuestRequirement {
    private String questName;
    private QuestStatus status;
    private String stage;

    public String getQuestName() {
        return questName;
    }

    public QuestRequirement(String questName, QuestStatus status) {
        this.questName = questName;
        this.status = status;
    }
    public QuestRequirement(String questName, QuestStatus status, String stage) {
        this.questName = questName;
        this.status = status;
        this.stage = stage;
    }

    public QuestStatus getStatus() {
        return status;
    }

    public String getStage() {
        return stage;
    }
}
