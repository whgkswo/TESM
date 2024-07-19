package net.whgkswo.tesm.conversation.quest.objective;

import net.whgkswo.tesm.calendar.Date;

public class QuestObjective {
    private String description;
    private Date dueDate;
    private ObjectiveType objectiveType;
    enum ObjectiveType{
        TALK_TO,
        GO_TO,
        COLLECT,
        KILL
    }
}
