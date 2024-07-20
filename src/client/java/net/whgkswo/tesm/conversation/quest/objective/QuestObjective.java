package net.whgkswo.tesm.conversation.quest.objective;

import net.whgkswo.tesm.calendar.DateTime;

public class QuestObjective {
    private String description;
    private DateTime dueDatetime;
    private ObjectiveType objectiveType;
    enum ObjectiveType{
        TALK_TO,
        GO_TO,
        COLLECT,
        KILL
    }
}
