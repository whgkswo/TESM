package net.whgkswo.tesm.conversation.quest.objective;

import net.whgkswo.tesm.calendar.InGameDateTime;

public class QuestObjective {
    private String description;
    private InGameDateTime dueDatetime;
    private ObjectiveType objectiveType;
    private String objectiveTarget;
    private String nextStage;
    public enum ObjectiveType{
        TALK_TO,
        GO_TO,
        COLLECT,
        KILL
    }

    public QuestObjective(String description, InGameDateTime dueDatetime, ObjectiveType objectiveType, String objectiveTarget) {
        this.description = description;
        this.dueDatetime = dueDatetime;
        this.objectiveType = objectiveType;
        this.objectiveTarget = objectiveTarget;
    }

    public QuestObjective(String description, ObjectiveType objectiveType, String objectiveTarget, String nextStage) {
        this.description = description;
        this.objectiveType = objectiveType;
        this.objectiveTarget = objectiveTarget;
        this.nextStage = nextStage;
    }
    public QuestObjective(String description, String nextStage) {
        this.description = description;
        this.nextStage = nextStage;
    }

    public String getDescription() {
        return description;
    }

    public InGameDateTime getDueDatetime() {
        return dueDatetime;
    }

    public ObjectiveType getObjectiveType() {
        return objectiveType;
    }

    public String getObjectiveTarget() {
        return objectiveTarget;
    }

    public String getNextStage() {
        return nextStage;
    }
}
