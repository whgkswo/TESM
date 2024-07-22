package net.whgkswo.tesm.conversation.quest.objective;

import net.whgkswo.tesm.calendar.DateTime;

public class TalkTo extends QuestObjective{
    private String targetName;

    public TalkTo(String description, DateTime dueDatetime, ObjectiveType objectiveType, String objectTarget) {
        super(description, dueDatetime, objectiveType, objectTarget);
    }

    public TalkTo(String description, ObjectiveType objectiveType, String objectTarget) {
        super(description, objectiveType, objectTarget);
    }
}
