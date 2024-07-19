package net.whgkswo.tesm.conversation.quest;

import net.whgkswo.tesm.conversation.quest.objective.QuestObjective;

import java.util.List;

public class Quest {
    private String name;
    private String description;
    private QuestType questType;
    private String subType;
    private List<QuestObjective> objectives;
    private boolean isCanBeStarted;
}
