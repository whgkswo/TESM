package net.whgkswo.tesm.conversation.quest.objective;

import net.whgkswo.tesm.calendar.InGameDateTime;

public class TalkTo extends QuestObjective{
    private String targetName;

    public TalkTo(String description, InGameDateTime dueDatetime, ObjectiveType objectiveType, String objectTarget) {
        super(description, dueDatetime, objectiveType, objectTarget);
    }

    /*public TalkTo(String description, ObjectiveType objectiveType, String objectTarget) {
        super(description, objectiveType, objectTarget);
    }*/
}
