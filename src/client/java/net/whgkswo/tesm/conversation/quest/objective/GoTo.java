package net.whgkswo.tesm.conversation.quest.objective;

import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.calendar.DateTime;

public class GoTo extends QuestObjective{
    private BlockPos targetPos;

    public GoTo(String description, DateTime dueDatetime, ObjectiveType objectiveType, String objectTarget) {
        super(description, dueDatetime, objectiveType, objectTarget);
    }

    /*public GoTo(String description, ObjectiveType objectiveType, String objectTarget) {
        super(description, objectiveType, objectTarget);
    }*/
}
