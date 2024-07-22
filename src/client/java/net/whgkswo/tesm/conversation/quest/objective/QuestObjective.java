package net.whgkswo.tesm.conversation.quest.objective;

import net.whgkswo.tesm.calendar.DateTime;

public class QuestObjective {
    private String description;
    private DateTime dueDatetime;
    private ObjectiveType objectiveType;
    private String objectTarget;
    public enum ObjectiveType{
        TALK_TO,
        GO_TO,
        COLLECT,
        KILL
    }

    public QuestObjective(String description, DateTime dueDatetime, ObjectiveType objectiveType, String objectTarget) {
        this.description = description;
        this.dueDatetime = dueDatetime;
        this.objectiveType = objectiveType;
    }

    public QuestObjective(String description, ObjectiveType objectiveType, String objectTarget) {
        this.description = description;
        this.objectiveType = objectiveType;
    }
    public QuestObjective(String description) {
        this.description = description;
    }
}
